/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.daytimetable.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwtmodel.table.DateUtil;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;

class DatePanelUtil {

	private DatePanelUtil() {
	}

	static class PanelDesc {
		private Date vfirstD;
		private int vpSize;
		private int vcurD;

		Date getfirstD() {
			return vfirstD;
		}

		void setfirstD(Date vfirstD) {
			this.vfirstD = vfirstD;
		}

		int getpSize() {
			return vpSize;
		}

		void setpSize(int vpSize) {
			this.vpSize = vpSize;
		}

		int getcurD() {
			return vcurD;
		}

		void setcurD(int vcurD) {
			this.vcurD = vcurD;
		}

		Date getCurDay() {
			return getPanelDay(this, vcurD);
		}

		Date getMonthI(int i) {
			return getPanelMonth(this, i);
		}

	}

	private interface IOperDate {
		Date nextD(Date d);

		Date prevD(Date d);

		boolean eqD(Date d1, Date d2);

		Date getBeg(Date d);
	}

	static private PanelDesc constructPeriod(Date firstD, Date lastD, final Date currD, int bSize, IOperDate o,
			int curPos) {

		PanelDesc pa = new PanelDesc();
		int middleD = bSize / 2;
		if (curPos != -1) {
			float f = (float) bSize * ((float) curPos / (float) 100);
			middleD = (int) f;
		}

		int no = middleD;
		Date cursD = new Date(currD.getTime());
		while (no > 0) {
			if (o.eqD(cursD, firstD)) {
				no = 0;
			}
			cursD = o.prevD(cursD);
			no--;
		}
		pa.setfirstD(new Date(cursD.getTime()));
		while (no < bSize) {
			if (o.eqD(cursD, currD))
				pa.setcurD(no);
			if (o.eqD(cursD, lastD))
				break;
			no++;
			cursD = o.nextD(cursD);
		}
		pa.setpSize(no);
		return pa;
	}

	private static class MonthO implements IOperDate {

		@Override
		public Date nextD(final Date d) {
			Date da = new Date(d.getTime());
			da.setDate(1);
			da = DateUtil.NextDayD(da);
			while (DateFormatUtil.getD(da) != 1)
				da = DateUtil.NextDayD(da);
			return da;
		}

		@Override
		public Date prevD(Date d) {
			Date da = new Date(d.getTime());
			da.setDate(1);
			da = DateUtil.PrevDayD(da);
			while (DateFormatUtil.getD(da) != 1)
				da = DateUtil.PrevDayD(da);
			return da;
		}

		@Override
		public boolean eqD(Date d1, Date d2) {
			if (DateFormatUtil.getY(d1) != DateFormatUtil.getY(d2))
				return false;
			if (DateFormatUtil.getM(d1) != DateFormatUtil.getM(d2))
				return false;
			return true;
		}

		@Override
		public Date getBeg(Date d) {
			int y = DateFormatUtil.getY(d);
			int dt = DateFormatUtil.getD(d);
			return DateFormatUtil.toD(y, 1, dt);
		}
	}

	private static class DayO implements IOperDate {

		@Override
		public Date nextD(Date d) {
			return DateUtil.NextDayD(d);
		}

		@Override
		public Date prevD(Date d) {
			return DateUtil.PrevDayD(d);
		}

		@Override
		public boolean eqD(Date d1, Date d2) {
			return DateUtil.eqDate(d1, d2);
		}

		@Override
		public Date getBeg(Date d) {
			return new Date(d.getTime());
		}

	}

	static PanelDesc createLMonth(Date firstD, Date lastD, Date currD, int noM) {
		return constructPeriod(firstD, lastD, currD, noM, new MonthO(), -1);
	}

	static PanelDesc createLDays(Date firstD, Date lastD, Date currD, int noM, int curPos) {
		return constructPeriod(firstD, lastD, currD, noM, new DayO(), curPos);
	}

	private static Date getPanelDate(PanelDesc pa, int no, IOperDate o) {
		// Date da = new Date(pa.getfirstD().getTime());
		Date da = o.getBeg(pa.getfirstD());
		while (no > 0) {
			da = o.nextD(da);
			no--;
		}
		return da;
	}

	static Date getPanelMonth(PanelDesc pa, int no) {
		return getPanelDate(pa, no, new MonthO());
	}

	static Date getPanelDay(PanelDesc pa, int no) {
		return getPanelDate(pa, no, new DayO());
	}

	static List<Integer> getListOfYears(Date firstD, Date lastD) {
		List<Integer> yList = new ArrayList<Integer>();
		int y1 = DateFormatUtil.getY(firstD);
		int y2 = DateFormatUtil.getY(lastD);
		for (int i = y1; i <= y2; i++)
			yList.add(i);
		return yList;
	}

	static boolean eqMonth(Date d1, Date d2) {
		return new MonthO().eqD(d1, d2);
	}

}
