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
package com.gwtmodel.table.common;

import java.math.BigDecimal;

public class DecimalUtils {

	private DecimalUtils() {

	}

	public static double toDouble(String s) {
		double d = Double.valueOf(s);
		return d;
	}

	public static double toDoubleE(String s, double badF) {
		try {
			return toDouble(s);
		} catch (NumberFormatException e) {
			return badF;
		}
	}

	// BigDecimal
	public static BigDecimal toBig(final String s) {
		return toBig(s, -1);
	}

	public static BigDecimal toBig(final String s, int afterdot) {
		if (CUtil.EmptyS(s)) {
			return null;
		}
		try {
			BigDecimal c = new BigDecimal(s);
			if (afterdot != -1) {
				c = c.setScale(afterdot, BigDecimal.ROUND_HALF_UP);
			}
			return c;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static BigDecimal toBig(final Double d) {
		if (d == null) {
			return null;
		}
		String s = Double.toString(d);
		return new BigDecimal(s);
	}

	public static Double toDouble(final BigDecimal b) {
		if (b == null) {
			return null;
		}
		String s = DecimalToS(b);
		return new Double(s);
	}

	public static String DecimalToS(final BigDecimal c, int afterdot) {
		BigDecimal cx = c.setScale(afterdot, BigDecimal.ROUND_HALF_UP);
		String ss = cx.toPlainString();
		int pos = ss.indexOf('.');
		String aDot = "";
		if (pos >= 0) {
			aDot = ss.substring(pos + 1);
			ss = ss.substring(0, pos);
		}
		if (afterdot <= 0) {
			return ss;
		}
		if (aDot.length() > afterdot) {
			aDot = aDot.substring(0, afterdot);
		}
		while (aDot.length() < afterdot) {
			aDot += "0";
		}
		return ss + "." + aDot;
	}

	public static String DecimalToS(final BigDecimal c) {
		return DecimalToS(c, 2);
	}

	public static BigDecimal percent0(final BigDecimal c, final BigDecimal percent) {
		BigDecimal res = c.multiply(percent);
		BigDecimal l100 = new BigDecimal("100");
		BigDecimal res1 = res.divide(l100, 2, 0);
		return res1;
	}

}
