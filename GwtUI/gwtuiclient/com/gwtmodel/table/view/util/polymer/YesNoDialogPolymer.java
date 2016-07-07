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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.helper.IStandDialog;
import com.vaadin.polymer.iron.widget.IronIcon;
import com.vaadin.polymer.paper.widget.PaperDialog;

public class YesNoDialogPolymer implements IStandDialog {

	private final String ask;
	private final String title;
	private final IClickYesNo yes;
	private ISignal i;

	public YesNoDialogPolymer(String ask, String title, final IClickYesNo yes) {
		this.ask = ask;
		this.title = title;
		this.yes = yes;
	}

	@Override
	public void show(WSize w) {

		YesNoDialog y = new YesNoDialog();
		PaperDialog p = y.yesdialog;
		PolymerUtil.setTitleMess(y.title, y.mess, title, ask);
		ControlButtonFactory fa = GwtGiniInjector.getI().getControlButtonFactory();
		ListOfControlDesc de = fa.constructAcceptResign();
		for (ControlButtonDesc bu : de.getcList()) {
			switch (bu.getActionId().getClickEnum()) {
			case ACCEPT:
				PolymerUtil.setButtonT(y.okbutton, bu, "done");
				break;
			case RESIGN:
				PolymerUtil.setButtonT(y.cancelbutton, bu, "close");
				break;
			default:
			}
		}
		y.okbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				i.signal();
				yes.click(true);
			}
		});
		y.cancelbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				i.signal();
				yes.click(false);
			}
		});
		i = PolymerUtil.popupPolymer(w, null, p, null,null);
	}

}
