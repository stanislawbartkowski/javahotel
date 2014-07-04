/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.util.VerifyXML;
import com.jythonui.server.IGetDialog;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.IUserCacheHandler;
import com.jythonui.server.Util;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.ReadResourceFactory;
import com.jythonui.server.security.ISecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.TypesDescr;

/**
 * @author hotel
 * 
 */
public class GetDialog extends UtilHelper implements IGetDialog {

    private final IJythonUIServerProperties p;
    private final ISecurity iSec;
    // private final ICommonCacheFactory mFactory;
    private final IGetLogMess logMess;
    private final IUserCacheHandler iUserCache;

    @Inject
    public GetDialog(IJythonUIServerProperties p, ISecurity iSec,
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess logMess,
            IUserCacheHandler iUserCache) {
        this.p = p;
        this.iSec = iSec;
        this.logMess = logMess;
        this.iUserCache = iUserCache;
    }

    private static final String XSDDIR = "xsd";
    private static final String DIALOGXSD = "dialogschema.xsd";
    private static final String TYPESXSD = "typedefschema.xsd";

    private static IReadResource iRead = new ReadResourceFactory()
            .constructLoader(GetDialog.class.getClassLoader());

    private void parseError(String errCode, String param, Exception e) {
        errorLog(logMess.getMess(errCode, ILogMess.DIALOGXMLPARSERROR, param),
                e);
    }

    private void error(String errCode, String plogMess, String param) {
        errorLog(logMess.getMess(errCode, plogMess, param));
    }

    private URL getURLSchema(String schemaname) {
        logDebug("Search schema " + schemaname);
        URL ur = iRead.getRes(XSDDIR + "/" + schemaname);
        if (ur == null) {
            errorLog(logMess.getMess(IErrorCode.ERRORCODE16,
                    ILogMess.SCHEMANOTFOUND));
        }
        return ur;
    }

    private InputStream getXML(String name) throws FileNotFoundException {
        return Util.getFile(p, name);
    }

    @Override
    public DialogFormat getDialog(String token, String dialogName,
            boolean verify) {
        DialogFormat d;
        if (Holder.isAuth() && CUtil.EmptyS(token)) {
            errorLog(logMess.getMess(IErrorCode.ERRORCODE8,
                    ILogMess.AUTOENABLEDNOTOKEN, dialogName));
            return null;
        }
        d = (DialogFormat) iUserCache.get(token, dialogName);
        if (d != null)
            return d;
        d = getDialogDirectly(token, dialogName, verify);
        String dParentName = d.getParent();
        if (dParentName != null) {
            DialogFormat dParent = getDialog(token, dParentName, false);
            for (ListFormat lo : dParent.getListList()) {
                if (lo.getfElem() != null
                        && lo.getfElem().getId().equals(dialogName)) {
                    return lo.getfElem();
                }
            }
            errorLog(logMess.getMess(IErrorCode.ERRORCODE9,
                    ILogMess.ELEMDOESNOTMATCHPARENT, dParentName, dialogName));
        }
        if (d != null)
            iUserCache.put(token, dialogName, d);
        return d;
    }

    private DialogFormat getDialogDirectly(String token, String dialogName,
            boolean verify) {
        DialogFormat d = null;
        try {
            URL u = getURLSchema(DIALOGXSD);
            InputStream sou;
            if (verify) {
                logDebug("Verify using xsd schema " + DIALOGXSD);
                sou = getXML(dialogName);
                VerifyXML.verify(u, new StreamSource(sou));
            }
            sou = getXML(dialogName);
            d = ReadDialog.parseDocument(p, sou, iSec);
            if (d != null) {
                d.setId(dialogName);
                if (verify) {
                    ValidateDialogFormat.validate(d);
                }
            }
            String typesNames = d.getAttr(ICommonConsts.TYPES);
            if (!CUtil.EmptyS(typesNames)) {
                String[] tList = typesNames.split(",");
                for (String typesName : tList) {
                    if (verify) {
                        u = getURLSchema(TYPESXSD);
                        sou = getXML(typesName);
                        VerifyXML.verify(u, new StreamSource(sou));
                    }
                    sou = getXML(typesName);
                    TypesDescr types = ReadTypes.parseDocument(sou, iSec);
                    d.getTypeList().add(types);
                }
            }
            // now check elemformat for lists
            if (d.getListList() != null) {
                for (ListFormat l : d.getListList()) {
                    if (l.getElemFormat() != null) {
                        // recursive
                        if (dialogName.equals(l.getElemFormat())) {
                            errorLog(logMess.getMess(IErrorCode.ERRORCODE75,
                                    ILogMess.PARENTCANNOTBETHESAME,
                                    ICommonConsts.PARENT, dialogName));
                            return null;
                        }
                        DialogFormat dElem = getDialogDirectly(token,
                                l.getElemFormat(), verify);
                        boolean wasmodified = false;
                        if (dElem.getFieldList().isEmpty()) {
                            // if there is no field list in the XML
                            // then copy parent column list
                            // dElem.setFieldList(l.getColumns());
                            dElem.getFieldList().addAll(l.getColumns());
                            logDebug(l.getElemFormat()
                                    + " copy list of columns from "
                                    + dialogName);
                            wasmodified = true;
                        }
                        String[] attrList = { ICommonConsts.IMPORT,
                                ICommonConsts.METHOD };
                        for (int i = 0; i < attrList.length; i++) {
                            String a = attrList[i];
                            if (dElem.getAttr(a) == null) {
                                wasmodified = true;
                                dElem.setAttr(a, d.getAttr(a));
                                logDebug(l.getElemFormat() + " copy attribute "
                                        + a + " from " + dialogName);
                            }
                        }
                        if (wasmodified) {
                            if (dElem.getParent() == null) {
                                errorLog(dElem.getId() + " "
                                        + ICommonConsts.PARENT
                                        + " attribute expected");
                            }
                            if (!dElem.getParent().equals(dialogName)) {
                                String mess = logMess.getMess(
                                        IErrorCode.ERRORCODE74,
                                        ILogMess.PARENTFILEEXPECTED,
                                        dElem.getId(), ICommonConsts.PARENT,
                                        dElem.getParent());
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
