/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.mygwt.client.impl.tiptest;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.view.button.ImgButtonFactory;
import com.gwtmodel.table.view.util.PopupTip;
import com.mygwt.client.testEntryPoint;

public class TooltipTest implements testEntryPoint.IGetWidget {

	private class LabTip extends PopupTip {

		LabTip() {
			Label l = new Label("Label");
			initWidget(l);
			setMessage("Help test - look how it looks like !");
		}
	}

	
	@Override
	public Widget getW() {
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(60);
		vp.add(new LabTip());
		final IGFocusWidget but = ImgButtonFactory.getButton(null,
				"Click on me and look who disables !", "gwt.png");
		final IGFocusWidget but1 = ImgButtonFactory.getButton(null,
				"Click on me and look who disables !", null);
		but.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				but1.setEnabled(!but1.isEnabled());
			}

		});

		but1.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				but.setEnabled(!but.isEnabled());
			}

		});

		vp.add(but.getGWidget());
		vp.add(but1.getGWidget());
		return vp;
	}

}
