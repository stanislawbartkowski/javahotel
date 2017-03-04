/*
 *  Copyright 2017 stanislawbartkowski@gmail.com 
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
package com.jythonui.client;

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

	String PolymerWidgetNotImplemented(String wType);

	String BinderCannotFindWidget(String id);

	String BinderCannotHaveWidgets(String id);

	String BinderNotHTMLPanel(String wId);

	String BinderWidgetNoPanels();

	String AttributeNotRecognized(String attr, String val);

	String InvalidValueForAttribute(String attr, String val);

	String ActionSupportedOnlyForWidget(String action, String allowed, String found);

	String CannotFindElementForAction(String action, String id);

	String WidgetToSetAttrNotImplemented();

	String InvalidWidgetAction(String action, String allowed);

	String MainPanelNotImplemented(String infoName);

	String MainPanelCannotFindWidget(String infoName, String labelid);

	String ActionWidgetNotAppropriate(String desc, String wType, String eList);

	String MainPanelActionName(String infoName, String widgetId);

	String HTMLPanelCannotFindWIdget(String desc, String fieldid);

	String MainPanelIconWidget(String id);

	String GWTVersion(String j1);

	String PanelHTMLInfo(String id);
	
	String CannotLoadClientResource();

	String CannotLoadMainPanelBinder(String panelName);
	
	String DialogShouldContainBinder(String dName);


}
