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
package com.jythonui.client.dialog.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.client.M;
import com.polymerui.client.binder.ICreateBinderWidget;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.eventbus.StandardDialogEvent;
import com.polymerui.client.eventbus.StandardDialogResult;
import com.polymerui.client.view.panel.IMainPanel;
import com.polymerui.client.view.util.PolymerUtil;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperDialog;

public class StandardDialog {

	private StandardDialog() {

	}

	@Inject
	private static ICreateBinderWidget iBinder;

	static final int OKDIALOG = 1;
	static final int YESNODIALOG = 2;

	static void dialog(IEventBus iBus, int a, String[] pars) {
		HTMLPanel ha = iBinder.create(M.getStandw()[a]);
		PaperDialog p = (PaperDialog) PolymerUtil.findandverifyWidget(M.getBinders()[a], ha, "dialog",
				PaperDialog.class);
		Label header = (Label) PolymerUtil.findandverifyWidget(M.getBinders()[a], ha, "header", Label.class);
		String[] butt = null;
		switch (a) {
		case OKDIALOG:
			butt = new String[] { "ok" };
			break;
		case YESNODIALOG:
			butt = new String[] { "yes", "no" };
			break;
		}

		for (String b : butt) {
			PaperButton ok = (PaperButton) PolymerUtil.findandverifyWidget(M.getBinders()[a], ha, b, PaperButton.class);
			ok.getPolymerElement().addEventListener("click", event -> {
				if (!CUtil.EmptyS(pars[2]))
					iBus.publish(new StandardDialogEvent(), new StandardDialogResult(pars[2], b.equals("yes")));
				p.close();
			});

		}

		if (!CUtil.EmptyS(pars[1]))
			header.setText(pars[1]);
		Element e = ha.getElementById("content");
		assert e != null;
		e.removeAllChildren();
		HTML html = new HTML(pars[0]);
		e.insertFirst(html.getElement());
		IMainPanel iM = M.getPanel();
		HTMLPanel hM = iM.getCurrentContent().getH();
		hM.add(ha);
		p.open();
		p.addIronOverlayClosedHandler(event -> {
			hM.remove(ha);
		});
	}

}
