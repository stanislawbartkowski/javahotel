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
package com.gwtmodel.table.view.webpanel.polymerui;

import com.gwtmodel.table.view.webpanel.polymer.PanelElemWidgets;

public class UIPanelWidgetConstruct {

	private UIPanelWidgetConstruct() {

	}

	public static PanelElemWidgets construct() {
		UIPanelWidget uW = new UIPanelWidget();
		PanelElemWidgets pW = new PanelElemWidgets();
		pW.drawerPanel = uW.drawerPanel;
		pW.exitIcon = uW.exitIcon;
		pW.hotelLabel = uW.hotelLabel;
		pW.htmlPanel = uW.htmlPanel;
		pW.infoLabel = uW.infoLabel;
		pW.menuIcon = uW.menuIcon;
		pW.ownerLabel = uW.ownerLabel;
		pW.personLabel = uW.personLabel;
		pW.productLabel = uW.productLabel;
		pW.progressHtml = uW.progressHtml;
		pW.progressIcon = uW.progressIcon;
		pW.titleIcon = uW.titleIcon;
		pW.panelWidget = uW;
		return pW;

	}

}
