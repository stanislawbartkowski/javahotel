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
package com.jythonui.shared;

import java.util.HashSet;
import java.util.Set;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.shared.JythonUIFatal;

/**
 * @author hotel
 * 
 */
public class FieldItem extends ElemDescription {

	private static final long serialVersionUID = 1L;

	private static final Set<String> stringType = new HashSet<String>();

	static {
		stringType.add(ICommonConsts.EMAILTYPE);
		stringType.add(ICommonConsts.STRINGTYPE);
		stringType.add(ICommonConsts.TEXTAREA);
		stringType.add(ICommonConsts.RICHTEXT);
		stringType.add(ICommonConsts.UPLOADTYPE);
		stringType.add(ICommonConsts.DOWNLOADTYPE);
		stringType.add(ICommonConsts.HTMLTYPE);
		stringType.add(ICommonConsts.PASSWORD);
		stringType.add(ICommonConsts.AJAXTYPE);
	}

	public String getTypeName() {
		return getAttr(ICommonConsts.TYPE);
	}

	public static int getAfterDot(String t) {
		if (CUtil.EmptyS(t)) {
			return ICommonConsts.DEFAULTAFTERDOT;
		}
		return CUtil.getNumb(t.trim());
	}

	public int getAfterDot() {
		return getAfterDot(getAttr(ICommonConsts.AFTERDOT));
	}

	public static String getCustomT(String t) {
		int p = t.indexOf(ICommonConsts.CUSTOMTYPE);
		if (p != 0) {
			return null;
		}
		return t.substring(ICommonConsts.CUSTOMTYPE.length());
	}

	public String getCustom() {
		String t = getTypeName();
		if (CUtil.EmptyS(t)) {
			return null;
		}
		return getCustomT(t);
	}

	private TT getFieldType(String t) {
		if (CUtil.EmptyS(t) || stringType.contains(t))
			return TT.STRING;
		if (getCustom() != null)
			return TT.STRING;

		if (isImageColumn())
			return TT.STRING;

		if (CUtil.EqNS(t, ICommonConsts.BOOLTYPE))
			return TT.BOOLEAN;

		if (CUtil.EqNS(t, ICommonConsts.DATETIMETYPE))
			return TT.DATETIME;

		if (CUtil.EqNS(t, ICommonConsts.DATETYPE))
			return TT.DATE;

		if (CUtil.EqNS(t, ICommonConsts.LONGTYPE))
			return TT.LONG;

		if (CUtil.EqNS(t, ICommonConsts.INTTYPE))
			return TT.INT;

		if (CUtil.EqNS(t, ICommonConsts.SPINNERTYPE))
			return TT.INT;

		if (CUtil.EqNS(t, ICommonConsts.DECIMALTYPE))
			return TT.BIGDECIMAL;

		throw new JythonUIFatal(t + " unexpected type name");
	}

	public TT getFieldType() {
		return getFieldType(getTypeName());
	}

	public boolean isNotEmpty() {
		return isAttr(ICommonConsts.NOTEMPTY);
	}

	public boolean isHidden() {
		return isAttr(ICommonConsts.HIDDEN);
	}

	private boolean isType(String val) {
		return CUtil.EqNS(getTypeName(), val);
	}

	public boolean isUploadType() {
		return isType(ICommonConsts.UPLOADTYPE);
	}

	public boolean isEmailType() {
		return isType(ICommonConsts.EMAILTYPE);
	}

	public boolean isDownloadType() {
		return isType(ICommonConsts.DOWNLOADTYPE);
	}

	public boolean isHtmlType() {
		return isType(ICommonConsts.HTMLTYPE);
	}

	public boolean isReadOnlyAdd() {
		return isAttr(ICommonConsts.READONLYADD);
	}

	public boolean isReadOnlyChange() {
		return isAttr(ICommonConsts.READONLYCHANGE);
	}

	public String getActionId() {
		return getAttr(ICommonConsts.ACTIONID);
	}

	public boolean isSignalChange() {
		return isAttr(ICommonConsts.SIGNALCHANGE) || isAttr(ICommonConsts.JSSIGNALCHANGE);
	}

	public String getJsSignalChange() {
		return getAttr(ICommonConsts.JSSIGNALCHANGE);
	}

	public boolean isHelper() {
		return isAttr(ICommonConsts.HELPER);
	}

