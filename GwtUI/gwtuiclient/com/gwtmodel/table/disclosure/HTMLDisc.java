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
package com.gwtmodel.table.disclosure;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.controlbuttonview.GetButtons;
import com.gwtmodel.table.editw.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotCustom;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.util.CreateFormView;

class HTMLDisc extends AbstractSlotContainer implements ISlotable {

	private final String html;
	private final String header;
	private final IGetStandardMessage iMess;
	private final IDataType publishType;

	HTMLDisc(IDataType publishType, IDataType dType, String header, String html, IGetStandardMessage iMess) {
		this.dType = dType;
		this.html = html;
		this.header = (header == null) ? "" : header;
		this.iMess = iMess;
		this.publishType = publishType;
	}

	private void publishevent(boolean open) {
		DisclosureEvent e = new DisclosureEvent(open);
		publish(DisclosureEvent.constructSlot(dType), e);
	}

	private class OpenH implements OpenHandler<DisclosurePanel> {

		@Override
		public void onOpen(OpenEvent<DisclosurePanel> event) {
			publishevent(true);
		}

	}

	private class CloseH implements CloseHandler<DisclosurePanel> {

		@Override
		public void onClose(CloseEvent<DisclosurePanel> event) {
			publishevent(false);
		}

	}

	@Override
	public void startPublish(CellId cellId) {
		// HTMLPanel pa = new HTMLPanel(html);
		HTMLPanel pa = new HTMLPanel(html);
		DisclosurePanel di = new DisclosurePanel(iMess.getMessage(header));
		di.addOpenHandler(new OpenH());
		di.addCloseHandler(new CloseH());
		di.setContent(pa);
		FormLineContainer fC = SlU.getFormLineContainer(publishType, this);
		if (fC != null)
			CreateFormView.setHtml(pa, fC.getfList(), null);
		ISlotCustom sl = GetButtons.constructSlot(publishType);
		ISlotSignalContext i = getSlContainer().getGetterCustom(sl);
		if (i != null) {
			GetButtons g = (GetButtons) i.getCustom();
			CreateFormView.IGetButtons iG = g.getValue();
			CreateFormView.setHtml(pa, iG, null);
		}
		publish(publishType, cellId, new GWidget(di));
	}

}
