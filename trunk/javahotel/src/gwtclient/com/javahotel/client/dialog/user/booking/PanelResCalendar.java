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
package com.javahotel.client.dialog.user.booking;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Grid;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.user.tableseason.PanelSeason;
import com.javahotel.client.widgets.stable.ScrollTable;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.toobject.OfferSeasonP;
import java.util.List;
import java.util.Date;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PanelResCalendar {

	@SuppressWarnings("unused")
	private final IResLocator rI;
	private PanelSeason pS;

	PanelResCalendar(final IResLocator rI, final Grid g,
			final ComplexPanel controlP, final int startC,
			final ScrollTable.DrawPartI drawI) {
		this.rI = rI;
		pS = new PanelSeason(rI, g, controlP, startC, drawI);
	}

	public void draPa(final OfferSeasonP oP, Date today) {
		pS.drawPa(oP, PeriodType.byDay, today);
	}

	List<Date> getDLine() {
		return pS.getDLine();
	}

	int getStartNo() {
		return pS.getStartNo();
	}

	int getColNo() {
		return pS.getColNumber();
	}

	PanelSeason getPPSeason() {
		return pS;
	}
}
