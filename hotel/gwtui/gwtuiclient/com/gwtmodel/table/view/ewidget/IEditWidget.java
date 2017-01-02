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
package com.gwtmodel.table.view.ewidget;

import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.IGetListOfIcons;
import com.gwtmodel.table.view.ewidget.gwt.RadioBoxString;

public interface IEditWidget {

	IFormLineView constructBinderField(IVField v, IFormFieldProperties pr);

	IFormLineView constructLabelField(IVField v, IFormFieldProperties pr, String displayName);

	IFormLineView constructHTMLField(IVField v, IFormFieldProperties pr);

	IFormLineView constructAnchorField(IVField v, IFormFieldProperties pr);

	RadioBoxString constructRadioBoxString(IVField v, IFormFieldProperties pr, IGetDataList iGet, final boolean enable);

	IFormLineView contructCalculatorNumber(IVField v, IFormFieldProperties pr);

	IFormLineView constructCheckField(IVField v, IFormFieldProperties pr);

	IFormLineView constructListValuesCombo(IVField v, IFormFieldProperties pr, IDataType dType);

	// used
	IFormLineView constructListValuesCombo(IVField v, IFormFieldProperties pr, IGetDataList iGet, boolean addEmpty);

	IFormLineView constructHelperList(IVField v, IFormFieldProperties pr, IDataType dType, boolean refreshAlways);

	IFormLineView constructPasswordField(IVField v, IFormFieldProperties pr);

	IFormLineView constructTextField(IVField v, IFormFieldProperties pr);

	IFormLineView constructTextField(IVField v, IFormFieldProperties pr, IGetDataList iGet, IRequestForGWidget iHelper,
			boolean textarea, boolean richtext, boolean refreshAlways);

	IFormLineView constructLabelFor(IVField v, IFormFieldProperties pr, String la);

	IFormLineView construcDateBoxCalendar(IVField v, IFormFieldProperties pr);

	IFormLineView constructDateBoxCalendarWithHelper(IVField v, IFormFieldProperties pr, IRequestForGWidget i,
			boolean refreshAlways);

	IFormLineView constructRadioSelectField(IVField v, IFormFieldProperties pr);

	IFormLineView constructListComboValuesHelp(IVField v, IFormFieldProperties pr, IDataType dType);

	IFormLineView constructEditFileName(IVField v, IFormFieldProperties pr);

	IFormLineView constructListCombo(IVField v, IFormFieldProperties pr, List<String> ma);

	IFormLineView constructListCombo(IVField v, IFormFieldProperties pr, List<String> ma, boolean addEmpty);

	IFormLineView constructListCombo(IVField v, IFormFieldProperties pr);

	IFormLineView constructSpinner(IVField v, IFormFieldProperties pr, int min, int max);

	IFormLineView constructSuggestBox(IVField v, IFormFieldProperties pr, IGetDataList iGet);

	IFormLineView constructEmail(IVField v, IFormFieldProperties pr);

	IFormLineView constructImageButton(IVField v, IFormFieldProperties pr, int imageNo, IGetListOfIcons iList);

	IFormLineView constructDateTimePicker(IVField v, IFormFieldProperties pr);

	IFormLineView constructListComboEnum(IVField v, IFormFieldProperties pr);
}
