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
package com.gwtmodel.table.view.ewidget.polymer;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.vaadin.polymer.paper.widget.PaperIconButton;

abstract class TextHelperImage extends PolymerTextField {

	protected final HorizontalPanel vp = new HorizontalPanel();
	protected final PaperIconButton bu = new PaperIconButton();
		
	protected TextHelperImage(IVField v, IFormFieldProperties pr, String pattern, String standErrMess,String image) {
		super(v, pr, standErrMess, pattern, false);
		bu.setIcon(image);
		bu.setAttributes("suffix");
		vp.add(in);
		vp.add(bu);
	}
	
	@Override
	public Widget getGWidget() {
		return vp;
	}


}
