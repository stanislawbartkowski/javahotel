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
package com.gwtmodel.table.view.util.polymer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperDialog;

public class YesNoDialog extends Composite {

	interface YesNoBinder extends UiBinder<HTMLPanel, YesNoDialog> {
	}

	@UiField
	PaperDialog yesdialog;
	
	@UiField
	Label title;
	
	@UiField
	Label mess;
	
	@UiField
	PaperButton cancelbutton;
	
	@UiField
	PaperButton okbutton;

	private static YesNoBinder ourUiBinder = GWT.create(YesNoBinder.class);

	public YesNoDialog() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}
}
