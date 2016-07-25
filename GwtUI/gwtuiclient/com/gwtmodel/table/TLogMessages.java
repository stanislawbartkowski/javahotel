/*
 *  Copyright 2016 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table;

/**
 *
 * @author hotel
 */
import com.google.gwt.i18n.client.Messages;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface TLogMessages extends Messages {

	String assertT(String exp, String given);

	String errorEnum(String val, String eClass);

	String slStringLog(String lName);

	String slStringLog1(String lName, String p1);

	String slStringLog2(String lName, String p1, String p2);

	String publishLogNull(String slName);

	String publishLog(String slname);

	String choosedEdit(String dName);

	String cannotBeNull();

	String slCallerNull(String slName);

	String slCallerNotFound(String slName);

	String slCaller(String slName);

	String FilterCannotbeNull();

	String sendSignalLog(String slName);

	String sendSignalNotFound(String slName);

	String receivedSignalLog(String slName);

	String receivedSignalLogParam(String slName, String param);

	String dTypeCannotBeNull();

	String filterCannotBeNull();

	String clickChangeNewLog(String change, String set);

	String widgetCanotbeNull();

	String errorWhileReading(String zmNamw);

	String CellCannotBeNull();

	String CellShouldBeRegistered();

	String GetterModelDataViewModel();

	String GetterContainerDataViewModel();

	String GetterWidgetDataViewModel();

	String mustBeConnectedToString();

	String notFoundSignalNotNull();

	String errrorUploadingFile();

	String notExpected();

	String formRecordTitle(String title, String action);

	String GWTVersion(String ver, String jVersion);

	String DataListCannotBeNull();

	String HeaderNull();

	String InternalError();

	String RowSelectedNotFound();

	String InproperWidthInColumn(String colName, String width);

	String InproperColumnUnit(String colName, String unit);

	String ValueNotExpectedHere();

	String ColumnCellUndefined();

	String SelectListCannotBeEmpty();

	String NotImplemented();

	String CannotFindChunkForIndex(int i);

	String CannotRecgonizeSortColumn();

	String GrigHtmlName(int lno, int cno);

	String WChoosedInfo(int rowno, String s);

	String NewEditLineFocus(String wChoose);

	String NextClickedAction(String place, String wChoose);

	String TablePresentationSetEditRow(int rowno);

	String TablePresentationSelectionChange(int rowno);

	String TablePresentationSelectionChangeNow(int rowno);

	String PresentationTableNullSetValObj(String id);

	String NoEditCheckBox();

	String NullValueHeaderPos(int pos);

	String CannotReadThisUrl(String url);

	String CannotReadThisUrlCode(String url, int errocode);

	String PolymerDialogNotImplemented(String mess);

	String BinderWidgetNoPanels();

	String BinderCannotHaveWidgets(String id);

	String BinderCannotFindWidget(String id);

	String BinderNotHTMLPanel(String wId);

	String BinderButtonShouldBeFocusWidget();

	String PolymerButtonShouldBePolymerWidget(String atype);

	String PanelCannotFindWidget(String id);

	String PupupPolymerDialogShouldBePaperDialog(String expected, String received);

	String ReplaceWidgetNotImplemented(String id);

	String ReplaceTypeNotCorrect(String desc, String expectedType, String givenType);

}
