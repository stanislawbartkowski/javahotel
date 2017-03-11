/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jythonui.client.dialog.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.jythonui.shared.ButtonItem;
import com.polymerui.client.eventbus.ButtonEvent;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.eventbus.ISubscriber;
import com.polymerui.client.util.Utils;
import com.polymerui.client.view.util.PolymerUtil;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.vaadin.widget.VaadinDatePicker;
import com.vaadin.polymer.vaadin.widget.VaadinDatePickerLight;

class EnrichWidgets {

	private EnrichWidgets() {

	}

	private static <T extends Widget> T castP(Widget w, Class<T> cl) {

		if (w.getClass().equals(cl))
			return (T) w;
		return null;

	}

	static void enrich(IEventBus iBus, HTMLPanel ha, ISubscriber<ButtonItem> i) {

		PolymerUtil.walkHTMLPanel(ha, (fieldid, w) -> {

			VaadinDatePickerLight d1 = castP(w, VaadinDatePickerLight.class);
			if (d1 != null) {
				Object oo = Utils.geti18N("");
				d1.setI18n((JavaScriptObject) oo);
			}
			VaadinDatePicker d2 = castP(w, VaadinDatePicker.class);
			if (d2 != null) {
				Object oo = Utils.geti18N("");
				d2.setI18n((JavaScriptObject) oo);
			}

			PaperButton b1 = castP(w, PaperButton.class);
			if (b1 != null) {
				ButtonItem bu = new ButtonItem();
				bu.setId(fieldid);
				ButtonEvent be = new ButtonEvent(bu);
				iBus.subscribe(be, i);
				b1.addClickHandler(p -> {
					iBus.publish(be);
				});
			}

		});
	}

}
