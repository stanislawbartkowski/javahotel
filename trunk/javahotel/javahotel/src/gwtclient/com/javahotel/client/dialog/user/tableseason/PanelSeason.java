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
package com.javahotel.client.dialog.user.tableseason;

import java.util.Collection;
import java.util.Date;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.WidgetSizeFactory;
import com.javahotel.client.idialog.GetIEditFactory;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.widgets.popup.PopUpWithClose;
import com.javahotel.client.widgets.solid.SolidColor;
import com.javahotel.client.widgets.stable.ScrollTable;
import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.GetPeriodsTemplate;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.dateutil.GetPeriods.StartWeek;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;
/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
public class PanelSeason {

	private final ComplexPanel controlP;
	private final IResLocator rI;
	private final ILineField ePeriod;
	private final ScrollTable sCr;
	private OfferSeasonP oP;
	private Date d1;
	private Date d2;
	private Collection<Date> dLine;
	private Collection<PeriodT> coP;
	private final Grid g;
	private final int startC;
	private final ScrollTable.DrawPartI drawI;

	@SuppressWarnings("unchecked")
	public PanelSeason(final IResLocator rI, final Grid g,
			final ComplexPanel controlP, final int startC,
			final ScrollTable.DrawPartI drawI) {
		this.rI = rI;
		this.g = g;
		this.startC = startC;
		this.controlP = controlP;
		this.drawI = drawI;
		ePeriod = GetIEditFactory.getEnumMap(rI, rI.getLabels().PeriodType());
		sCr = new ScrollTable(rI, new DrawC(), 12);
	}

	private class DrawC implements ScrollTable.DrawPartI {

		public void draw(final int sno, final int sto) {
			drawD(sno, sto);
			if (drawI != null) {
				drawI.draw(sno, sto);
			}
		}

		public void drawagain(final int l, final int lno, final int cno,
				final boolean setC) {
			if (drawI != null) {
				drawI.drawagain(l, lno, cno, setC);
			}
		}

		public void setSWidget(final Widget w) {
			if (controlP.getWidgetCount() > 1) {
				controlP.remove(1);
			}
			controlP.add(w);
		}
	}

	private String getName(final PeriodT pe) {
		Object o = pe.getI();
		OfferSeasonPeriodP pp = (OfferSeasonPeriodP) o;
		SeasonPeriodT t = null;
		if (pp != null) {
			t = pp.getPeriodT();
		}
		String s = SeasonNames.getName(rI, t);
		return s;
	}

	private class SeasonDayInfo extends Composite {

		private final VerticalPanel vp = new VerticalPanel();

		private SeasonDayInfo(final Date da) {
			PeriodT p = GetPeriods.findPeriod(da, coP);
			assert p != null : "Must find period";
			String na = getName(p);
			vp.add(new Label(na));
			initWidget(vp);
		}
	}

	public Widget getDInfo(final Date d) {
		return new SeasonDayInfo(d);
	}

	private class IClick implements ClickListener {

		private final PeriodT pe;

		IClick(PeriodT pe) {
			this.pe = pe;
		}

		public void onClick(final Widget arg0) {
			PopUpWithClose aPanel = new PopUpWithClose(WidgetSizeFactory
					.getW(arg0));
			VerticalPanel h = aPanel.getVP();
			String s = getName(pe);
			h.add(new Label(s));
			String s1 = DateFormatUtil.toS(pe.getFrom());
			String s2 = DateFormatUtil.toS(pe.getTo());
			h.add(new Label(s1 + " - " + s2));
			int noD = DateUtil.noLodgings(pe.getFrom(), pe.getTo());
			h.add(new Label(rI.getMessages().noSleeps(noD)));
		}
	}

	private class DrawDC extends GetPeriodsTemplate {

		private VerticalPanel hP;
		private HorizontalPanel pH;
		private int co;

		DrawDC(final Date d1, final Collection<Date> dLine,
				final Collection<PeriodT> coP) {
			super(d1, dLine, coP);
			co = 0;
		}

		@Override
		protected int startF(final Date dd) {
			hP = new VerticalPanel();
			pH = new HorizontalPanel();
			String s = DateFormatUtil.toS(dd);
			hP.add(new Label(s));
			g.setWidget(0, startC + co, hP);
			int no = hP.getOffsetWidth();
			return no;
		}

		@Override
		protected void addElem(final PeriodT pr, final int wi) {
			String color = "#0000FF";
			if (pr.getI() != null) {
				Object o = pr.getI();
				OfferSeasonPeriodP pp = (OfferSeasonPeriodP) o;
				switch (pp.getPeriodT()) {
				case LOW:
					color = "#00FF00";
					break;
				case SPECIAL:
					color = "#FF0000";
					break;
				case LOWWEEKEND:
					color = "#00FFF0";
					break;
				case HIGHWEEKEND:
					color = "#00FFFF";
					break;
				default:
					break;
				}
			}
			SolidColor d = new SolidColor(color, wi, -1);
			d.addClickListener(new IClick(pr));
			pH.add(d);
		}

		@Override
		protected void endF() {
			hP.add(pH);
			co++;
		}
	}

	private void drawD(final int startno, final int endno) {
		g.resizeColumns(startC + endno - startno + 1);
		DrawDC d = new DrawDC(d1, dLine, coP);
		d.drawD(startno, endno);
	}

	private void drawCa(final PeriodType pType,int actC) {
		dLine = CalendarTable.listOfDates(d1, d2, pType);
		coP = CreateTableSeason.createTable(oP, StartWeek.onFriday);
		sCr.createVPanel(dLine.size(),actC);
	}

	private class PChange implements IChangeListener {

		public void onChange(final ILineField arg0) {
			String val = ePeriod.getVal();
			PeriodType pT = PeriodType.valueOf(val);
			drawCa(pT,-1);
		}
	}

	private void createControl(final ComplexPanel cP, final PeriodType pT) {
		cP.add(ePeriod.getMWidget().getWidget());
		ePeriod.setVal(pT.name());
		ePeriod.setChangeListener(new PChange());
	}
	
	private int countA(final Date t) {
		if (t == null) { return -1; }
		int n = DateUtil.noDays(d1,t);
		return n;
	}

	public void drawPa(final OfferSeasonP oP, final PeriodType t,
			final Date today) {
		this.oP = oP;
		controlP.clear();
		createControl(controlP, t);
		d1 = oP.getStartP();
		d2 = oP.getEndP();
		drawCa(t,countA(today));
	}

	public Collection<Date> getDLine() {
		return dLine;
	}

	public int getStartNo() {
		return sCr.getStartNo();
	}

	public int getColNumber() {
		return g.getColumnCount() - startC;
	}
}
