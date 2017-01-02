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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.CheckList;
import com.jythonui.shared.DateLine;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DisclosureElemPanel;
import com.jythonui.shared.ElemDescription;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.TabPanel;

/**
 * @author hotel
 * 
 */
class ValidateDialogFormat extends UtilHelper {

	private ValidateDialogFormat() {
	}

	private static void idNotNull(String tag, ElemDescription e) {
		if (CUtil.EmptyS(e.getId())) {
			errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE25, ILogMess.CANNOTBEEMPTY, tag));
		}
	}

	private static <T extends ElemDescription> void validateL(String lTag, String eTag, List<T> eList) {
		Set<String> sId = new HashSet<String>();
		for (ElemDescription e : eList) {
			idNotNull(lTag + " " + eTag, e);
			String id = e.getId();
			if (sId.contains(id))
				errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE26, ILogMess.TAGVALUEDUPLICATED, lTag, eTag,
						ICommonConsts.ID, id));

			sId.add(id);
		}
	}

	private static void tagExists(DialogFormat d, ElemDescription e, String tag, String... tagList) {
		for (String s : tagList) {
			String val = e.getAttr(s);
			if (CUtil.EmptyS(val))
				errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE56, ILogMess.TAGCANNOTBEEMPTY, d.getId(), tag, s));

		}
	}

	private static <T extends ElemDescription> void findTag(DialogFormat d, ElemDescription e, String findTag,
			String listTag, List<T> eList) {
		String id = e.getAttr(findTag);
		if (DialogFormat.findE(eList, id) != null)
			return;
		errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE57, ILogMess.DIALOGCANNOTFINDINLIST, d.getId(), findTag, id,
				listTag));
	}

	private static void checkListCustomButton(DialogFormat d, ElemDescription l) {
		if (!l.isAttr(ICommonConsts.STANDBUTT))
			return;
		String sta = l.getAttr(ICommonConsts.STANDBUTT);
		String[] cButtons = sta.split(",");
		for (String s : cButtons) {
			String customButt = FieldItem.getCustomT(s);
			if (CUtil.EmptyS(customButt))
				continue;
			// check if custom button is action list
			ButtonItem b = DialogFormat.findE(d.getActionList(), customButt);
			if (b == null) {
				errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE76, ILogMess.STANDBUTTONNOTINACTION, d.getId(),
						l.getId(), sta, customButt, ICommonConsts.ACTIONS));
			}
		}

	}

	private static <T extends ElemDescription> void verifyDuplicatedID(Set<String> sId, List<T> eList, String tagId) {
		for (T t : eList) {
			String id = t.getId();
			if (sId.contains(id))
				errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE27, ILogMess.TAGDUPLICATEDITENDTIFIER, tagId,
						ICommonConsts.FIELD, id));
			sId.add(id);
		}
	}

	private static void verifyTypes(DialogFormat d, String liName, List<FieldItem> li) {
		for (FieldItem i : li) {
			String t = i.getCustom();
			if (CUtil.EmptyS(t))
				continue;
			if (d.findCustomType(t) == null)
				if (liName == null)
					errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE124, ILogMess.CUSTOMTYPENOTRECOGNIZED,
							d.getId(), i.getId(), i.getTypeName(), t));
				else
					errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE125, ILogMess.CUSTOMTYPENOTRECOGNIZEDLIST,
							d.getId(), liName, i.getId(), i.getTypeName(), t));
		}

	}

	private static void verifyStatic(DialogFormat d) {
		for (FieldItem f : d.getFieldList()) {
			if (!f.isSuggest())
				continue;
			// suggest
			if (f.isEmailType() || f.isDownloadType() || f.isUploadType() || f.isHtmlType() || f.isLabel()
					|| f.isPassword() || f.isRichText() || f.isTextArea() || f.isSpinner())
				errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE125, ILogMess.ITEMCANNOTBESUGGEST, d.getId(),
						f.getId()));
		}

	}

	static void validate(DialogFormat d) {
		validateL(ICommonConsts.LEFTMENU, ICommonConsts.BUTTON, d.getLeftButtonList());
		validateL(ICommonConsts.CHECKLIST, ICommonConsts.CHECKLIST, d.getCheckList());
		validateL(ICommonConsts.CHARTTYPE, ICommonConsts.COLUMN, d.getChartList());
		validateL(ICommonConsts.FORM, ICommonConsts.FIELD, d.getFieldList());
		for (ListFormat l : d.getListList()) {
			idNotNull(ICommonConsts.LIST, l);
			validateL(ICommonConsts.COLUMNS, ICommonConsts.COLUMN, l.getColumns());

		}
		Set<String> sId = new HashSet<String>();
		for (FieldItem f : d.getFieldList()) {
			sId.add(f.getId());
		}
		for (ListFormat l : d.getListList()) {
			String id = l.getId();
			if (sId.contains(id))
				errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE27, ILogMess.TAGDUPLICATEDITENDTIFIER, l.getId(),
						ICommonConsts.FIELD, id));
			// check for editable, boolean and signalbefore
			for (FieldItem f : l.getColumns()) {
				if (f.isColumnEditable() && f.getFieldType() == TT.BOOLEAN && f.isSignalBefore())
					errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE71, ILogMess.CANNOTEDITBOOLEANBEFORE, d.getId(),
							f.getId(), ICommonConsts.SIGNALBEFORE));

				if (f.isHelper() && f.getCustom() != null)
					errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE94, ILogMess.CANNOTCUSTOMANDHELPER, d.getId(),
							l.getId(), f.getId()));
			}
			sId.add(id);
		}
		verifyDuplicatedID(sId, d.getChartList(), ICommonConsts.CHARTLIST);
		for (ListFormat l : d.getListList())
			checkListCustomButton(d, l);
		for (CheckList c : d.getCheckList()) {
			idNotNull(ICommonConsts.CHECKLIST, c);
			String type = c.getAttr(ICommonConsts.TYPE);
			if (CUtil.EmptyS(type))
				continue;
			if (type.equals(ICommonConsts.BOOLTYPE))
				continue;
			if (type.equals(ICommonConsts.DECIMALTYPE))
				continue;
			String errMess = SHolder.getM().getMess(IErrorCode.ERRORCODE45, ILogMess.UNEXPECTEDCHECKLISTTYPE, c.getId(),
					type, ICommonConsts.BOOLTYPE, ICommonConsts.DATETIMETYPE);
			errorLog(errMess);
		}
		for (DateLine dl : d.getDatelineList()) {
			idNotNull(ICommonConsts.DATELINE, dl);
			validateL(ICommonConsts.COLUMNS, ICommonConsts.COLUMN, dl.getColList());
			String dId = dl.getListId();
			if (CUtil.EmptyS(dId)) {
				errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE51, ILogMess.DATELINEIDCANNOTBEEMPTY, d.getId(),
						ICommonConsts.DATELINE, ICommonConsts.DATELINEID));
			}
			FieldItem f = DialogFormat.findE(dl.getColList(), dId);
			if (f == null)
				errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE52, ILogMess.DATELINEIDCANNOTBEFOUND, d.getId(),
						ICommonConsts.DATELINE, ICommonConsts.DATELINEID, dId, ICommonConsts.COLUMNS));
			tagExists(d, dl, ICommonConsts.DATELINE, ICommonConsts.DATELINEDEFAFILE);
			validateL(ICommonConsts.DATELINE, ICommonConsts.DATELINEFORMS, dl.getFormList());
			findTag(d, dl, ICommonConsts.DATELINEDEFAFILE, ICommonConsts.DATELINEFORMS, dl.getFormList());
			checkListCustomButton(d, dl);
		}
		for (TabPanel t : d.getTabList())
			validateL(ICommonConsts.TABPANEL, ICommonConsts.TABPANELELEM, t.gettList());

		validateL(ICommonConsts.DISCLOSUREPANEL, ICommonConsts.DISCLOUSREPANELELEM, d.getDiscList());

		for (DisclosureElemPanel s : d.getDiscList())
			tagExists(d, s, ICommonConsts.DISCLOSUREPANEL, ICommonConsts.HTMLPANEL);

		// verify custom types
		verifyTypes(d, null, d.getFieldList());
		for (ListFormat l : d.getListList())
			verifyTypes(d, l.getId(), l.getColumns());
		verifyStatic(d);
	}
}