	public boolean isHelperRefresh() {
		return isAttr(ICommonConsts.HELPERREFRESH);
	}

	public boolean isTextArea() {
		return isType(ICommonConsts.TEXTAREA);
	}

	public boolean isSpinner() {
		return isType(ICommonConsts.SPINNERTYPE);
	}

	public boolean isRichText() {
		return isType(ICommonConsts.RICHTEXT);
	}

	public boolean isPassword() {
		return isType(ICommonConsts.PASSWORD);
	}

	public String getFrom() {
		return getAttr(ICommonConsts.FROM);
	}

	public String getVisLines() {
		return getAttr(ICommonConsts.VISLINES);
	}

	public String getAlign() {
		return getAttr(ICommonConsts.ALIGN);
	}

	public String getDefValue() {
		return getAttr(ICommonConsts.DEFVALUE);
	}

	public boolean isDefValue() {
		return isAttr(ICommonConsts.DEFVALUE);
	}

	public boolean isFooter() {
		return isAttr(ICommonConsts.FOOTER);
	}

	public boolean isColumnEditable() {
		return isAttr(ICommonConsts.EDITCOL);
	}

	public boolean isSignalBefore() {
		return isAttr(ICommonConsts.SIGNALBEFORE);
	}

	public TT getFooterType() {
		String tName = getAttr(ICommonConsts.FOOTERTYPE);
		if (CUtil.EmptyS(tName))
			tName = getTypeName();
		return getFieldType(tName);
	}

	public String getFooterAlign() {
		if (isAttr(ICommonConsts.FOOTERALIGN))
			return getAttr(ICommonConsts.FOOTERALIGN);
		return getAlign();
	}

	public int getFooterAfterDot() {
		String a = getAttr(getAttr(ICommonConsts.FOOTERAFTERDOT));
		if (CUtil.EmptyS(a))
			a = getAttr(ICommonConsts.AFTERDOT);
		return getAfterDot(a);
	}

	public String getCellTitle() {
		return getAttr(ICommonConsts.CELLTITLE);
	}

	public boolean isImageColumn() {
		return isAttr(ICommonConsts.IMAGECOLUMN);
	}

	public boolean isSuggest() {
		return isAttr(ICommonConsts.SUGGEST) || isAttr(ICommonConsts.SUGGESTKEY) || isAttr(ICommonConsts.SUGGESTSIZE);
	}

	public String getSuggestKey() {
		return getAttr(ICommonConsts.SUGGESTKEY);
	}

	public int getSuggestSize() {
		return getInt(ICommonConsts.SUGGESTSIZE, ICommonConsts.DEFAULTSUGGESTSIZE);
	}

	public int getImageColumn() {
		String t = getAttr(ICommonConsts.IMAGECOLUMN);
		if (CUtil.EmptyS(t)) {
			return 1;
		}
		return CUtil.getNumb(t.trim());
	}

	public String getImageList() {
		return getAttr(ICommonConsts.IMAGELIST);
	}

	public String getEditCss() {
		return getAttr(ICommonConsts.EDITCSS);
	}

	public String getEditClass() {
		return getAttr(ICommonConsts.EDITCLASS);
	}

	public boolean isLabel() {
		return isAttr(ICommonConsts.LABEL);
	}

	public String getColumnClass() {
		return getAttr(ICommonConsts.COLUMNCLASS);
	}

	public String getHeaderClass() {
		return getAttr(ICommonConsts.HEADERCLASS);
	}

	public int getSpinnerMin() {
		return getInt(ICommonConsts.SPINNERMIN, ICommonConsts.DEFAULTSPINNERMIN);
	}

	public int getSpinnerMax() {
		return getInt(ICommonConsts.SPINNERMAX, ICommonConsts.DEFAULTSPINNERMAX);
	}

	public boolean isRemember() {
		return isAttr(ICommonConsts.REMEMBER) || isAttr(ICommonConsts.REMEMBERKEY);
	}

	public String getRememberKey() {
		return getAttr(ICommonConsts.REMEMBERKEY);
	}

	public boolean isMulti() {
		return isAttr(ICommonConsts.MULTI);
	}

	public boolean isMenu() {
		return isAttr(ICommonConsts.MENU);
	}

	public boolean isBinderField() {
		return isAttr(ICommonConsts.BINDER);
	}

	public boolean isAjaxField() {
		return isType(ICommonConsts.AJAXTYPE);
	}

}
