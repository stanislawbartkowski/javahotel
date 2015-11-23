/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author hotel
 * 
 */
public class DialogFormat extends ElemDescription {

	private static final long serialVersionUID = 1L;

	private List<FieldItem> fieldList = new ArrayList<FieldItem>();

	private List<ButtonItem> leftButtonList = new ArrayList<ButtonItem>();
	private List<ButtonItem> upMenuList = new ArrayList<ButtonItem>();
	private List<ButtonItem> leftStackList = new ArrayList<ButtonItem>();

	private List<ListFormat> listList = new ArrayList<ListFormat>();

	private List<ButtonItem> buttonList = new ArrayList<ButtonItem>();

	private List<ButtonItem> actionList = new ArrayList<ButtonItem>();

	private List<TypesDescr> typeList = new ArrayList<TypesDescr>();

	private List<ValidateRule> valList = new ArrayList<ValidateRule>();

	private List<CheckList> checkList = new ArrayList<CheckList>();

	private List<DateLine> datelineList = new ArrayList<DateLine>();

	private List<TabPanel> tabList = new ArrayList<TabPanel>();

	private List<DisclosureElemPanel> discList = new ArrayList<DisclosureElemPanel>();

	private List<ChartFormat> chartList = new ArrayList<ChartFormat>();

	/**
	 * @return the actionList
	 */
	public List<FieldItem> getFieldList() {
		return fieldList;
	}

	/**
	 * @return the leftButtonList
	 */
	public List<ButtonItem> getLeftButtonList() {
		return leftButtonList;
	}

	public String getJythonMethod() {
		return getAttr(ICommonConsts.METHOD);
	}

	public String getJythonImport() {
		return getAttr(ICommonConsts.IMPORT);
	}

	public static <T extends ElemDescription> T findE(List<T> eList, String id) {
		if (eList == null) {
			return null;
		}
		for (T e : eList) {
			if (e.eqId(id)) {
				return e;
			}
		}
		return null;
	}

	public FieldItem findFieldItem(String id) {
		return findE(fieldList, id);
	}

	public List<ListFormat> getListList() {
		return listList;
	}

	public ListFormat findList(String id) {
		return findE(listList, id);
	}

	public ChartFormat findChart(String id) {
		return findE(chartList, id);
	}

	public boolean isBefore() {
		return isAttr(ICommonConsts.BEFORE);
	}

	public String getParent() {
		return getAttr(ICommonConsts.PARENT);
	}

	public List<ButtonItem> getButtonList() {
		return buttonList;
	}

	public List<ButtonItem> getActionList() {
		return actionList;
	}

	public List<TypesDescr> getTypeList() {
		return typeList;
	}

	public List<ValidateRule> getValList() {
		return valList;
	}

	public List<CheckList> getCheckList() {
		return checkList;
	}

	public TypedefDescr findCustomType(String customType) {
		for (TypesDescr ty : typeList) {
			TypedefDescr te = findE(ty.getTypeList(), customType);
			if (te != null) {
				return te;
			}
		}
		return null;
	}

	public CheckList findCheckList(String id) {
		return findE(checkList, id);
	}

	public List<DateLine> getDatelineList() {
		return datelineList;
	}

	public DateLine findDateLine(String id) {
		return findE(datelineList, id);
	}

	private static void addDataCols(List<FieldItem> colList, String dataType, String... dTag) {
		for (String s : dTag) {
			FieldItem fItem = new FieldItem();
			fItem.setAttr(ICommonConsts.TYPE, dataType);
			fItem.setId(s);
			colList.add(fItem);
		}
	}

	public static void addDefDataCols(List<FieldItem> colList, String... dTag) {
		addDataCols(colList, ICommonConsts.DATETYPE, dTag);
	}

	public static void addDefStringCols(List<FieldItem> colList, String... dTag) {
		addDataCols(colList, ICommonConsts.STRINGTYPE, dTag);
	}

	public String getHtmlPanel() {
		return getAttr(ICommonConsts.HTMLPANEL);
	}

	public String getHtmlLeftMenu() {
		return getAttr(ICommonConsts.HTMLLEFTMENU);
	}

	public boolean isJsCode() {
		return isAttr(ICommonConsts.JSCODE);
	}

	public String getJsCode() {
		return getAttr(ICommonConsts.JSCODE);
	}

	public String getCssCode() {
		return getAttr(ICommonConsts.CSSCODE);
	}

	public boolean isHtmlPanel() {
		return isAttr(ICommonConsts.HTMLPANEL);
	}

	public List<TabPanel> getTabList() {
		return tabList;
	}

	public boolean isAsXmlList() {
		return isAttr(ICommonConsts.ASXML);
	}

	public boolean isFormPanel() {
		return isAttr(ICommonConsts.FORMPANEL);
	}

	public boolean isAutoHideDialog() {
		return isAttr(ICommonConsts.AUTOHIDE);
	}

	public boolean isModelessDialog() {
		return isAttr(ICommonConsts.MODELESS);
	}

	public String getAsXmlList() {
		return getAttr(ICommonConsts.ASXML);
	}

	public List<ButtonItem> getUpMenuList() {
		return upMenuList;
	}

	public List<ButtonItem> getLeftStackList() {
		return leftStackList;
	}

	public boolean isClearLeft() {
		return isAttr(ICommonConsts.CLEARLEFT);
	}

	public boolean isClearCentre() {
		return isAttr(ICommonConsts.CLEARCENTRE);
	}

	public List<DisclosureElemPanel> getDiscList() {
		return discList;
	}

	public void setDiscList(List<DisclosureElemPanel> discList) {
		this.discList = discList;
	}

	public List<ChartFormat> getChartList() {
		return chartList;
	}

	public int getTop() {
		return getIntAttr(ICommonConsts.TOP);
	}

	public int getLeft() {
		return getIntAttr(ICommonConsts.LEFT);
	}

	public int getMaxTop() {
		return getIntAttr(ICommonConsts.MAXTOP);
	}

	public int getMaxLeft() {
		return getIntAttr(ICommonConsts.MAXLEFT);
	}

	public boolean isSignalClose() {
		return isAttr(ICommonConsts.SIGNALCLOSE);
	}

}
