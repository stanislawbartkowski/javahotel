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
package com.gwtmodel.table.controlbuttonview;

import java.util.List;

import javax.inject.Inject;

import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.mm.MM;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.stackpanelcontroller.IStackPanelController;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.controlpanel.polymer.PolymerMenuButton;

/**
 * 
 * @author perseus
 */
public class StackPanelButtonFactory {

	private final IContrButtonViewFactory vFactory;
	private final SlotSignalContextFactory slFactory;

	@Inject
	public StackPanelButtonFactory(IContrButtonViewFactory vFactory, SlotSignalContextFactory slFactory) {
		this.vFactory = vFactory;
		this.slFactory = slFactory;
	}

	private class ControlButtonDescPanel extends ControlButtonView {

		private final String html;

		ControlButtonDescPanel(IContrButtonViewFactory vFactory, ListOfControlDesc listButton, IDataType dType,
				String html) {
			super(vFactory, listButton, dType, false, false);
			this.html = html;
		}

		private class GetGwt implements ISlotCallerListener {

			private final IGWidget wg;

			GetGwt(IGWidget wg) {
				this.wg = wg;
			}

			public ISlotSignalContext call(ISlotSignalContext slContext) {
				return slFactory.construct(slContext.getSlType(), wg);
			}
		}

		@Override
		public void startPublish(CellId cellId) {
			SlotType sl = slTypeFactory.constructH(cellId);
			IGWidget gw = new GWidget(html);

			getSlContainer().registerCaller(sl, new GetGwt(gw));
			publish(dType, cellId, gw);
			super.startPublish(cellId);
		}
	}

	public IStackPanelController construct(IDataType dType, List<ControlButtonDesc> bList, String html) {
		ListOfControlDesc listButton = new ListOfControlDesc(bList);
		if (MM.isPolymer()) {
			return new ControlButtonView(new IContrButtonViewFactory() {

				@Override
				public IContrButtonView getView(ListOfControlDesc model, IControlClick co, boolean hori,
						boolean polymer) {
					return new PolymerMenuButton(model, co);
				}
			}, listButton, dType, false, false);
		}
		if (html == null) {
			return new ControlButtonView(vFactory, listButton, dType, false, false);
		} else {
			return new ControlButtonDescPanel(vFactory, listButton, dType, html);
		}
	}
}
