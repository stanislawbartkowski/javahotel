/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.client;

import com.google.gwt.i18n.client.Messages;

/**
 * @author hotel
 * 
 */
public interface JMessages extends Messages {

	String ErrorNoValue(String key);

	String ListDoesNotHaveELem(String listId, String attrName);

	String UnknownAction(String action, String param);

	String CustomTypeNotDefine();

	String CannotFindCustomType(String typeName);

	String CannotFindFromField(String from, String fid);

	String ValidateAttributeNotDefined(String id);

	String ValidateFieldNotFound(String id, String fid);

	String NoFieldRelatedToError(String id, String fie);

	String cannotLoadClientResource();

	String CannotStartWithThisPage(String page);

	String NoCheckList(String id);

	String CheckListWithoutLinesOrColumns(String id);

	String CannotFindRowInCheckRows(String checkId, String id);

	String CannotFindColInCheckColumns(String checkId, String id);

	String InproperFormatCheckSet(String id, String j);

	String CannotFindCheckList(String s, String checkid);

	String CheckListActionNotExpected(String s, String a);

	String DataLineNotDefined(String dLineId, String tagId, String dId);

	String NoValuesRelatedTo(String dialogId, String datelineId);

	String NoFormRelatedToValue(String dialogId, String datelineId, String formId);

	String FooterSetValueShouldBeBoolean(String dialId, String varName);

	String FooterSetDefinedButValueBot(String bValue, String value);

	String CannotCallActionHere(String mess);

	String BeforeValueNotFound(int row, String col);

	String HelperOnlyForStringType(String id);

	String TabNotFilled(String tabid, String elemid);

	String NotValidStatusTextType(String varName, String typeId);

	String SetCookieValueShoulbBool(String cName);

	String OnlyStringColumnImage(String id, String type, String typeS, String ima);

	String CustomTypeIsNull(String customType);

	String ErrorWhileUploading();

	String SearchFailed();

	String JavaScriptInvalideType(String type, String attr);

	String RowReferenceIsNull(String id);

	String ValueForAttributeShouldBeNull(String aName);

	String RelativeDialogNameNotAllowed(String dialogName);

	String FirstYearCannotBeGreateThenLastYear(int firstY, int lastY);

	String YearValueInvalid(String param, String value, int iValue);

	String CurrentDateNotInRange(int cY, int fY, int lY);

	String ValueShouldBeString(String var, String type);

	String CookieNameStore(String cId, String dName, String listName, String id);

	String InvalidFieldType(String field, String aType, String expectedType);

	String ListOfImagesCannotBeEmpty(String field, String paramImages);

	String ListOfImagesDoNotComply(String field, String paramImages, String list, int no);

}
