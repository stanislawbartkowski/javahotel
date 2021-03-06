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
package com.gwtmodel.table.editw;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IGetSetVField;

public interface IFormLineView extends IGetSetVField, IGWidget {

	void addChangeListener(IFormChangeListener cListener);

	void setReadOnly(boolean readOnly);

	void setHidden(boolean hidden);

	void setInvalidMess(String errmess);

	void setGStyleName(String styleMess, boolean set);

	void setOnTouch(ITouchListener lTouch);

	String getHtmlName();

	void setAttr(String attrName, String attrValue);

	void setCellTitle(String title);

	void setSuggestList(List<String> list);

	void setFocus(boolean focus);

	boolean isInvalid();
	
	void replaceWidget(Widget w);

}
