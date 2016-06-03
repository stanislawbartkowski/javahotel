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

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.vaadin.polymer.iron.widget.IronDropdown;
import com.vaadin.polymer.iron.widget.IronIcon;
import com.vaadin.polymer.iron.widget.event.IronOverlayClosedEvent;
import com.vaadin.polymer.iron.widget.event.IronOverlayClosedEventHandler;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperDialog;

public class PolymerUtil {

	private PolymerUtil() {

	}

	public static PaperButton construct(String butt, String icon) {
		PaperButton b = new PaperButton(butt);
		if (!CUtil.EmptyS(icon)) {
			IronIcon i = new IronIcon();
			i.setIcon(icon);
			b.add(i);
		}
		return b;
	}

	public static ISignal popupPolymer(WSize ws, Widget w, PaperDialog pap) {
		PopupPanel pa = new PopupPanel();
		IronDropdown p = new IronDropdown("<div class=\"dropdown-content\" id=\"content\"></div>");
		p.setHorizontalOffset(ws.getLeft());
		p.setVerticalOffset(ws.getTop());
		p.add(w, "content");
		pa.setWidget(p);
		pa.setVisible(false);
		pa.show();
		p.open();
		if (pa != null) {
			pap.getElement().getStyle().setProperty("top", "" + (ws.getTop() + 10));
			pap.getElement().getStyle().setProperty("left", "" + (ws.getLeft() + 10));
			pap.open();
		}
		p.getElement().getStyle().setProperty("visibility", "visible");
		p.addIronOverlayClosedHandler(new IronOverlayClosedEventHandler() {

			@Override
			public void onIronOverlayClosed(IronOverlayClosedEvent event) {
				pa.hide();
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
