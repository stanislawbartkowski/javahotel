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
package com.gwtmodel.table;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DateFormat;
import com.gwtmodel.table.common.PeriodT;

/**
 * Utilities related to date methods
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DateUtil {

	private DateUtil() {
	}

	private static final long DAY_IN_MILISECONDS = 86400000;

	/**
	 * Change to next day
	 * 
	 * @param d
	 *            Date input and output (next day)
	 */
	public static Date NextDayD(final Date di) {
		Date d = copyDate(di);
		CalendarUtil.addDaysToDate(d, 1);
		return d;
	}

	/**
	 * Change to previous day
	 * 
	 * @param d
	 *            Data input and output (previous day)
	 */
	public static Date PrevDayD(final Date di) {
		Date d = copyDate(di);
		CalendarUtil.addDaysToDate(d, -1);
		return d;
	}

	/**
	 * Make a copy of Data class
	 * 
	 * @param d
	 *            Date (source)
	 * @return Date copy of source
	 */
	private static Date copyDate(final Date d) {
		return CalendarUtil.copyDate(d);
	}

	/**
	 * Test if two dates (day) are equal
	 * 
	 * @param d1
	 *            First to compare
	 * @param d2
	 *            Second to compare
	 * @return true: if equal
	 */
	public static boolean eqDate(final Date d1, final Date d2) {
		return compareDate(d1, d2) == 0;
	}

	private static int dCompare(int y1, int y2, int m1, int m2, int dd1, int dd2) {
		if (y1 != y2) {
			if (y1 < y2) {
				return -1;
			}
			return 1;
		}
		if (m1 != m2) {
			if (m1 < m2) {
				return -1;
			}
			return 1;
		}
		if (dd1 != dd2) {
			if (dd1 < dd2) {
				return -1;
			}
			return 1;
		}
		return 0;
	}

	/**
	 * Test if two dates are equal and also check for nul
	 * 
	 * @param d1
	 *            First to compare (can be null)
	 * @param d2
	 *            Second to compare (can be null)
	 * @return True : if equals or both null
	 */
	public static boolean eqNDate(final Date d1, final Date d2) {
		if (d1 == null) {
			if (d2 != null) {
				return false;
			}
		} else {
			if (d2 == null) {
				return false;
			}
		}
		return eqDate(d1, d2);
	}

	@SuppressWarnings("deprecation")
	public static int compareTimestamp(final Timestamp d1, final Timestamp d2) {
		int res = compareDate(d1, d2);
		if (res != 0) {
			return res;
		}
		return dCompare(d1.getHours(), d2.getHours(), d1.getMinutes(), d2.getMinutes(), d1.getSeconds(),
				d2.getSeconds());
	}

	/**
	 * Compare two dates (day)
	 * 
	 * @param d1
	 *            First day
	 * @param d2
	 *            Second day
	 * @return -1 : first earlier then second 0 : equals 1 : first greater then
	 *         second
	 */

	@SuppressWarnings("deprecation")
	public static int compareDate(final Date d1, final Date d2) {
		return dCompare(d1.getYear(), d2.getYear(), d1.getMonth(), d2.getMonth(), d1.getDate(), d2.getDate());
	}

	/**
	 * Check if day is inside period (iclusive)
	 * 
	 * @param d
	 *            Date to test
	 * @param pFrom
	 *            Beginning of period
	 * @param pTo
	 *            End of period
	 * @return -1: before, 0: inside, 1 : after
	 */
	public static int comparePeriod(final Date d, final Date pFrom, final Date pTo) {
		int c1 = compareDate(d, pFrom);
		if (c1 == -1) {
			return -1;
		}
		int c2 = compareDate(d, pTo);
		if (c2 == 1) {
			return 1;
		}
		return 0;
	}

	public static Date getMinMaxDate(final Date d1, Date d2, int co) {
		if (compareDate(d1, d2) == co) {
			return copyDate(d1);
		}
		return copyDate(d2);
	}

	public static Date getMinDate(final Date d1, Date d2) {
		return getMinMaxDate(d1, d2, -1);
	}

	public static Date getMaxDate(final Date d1, Date d2) {
		return getMinMaxDate(d1, d2, 1);
	}

	public static int noDays(final Date from, Date to) {
		return CalendarUtil.getDaysBetween(from, to);
	}

	public static int noLodgings(final Date from, Date to) {
		return noDays(from, to) + 1;
	}

	public static int weekDay(Date d) {
		return d.getDay();
	}

	public static int compPeriod(final Date d, final PeriodT pe) {
		if (pe.getFrom() != null) {
			if (compareDate(d, pe.getFrom()) == -1) {
				return -1;
			}
		}
		if (pe.getTo() != null) {
			if (compareDate(d, pe.getTo()) == 1) {
				return 1;
			}
		}
		return 0;
	}

	public static Date addDaysD(final Date di, int noD) {
		Date d = copyDate(di);
		CalendarUtil.addDaysToDate(d, noD);
		return d;
	}

	public static PeriodT getWider(final PeriodT pe, final PeriodT pe1) {
		Date d1 = pe.getFrom();
		if ((d1 == null) || (compareDate(pe1.getFrom(), d1) == -1)) {
			d1 = copyDate(pe1.getFrom());
		}
		Date d2 = pe.getTo();
		if ((d2 == null) || (compareDate(d2, pe1.getTo()) == -1)) {
			d2 = copyDate(pe1.getTo());
		}
		return new PeriodT(d1, d2);
	}

	public static boolean isOkDate(int y, int m, int d) {
		if (d == 29 && m == 2) {
			if (y % 100 == 0)
				return false;
			if (y % 4 == 0)
				return true;
			return false;
		}
		if (d <= 30)
			return true;
		if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12)
			return true;
		return false;
	}

	public static Date getToday() {
		return new Date();
	}

	public static Date toLastMonthDay(int y, int m) {
		Date dd = DateFormat.toD(y, m, 1);
		while (DateFormat.getM(dd) == m) {
			dd = NextDayD(dd);
		}
		return PrevDayD(dd);
	}

	private static final String[] months;
	private static final String[] polmonths = { "Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec",
			"Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień" };
	static {
		if (CUtil.EqNS("pl", Utils.getLocale())) {
			months = polmonths;
		} else {
			months = LocaleInfo.getCurrentLocale().getDateTimeFormatInfo().monthsFull();
		}
	}

	public static String[] getMonths() {
		return months;
	}

}
