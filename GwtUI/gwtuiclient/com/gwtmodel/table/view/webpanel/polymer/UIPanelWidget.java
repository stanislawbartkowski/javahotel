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
package com.gwtmodel.table.view.webpanel.polymer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.polymer.paper.widget.PaperDrawerPanel;
import com.vaadin.polymer.paper.widget.PaperIconButton;

public class UIPanelWidget extends Composite {
	interface MainUiBinder extends UiBinder<HTMLPanel, UIPanelWidget> {
	}

	@UiField
	HTMLPanel htmlPanel;

	@UiField
	Image titleIcon;

	@UiField
	Image progressIcon;
	@UiField
	HTMLPanel progressHtml;

	@UiField
	PaperDrawerPanel drawerPanel;

	@UiField
	Label personLabel;
	@UiField
	Label productLabel;
	@UiField
	Label ownerLabel;
	@UiField
	Label infoLabel;
	@UiField
	Label hotelLabel;

	@UiField
	PaperIconButton exitIcon;
	
	@UiField
	PaperIconButton menuIcon;					


	private static MainUiBinder ourUiBinder = GWT.create(MainUiBinder.class);

	public UIPanelWidget() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}
}
