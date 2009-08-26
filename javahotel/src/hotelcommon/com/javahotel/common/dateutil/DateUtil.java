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
package com.javahotel.common.dateutil;

import java.util.Date;

import com.javahotel.types.DateP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DateUtil {

	private DateUtil() {
	}

	private static final long DAY_IN_MILISECONDS = 86400000;

	public static void NextDay(final Date d) {
		long ti = d.getTime();
		ti += DAY_IN_MILISECONDS;
		d.setTime(ti);
	}

	public static void PrevDay(final Date d) {
		long ti = d.getTime();
		ti -= DAY_IN_MILISECONDS;
		d.setTime(ti);
	}

	public static Date copyDate(final Date d) {
		return new Date(d.getTime());
	}

	public static boolean eqDate(final Date d1, final Date d2) {
		return compareDate(d1, d2) == 0;
	}

	public static boolean eqNDate(final Date d1, final Date d2) {
		if (d1 == null) {
			if (d2 != null) {
				return false;
			}
		} else {
			if (d1 == null) {
				return false;
			}
		}
		return eqDate(d1, d2);
	}

	@SuppressWarnings("deprecation")
	public static int compareDate(final Date d1, final Date d2) {
		int y1 = d1.getYear();
		int y2 = d2.getYear();
		if (y1 != y2) {
			if (y1 < y2) {
				return -1;
			}
			return 1;
		}
		int m1 = d1.getMonth();
		int m2 = d2.getMonth();
		if (m1 != m2) {
			if (m1 < m2) {
				return -1;
			}
			return 1;
		}
		int dd1 = d1.getDate();
		int dd2 = d2.getDate();
		if (dd1 != dd2) {
			if (dd1 < dd2) {
				return -1;
			}
			return 1;
		}
		return 0;
	}

	public static int comparePeriod(final Date d, final Date pFrom,
			final Date pTo) {
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
		long dif = to.getTime() - from.getTime();
		long l = dif / DAY_IN_MILISECONDS;
		return (int) l;
	}

	public static int noLodgings(final Date from, Date to) {
		return noDays(from, to) + 1;
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

	private static Date testToday = null;

	public static void setTestToday(final Date d) {
		testToday = d;
	}

	public static Date getToday() {
		DateP d = new DateP();
		Date tt;
		if (testToday != null) {
			tt = testToday;
		} else {
			tt = new Date();
		}
		d.setD(tt);
		return d.getD();
	}

	public static void addDays(final Date d, int noD) {
		while (noD > 0) {
			NextDay(d);
			noD--;
		}
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
}
