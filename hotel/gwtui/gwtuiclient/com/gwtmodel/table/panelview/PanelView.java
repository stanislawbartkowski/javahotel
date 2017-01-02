/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.panelview;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.common.MaxI;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotCustom;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.view.binder.ICreateBinderWidget;
import com.gwtmodel.table.view.panel.GwtPanelViewFactory;
import com.gwtmodel.table.view.panel.IGwtPanelView;
import com.gwtmodel.table.view.util.CreateFormView;

class PanelView extends AbstractSlotContainer implements IPanelView {

	private class PanelRowCell {

		private final int rowNo, cellNo;
		private final String cellId;

		PanelRowCell(int rowNo, int cellNo, String cellId) {
			this.rowNo = rowNo;
			this.cellNo = cellNo;
			this.cellId = cellId;
		}

		int getRowNo() {
			return rowNo;
		}

		int getCellNo() {
			return cellNo;
		}

		/**
		 * @return the cellId
		 */
		String getCellId() {
			return cellId;
		}
	}

	private interface ICreateBinderWidgetPanel {
		HTMLPanel create(BinderWidget bw);

		GWidget construct(HTMLPanel h, BinderWidget bw);
	}

	private class EarlyBinder implements ICreateBinderWidgetPanel {

		@Override
		public HTMLPanel create(BinderWidget bw) {
			return iBinder.create(bw);
		}

		@Override
		public GWidget construct(HTMLPanel h, BinderWidget bw) {
			return new GWidget(htmlWidget);
		}

	}

	private class DelayedBinder implements ICreateBinderWidgetPanel {

		@Override
		public HTMLPanel create(BinderWidget bw) {
			return iBinder.createEmptyHtmlPanel(bw);
		}

		@Override
		public GWidget construct(HTMLPanel h, BinderWidget bw) {
			return new GWidget(htmlWidget, bw);
		}

	}

	private final ICreateBinderWidgetPanel iB = new EarlyBinder();
//	private final ICreateBinderWidgetPanel iB = new DelayedBinder();

	private final Map<CellId, PanelRowCell> colM = new HashMap<CellId, PanelRowCell>();
	private CellId panelId;
	private IGwtPanelView pView;
	private HTMLPanel htmlWidget;
	private BinderWidget bw;
	private final SlotSignalContextFactory slFactory;
	private final ICreateBinderWidget iBinder;

	PanelView(SlotSignalContextFactory slFactory, ICreateBinderWidget iBinder, IDataType dType, CellId panelId) {
		assert dType != null : LogT.getT().dTypeCannotBeNull();
		this.panelId = panelId;
		this.slFactory = slFactory;
		this.iBinder = iBinder;
		this.dType = dType;
		htmlWidget = null;
		pView = null;
		bw = null;
	}

	@Override
	public CellId addCellPanel(int row, int col) {
		return addCellPanel(null, row, col, null);
	}

	@Override
	public CellId addCellPanel(int row, int col, String cellId) {
		return addCellPanel(null, row, col, cellId);
	}

	@Override
	public CellId addCellPanel(IDataType publishType, int row, int col, String cellId) {
		PanelRowCell pa = new PanelRowCell(row, col, cellId);
		CellId nextId = panelId.constructNext(publishType);
		panelId = nextId;
		colM.put(nextId, pa);
		return nextId;
	}

	@Override
	public CellId addCellPanel(IDataType publishType, int row, int col) {
		// return addCellPanel(null, row, col, null);
		// 2013/01/15 -- replace publishType (null previously)
		return addCellPanel(publishType, row, col, null);
	}

	private class SetWidget implements ISlotListener {

		@Override
		public void signal(ISlotSignalContext slContext) {
			CellId cellId = slContext.getSlType().getCellId();
			assert cellId != null : LogT.getT().CellCannotBeNull();
			PanelRowCell pa = colM.get(cellId);
			// 2012/01/15 introduced to allow several panels
			if (pa == null) {
				return;
			}
			assert pa != null : LogT.getT().CellShouldBeRegistered();
			IGWidget gwtWidget = slContext.getGwtWidget();
			String id = pa.getCellId();
			if (htmlWidget == null) {
				pView.setWidget(pa.rowNo, pa.cellNo, gwtWidget.getGWidget());
			} else {
				if (id != null) {
					CreateFormView.replace(htmlWidget, id, gwtWidget.getGWidget());
				}
			}
			if (id != null) {
				CustomStringSlot cSlot = SendPanelElemSignal.constructSlotSendPanelElem(dType);
				SendPanelElemSignal sl = new SendPanelElemSignal(id, gwtWidget);
				publish(cSlot, sl);
			}
		}
	}

	private class GetMainHtml implements ISlotCallerListener {

		private final BinderWidget bw;

		GetMainHtml(BinderWidget bw) {
			this.bw = bw;
		}

		@Override
		public ISlotSignalContext call(ISlotSignalContext slContext) {
			BinderWidgetSignal s = new BinderWidgetSignal(htmlWidget, bw);
			return slFactory.construct(slContext.getSlType(), s);
		}
	}

	@Override
	public void createView() {
		createView(null, null);
	}

	@Override
	public void createView(String html, BinderWidget b) {
		if (html == null && b == null) {
			int maxR = 0;
			int maxC = 0;
			for (CellId i : colM.keySet()) {
				PanelRowCell ro = colM.get(i);
				maxR = MaxI.max(maxR, ro.getRowNo());
				maxC = MaxI.max(maxC, ro.getCellNo());
			}
			pView = GwtPanelViewFactory.construct(maxR + 1, maxC + 1);
		} else {
			bw = b;
			htmlWidget = html != null ? new HTMLPanel(html) : iB.create(bw);
			ISlotCustom sl = BinderWidgetSignal.constructSlotLineErrorSignal(dType);
			registerCaller(sl, new GetMainHtml(b));
		}

		colM.keySet().forEach(ii -> registerSubscriber(dType, ii, new SetWidget()));

		// create publisher
	}

	@Override
	public void startPublish(CellId cellId) {
		IDataType publishType;
		if (cellId.getdType() == null) {
			publishType = dType;
		} else {
			publishType = cellId.getdType();
		}
		if (htmlWidget != null) {
			publish(publishType, cellId, iB.construct(htmlWidget, bw));
		} else if (pView != null)
			publish(publishType, cellId, pView);
	}

}
