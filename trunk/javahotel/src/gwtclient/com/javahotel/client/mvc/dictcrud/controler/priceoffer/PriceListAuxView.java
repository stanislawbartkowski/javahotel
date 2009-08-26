/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.dictcrud.controler.priceoffer;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.checktable.view.CheckTableViewFactory;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.dictcrud.controler.IBeforeViewSignal;
import com.javahotel.client.mvc.dictcrud.controler.IModifRecordDef;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;
import com.javahotel.client.mvc.seasonprice.model.ISeasonPriceModel;
import com.javahotel.client.mvc.seasonprice.model.ISpecialMap;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.client.mvc.util.GetFieldModif;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.util.StringU;
import java.util.ArrayList;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class PriceListAuxView extends AbstractAuxRecordPanel {

	private final IResLocator rI;
	private final IDecimalTableView tView;
	private final GetSeasonSpecial sS;
	private final GetFieldModif fModif;
	private boolean valStarted;
	private SyncC sync;
	private final static int HWIDTH = 30;

	public PriceListAuxView(IResLocator rI) {
		this.rI = rI;
		tView = CheckTableViewFactory.getDevView(rI);
		sS = new GetSeasonSpecial(rI);
		fModif = new GetFieldModif(OfferPriceP.F.season);
		fModif.setChangeL(new ChangeL());
	}

	public Object getAuxO() {
		ISeasonPriceModel iM = new ISeasonPriceModel() {

			public IDecimalTableView getT() {
				return tView;
			}

			public void setSpecial(String sName, ISpecialMap ma) {
				sS.runSpecial(sName, ma);
			}
		};
		return iM;
	}

	public IMvcWidget getMWidget() {
		return tView.getMWidget();
	}

	private class SyncC extends SynchronizeList {

		private OfferPriceP oP;
		private String sName;

		SyncC() {
			super(2);
			oP = null;
			sName = null;
		}

		@Override
		protected void doTask() {
			valStarted = true;
			if (StringU.isEmpty(oP.getSeason())) {
				return;
			}
			if (!oP.getSeason().equals(sName)) {
				return;
			}
			SetTableVal.setVal(rI, sS, tView, oP);
		}

		/**
		 * @param oP
		 *            the oP to set
		 */
		void setOP(OfferPriceP oP) {
			this.oP = oP;
		}

		/**
		 * @param sName
		 *            the sName to set
		 */
		void setSName(String sName) {
			this.sName = sName;
		}
	}

	private class SetC implements ISpecialMap {

		private final StorePrices sto;
		private final String sName;

		SetC(StorePrices sto, String sName) {
			this.sto = sto;
			this.sName = sName;
		}

		public void set(ArrayList<MapSpecialToI> col) {
			ArrayList<ColsHeader> co = new ArrayList<ColsHeader>();
			setTitle(co);
			for (MapSpecialToI m : col) {
				co.add(new ColsHeader(m.getName(), HWIDTH));
			}
			tView.setCols(new ColsHeader("Usługi", 70), co);
			sync.setSName(sName);
			sync.signalDone();
			if (sto != null) {
				sto.restore();
			}
		}
	}

	private void setSeason(String season, StorePrices sto) {
		ISpecialMap i = new SetC(sto, season);
		sS.runSpecial(season, i);
	}

	private class ChangeL implements IChangeListener {

		public void onChange(ILineField arg0) {
			ILineField season = (ILineField) arg0;
			String s = season.getVal();
			if (StringU.isEmpty(s)) {
				return;
			}
			StorePrices sto = null;
			if (valStarted) {
				sto = new StorePrices(tView);
			}
			setSeason(s, sto);
		}
	}

	public IModifRecordDef getModif() {
		return fModif.getModif();
	}

	public IBeforeViewSignal getBSignal() {
		return new BSignal();
	}

	private class R implements RData.IVectorList {

		public void doVList(final ArrayList<? extends AbstractTo> val) {

			ArrayList<String> rows = new ArrayList<String>();
			for (AbstractTo a : val) {
				DictionaryP d = (DictionaryP) a;
				rows.add(d.getName());
			}
			tView.setRows(rows);
		}
	}

	private void setTitle(ArrayList<ColsHeader> cols) {
		for (int i = 0; i <= ISeasonPriceModel.MAXSPECIALNO; i++) {
			cols.add(new ColsHeader("", 0));
		}
		cols.set(ISeasonPriceModel.HIGHSEASON, new ColsHeader("W sezonie",
				HWIDTH));
		cols.set(ISeasonPriceModel.HIGHSEASONWEEKEND, new ColsHeader(
				"W sezonie weekend", HWIDTH));
		cols.set(ISeasonPriceModel.LOWSEASON, new ColsHeader("Poza sezonem",
				HWIDTH));
		cols.set(ISeasonPriceModel.LOWSEASONWEEKEND, new ColsHeader(
				"Poza sezonem weekend", HWIDTH));
	}

	private class BSignal implements IBeforeViewSignal {

		public void signal(RecordModel mo) {
			valStarted = false;
			sync = new SyncC();
			sS.initSeason();
			tView.reset();
		}
	}

	@Override
	public void setFields(RecordModel mo) {
		OfferPriceP oP = (OfferPriceP) mo.getA();

		CommandParam p1 = rI.getR().getHotelCommandParam();
		p1.setDict(DictType.ServiceDict);
		rI.getR().getList(RType.ListDict, p1, new R());
		sync.setOP(oP);
		sync.signalDone();
	}

	@Override
	public void changeMode(int actionMode) {
		if (actionMode == IPersistAction.DELACTION) {
			tView.setEnable(false);
		} else {
			tView.setEnable(true);
		}
	}
}
