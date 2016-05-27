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
package com.gwtmodel.table.view.ewidget;

import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.IGetListOfIcons;
import com.gwtmodel.table.view.ewidget.gwt.RadioBoxString;

public interface IEditWidget {

	IFormLineView constructLabelField(IVField v, String displayName);

	IFormLineView constructHTMLField(IVField v);

	IFormLineView constructAnchorField(IVField v);

	RadioBoxString constructRadioBoxString(IVField v, IGetDataList iGet, final boolean enable, String htmlName);

	IFormLineView contructCalculatorNumber(IVField v, String htmlName);

	IFormLineView constructCheckField(IVField v, String text, String htmlName);

	IFormLineView constructListValuesCombo(IVField v, IDataType dType, String htmlName);

	// used
	IFormLineView constructListValuesCombo(IVField v, IGetDataList iGet, boolean addEmpty, String htmlName);

	IFormLineView constructHelperList(IVField v, IDataType dType, boolean refreshAlways, String htmlName);

	IFormLineView constructPasswordField(IVField v, String htmlName);

	IFormLineView constructTextField(IVField v, String htmlName);

	IFormLineView constructTextField(IVField v, IGetDataList iGet, IRequestForGWidget iHelper, boolean textarea,
			boolean richtext, boolean refreshAlways, String htmlName);

	IFormLineView constructLabelFor(IVField v, String la);

	IFormLineView construcDateBoxCalendar(IVField v, String htmlName);

	IFormLineView constructDateBoxCalendarWithHelper(IVField v, IRequestForGWidget i, boolean refreshAlways,
			String htmlName);

	IFormLineView constructRadioSelectField(IVField v, String htmlName);

	IFormLineView constructListComboValuesHelp(IVField v, IDataType dType, String htmlName);

	IFormLineView constructEditFileName(IVField v, String htmlName);

	IFormLineView constructListCombo(IVField v, List<String> ma, String htmlName);

	IFormLineView constructListCombo(IVField v, List<String> ma, boolean addEmpty, String htmlName);

	IFormLineView constructListCombo(IVField v, String htmlName);

	IFormLineView constructSpinner(IVField v, String htmlName, int min, int max);

	IFormLineView constructSuggestBox(IVField v, IGetDataList iGet, String htmlName);

	IFormLineView constructEmail(IVField v, String htmlName);

	IFormLineView constructImageButton(IVField v, String htmlName, int imageNo, IGetListOfIcons iList);

	IFormLineView constructDateTimePicker(IVField v, String htmlName);
	
	IFormLineView constructListComboEnum(IVField v, String htmlName);
}
