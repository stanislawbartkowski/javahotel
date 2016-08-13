/*
 * Copyr ight 2016 stanislawbartkowski@gmail.com 
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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.vaadin.polymer.iron.widget.IronDropdown;
import com.vaadin.polymer.iron.widget.IronIcon;
import com.vaadin.polymer.iron.widget.event.IronOverlayClosedEvent;
import com.vaadin.polymer.iron.widget.event.IronOverlayClosedEventHandler;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperDialog;

public class PolymerUtil {

	private PolymerUtil() {

	}
	
	@Inject
	private static IGetStandardMessage iMess;
  
	public static String convert(String s) {
		StringBuffer b = new StringBuffer(s);
		while (true) {
			int i = b.indexOf(IConsts.RESBEG);
			if (i == -1)
				break;
			int k = b.indexOf(IConsts.RESEND, i);
			if (k == -1)
				break;
			String res = b.substring(i + 1, k);
			// together with $
			String v = iMess.getMessage(res);
			b.replace(i, k + 2, v);
		}
		return b.toString();
	}

	static void setTitleMess(Label ltitle, Label lmess, String title, String mess) {
		if (!CUtil.EmptyS(title))
			ltitle.setText(iMess.getMessage(title));
		lmess.setText(iMess.getMessage(mess));
	}

	static void setButtonT(PaperButton b, ControlButtonDesc bu, String icon) {
		Utils.setInnerText(b, bu.getDisplayName());
		PolymerUtil.addIcon(b, icon);
	}

	public static void addIcon(PaperButton b, String icon) {
		if (!CUtil.EmptyS(icon)) {
			IronIcon i = new IronIcon();
			i.setIcon(icon);
			b.add(i);
		}
	}

	public static PaperButton construct(String butt, String icon) {
		PaperButton b = new PaperButton(butt);
		addIcon(b, icon);
		return b;
	}

	public static ISignal popupPolymer(WSize ws, Widget w, PaperDialog pap, final ISignal sClose,
			String addStyleNames) {
		final PopupPanel pa = new PopupPanel();
		if (addStyleNames == null)
			addStyleNames = "";
		IronDropdown p = new IronDropdown(
				"<div class=\"dropdown-content " + addStyleNames + "\" id=\"content\"></div>");
		p.setHorizontalOffset(ws.getLeft());
		p.setVerticalOffset(ws.getTop());
		if (w != null)
			p.add(w, "content");
		else
			p.add(pap, "content");
		pa.setWidget(p);
		pa.setVisible(false);
		pa.show();
		p.open();
		if (pap != null) {
			Utils.setTopLeftProperty(pap, ws.getTop() + 10, ws.getLeft() + 10);
			pap.open();
		}
		Utils.setVisibleProperty(p);
		p.addIronOverlayClosedHandler(new IronOverlayClosedEventHandler() {

			@Override
			public void onIronOverlayClosed(IronOverlayClosedEvent event) {
				pa.hide();
				if (sClose != null)
					sClose.signal();
			}
		});
		return new ISignal() {

			@Override
			public void signal() {
				pa.hide();
			}
		};
	}

}
