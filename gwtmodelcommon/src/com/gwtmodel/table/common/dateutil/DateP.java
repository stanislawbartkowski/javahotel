/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.common.dateutil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class DateP implements Serializable {

	private int year, month, day;
	private int h, m, s;
	private boolean empty;
	public static final int DEFH = 5;
	public static final int DEFM = 5;
	public static final int DEFS = 5;

	public DateP() {
		empty = true;
		h = DEFH;
		m = DEFM;
		s = DEFS;
	}

	public int getYear() {
		return year;
	}

	public void setYear(final int year) {
		empty = false;
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(final int month) {
		empty = false;
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(final int day) {
		empty = false;
		this.day = day;
	}

	@SuppressWarnings("deprecation")
    public void setT(final Timestamp d) {
		if (d == null) {
			empty = true;
			return;
		}
		setYear(d.getYear() + 1900);
		setMonth(d.getMonth() + 1);
		setDay(d.getDate());
		setH(d.getHours());
		setM(d.getMinutes());
		setS(d.getSeconds());
	}

	@SuppressWarnings("deprecation")
    public void setD(final Date d) {
		if (d == null) {
			empty = true;
			return;
		}
		setYear(d.getYear() + 1900);
		setMonth(d.getMonth() + 1);
		setDay(d.getDate());
		setH(DEFH);
		setM(DEFM);
		setS(DEFS);
	}

	@SuppressWarnings("deprecation")
    public Date getD() {
		if (empty) {
			return null;
		}
		Date d = new Date(year - 1900, month - 1, day, DEFH, DEFM, DEFS);
		return d;
	}

	@SuppressWarnings("deprecation")
    public Timestamp getT() {
		if (empty) {
			return null;
		}
		Timestamp d = new Timestamp(year - 1900, month - 1, day, getH(),
				getM(), getS(), 0);
		return d;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}
}
