/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jythonui.server.dialog;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.IBinderParser;
import com.jythonui.server.IGetDialog;
import com.jythonui.server.IGetResourceFile;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.IUserCacheHandler;
import com.jythonui.server.IVerifySchema;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.security.ISecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.SUtil;
import com.jythonui.shared.TypesDescr;

/**
 * @author hotel
 * 
 */
public class GetDialog extends UtilHelper implements IGetDialog {

	private final ISecurity iSec;
	private final IGetLogMess logMess;
	private final IUserCacheHandler iUserCache;
	private final IGetResourceFile iGetResource;
	private final IBinderParser iBinder;
	private final IVerifySchema iVerify;

	@Inject
	public GetDialog(ISecurity iSec, @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess logMess,
			IUserCacheHandler iUserCache, IGetResourceFile iGetResource, IBinderParser iBinder, IVerifySchema iVerify) {
		this.iSec = iSec;
		this.logMess = logMess;
		this.iUserCache = iUserCache;
		this.iGetResource = iGetResource;
		this.iBinder = iBinder;
		this.iVerify = iVerify;
	}

	private static final String DIALOGXSD = "dialogschema.xsd";
	private static final String TYPESXSD = "typedefschema.xsd";

	private void parseError(String errCode, String param, Exception e) {
		errorLog(logMess.getMess(errCode, ILogMess.DIALOGXMLPARSERROR, param), e);
	}

	private void error(String errCode, String plogMess, String param) {
		errorLog(logMess.getMess(errCode, plogMess, param));
	}

	private InputStream getXML(String name) {
		return iGetResource.getDialogFile(name);
	}

	@Override
	public DialogFormat getDialog(String token, String dialogName, boolean verify) {
		DialogFormat d;
		if (Holder.isAuth() && CUtil.EmptyS(token)) {
			errorLog(logMess.getMess(IErrorCode.ERRORCODE8, ILogMess.AUTOENABLEDNOTOKEN, dialogName));
			return null;
		}
		d = (DialogFormat) iUserCache.get(token, dialogName);
		if (d != null)
			return d;
		d = getDialogDirectly(token, dialogName, verify);
		String dParentName = d.getParent();
		if (dParentName != null) {
			String pName = SUtil.getFileName(dialogName, dParentName);
			DialogFormat dParent = getDialog(token, pName, false);
			for (ListFormat lo : dParent.getListList()) {
				if (lo.getfElem() != null && lo.getfElem().getId().equals(dialogName)) {
					return lo.getfElem();
				}
			}
			errorLog(logMess.getMess(IErrorCode.ERRORCODE9, ILogMess.ELEMDOESNOTMATCHPARENT, dParentName, dialogName));
		}
		if (d != null)
			iUserCache.put(token, dialogName, d);
		return d;
	}

	private DialogFormat getDialogDirectly(String token, String dialogName, boolean verify) {
		DialogFormat d = null;
		try {
			if (verify)
				iVerify.verify(getXML(dialogName), DIALOGXSD);
			InputStream sou = getXML(dialogName);
			d = ReadDialog.parseDocument(dialogName, sou, iSec, iGetResource, iBinder);
			if (d != null)
				d.setId(dialogName);
			String typesNames = d.getAttr(ICommonConsts.TYPES);
			if (!CUtil.EmptyS(typesNames)) {
				String[] tList = typesNames.split(",");
				for (String typesName : tList) {
					if (verify)
						iVerify.verify(getXML(typesName), TYPESXSD);
					sou = getXML(typesName);
					TypesDescr types = ReadTypes.parseDocument(sou, iSec);
					d.getTypeList().add(types);
				}
			}
			if (verify)
				ValidateDialogFormat.validate(d);

			// now check elemformat for lists
			if (d.getListList() != null) {
				for (ListFormat l : d.getListList()) {
					if (l.getElemFormat() != null) {
						// recursive
						if (dialogName.equals(l.getElemFormat())) {
							errorLog(logMess.getMess(IErrorCode.ERRORCODE75, ILogMess.PARENTCANNOTBETHESAME,
									ICommonConsts.PARENT, dialogName));
							return null;
						}
						String elemName = SUtil.getFileName(dialogName, l.getElemFormat());
						DialogFormat dElem = getDialogDirectly(token, elemName, verify);
						boolean wasmodified = false;
						if (dElem.getFieldList().isEmpty()) {
							// if there is no field list in the XML
							// then copy parent column list
							// dElem.setFieldList(l.getColumns());
							dElem.getFieldList().addAll(l.getColumns());
							logDebug(l.getElemFormat() + " copy list of columns from " + dialogName);
							wasmodified = true;
						}
						String[] attrList = { ICommonConsts.IMPORT, ICommonConsts.METHOD };
						for (int i = 0; i < attrList.length; i++) {
							String a = attrList[i];
							if (dElem.getAttr(a) == null) {
								wasmodified = true;
								dElem.setAttr(a, d.getAttr(a));
								logDebug(l.getElemFormat() + " copy attribute " + a + " from " + dialogName);
							}
						}
						if (wasmodified) {
							if (dElem.getParent() == null) {
								errorLog(dElem.getId() + " " + ICommonConsts.PARENT + " attribute expected");
							}
							String dName = SUtil.getFileName(d.getId(), dElem.getParent());
							if (!dName.equals(dialogName)) {
								String mess = logMess.getMess(IErrorCode.ERRORCODE74, ILogMess.PARENTFILEEXPECTED,
										dElem.getId(), ICommonConsts.PARENT, dElem.getParent());
								errorLog(mess);
							}
							// cache again with changes
							iUserCache.put(token, dElem.getId(), dElem);
						}
						l.setfElem(dElem);
					}
				}
			}

		} catch (SAXException e) {
			parseError(IErrorCode.ERRORCODE12, dialogName, e);
		} catch (IOException e) {
			parseError(IErrorCode.ERRORCODE13, dialogName, e);
		} catch (ParserConfigurationException e) {
			parseError(IErrorCode.ERRORCODE14, dialogName, e);
		}
		if (d == null)
			error(IErrorCode.ERRORCODE15, ILogMess.DIALOGNOTFOUND, dialogName);
		else
			iUserCache.put(token, dialogName, d);
		// }
		return d;

	}

}
