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
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.helper.IStandDialog;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperDialog;

public class OkDialogPolymer implements IStandDialog {

	private final String mess;
	private final String title;
	private ISignal i;

	public OkDialogPolymer(String mess, String title) {
		this.mess = mess;
		this.title = title;
	}

	private PaperDialog construct(String mess, String title) {
		OkDialog o = new OkDialog();
		PolymerUtil.setTitleMess(o.title, o.mess, title, mess);
		ControlButtonFactory fa = GwtGiniInjector.getI().getControlButtonFactory();
		ListOfControlDesc de = fa.constructOkButton();
		PolymerUtil.setButtonT(o.okbutton, de.getcList().get(0), "done");
		o.okbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				i.signal();

			}
		});
		// not modal or modal
		// p.setModal(true);
		return o.okdialog;
	}

	@Override
	public void show(WSize w) {
		PaperDialog p = construct(mess, title);
		// p.setAttributes("horizontalOffset:10px;");
		// p.open();
		i = PolymerUtil.popupPolymer(w, null, p, null, null);
	}

}
