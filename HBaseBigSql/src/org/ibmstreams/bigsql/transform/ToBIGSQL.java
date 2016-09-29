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
package org.ibmstreams.bigsql.transform;

import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author sbartkowski
 *         <p>
 *         Provides transformation of Java type to appropriate byte[] binary
 *         sequence. Helper class, contains only static methods.
 * 
 */
public class ToBIGSQL {

	private static final long SEVEN_BYTE_LONG_SIGN_FLIP = 0xff80L << 48;

	private ToBIGSQL() {

	}

	/**
	 * Construct null value
	 * 
	 * @return
	 */
	public static byte[] toNull() {
		return new byte[] { IVALS.NULL };
	}

	/**
	 * TONYINT (single byte) value to binary
	 * 
	 * @param v
	 * @return
	 */
	public static byte[] tinytoB(int v) {
		return new byte[] { IVALS.NOTNULL, (byte) (v ^ 0x80) };
	}

	/**
	 * BIGING (long integer) value to binary
	 * 
	 * @param v
	 * @return
	 */
	public static byte[] longtoB(long v) {
		return new byte[] { IVALS.NOTNULL, (byte) ((0xff & (v >> 56)) ^ 0x80), (byte) ((0xff & (v >> 48))),
				(byte) ((0xff & (v >> 40))), (byte) ((0xff & (v >> 32))), (byte) ((0xff & (v >> 24))),
				(byte) ((0xff & (v >> 16))), (byte) ((0xff & (v >> 8))), (byte) (0xff & v) };

	}

	/**
	 * FLOAT value transformation to binary. Strange: the same as for double, no
	 * difference. Discovered by experience.
	 * 
	 * @param f
	 * @return
	 */

	public static byte[] floattoB(float f) {
		return doubletoB(f);
	}

	/**
	 * DOUBLE value transformation to binary
	 * 
	 * @param f
	 * @return
	 */
	public static byte[] doubletoB(double f) {
		ByteBuffer bf = ByteBuffer.allocate(8);
		byte[] b = new byte[8];
		bf.putDouble(f);
		bf.position(0);
		bf.get(b);
		if (f >= 0)
			return new byte[] { IVALS.NOTNULL, (byte) (b[0] ^ (byte) (1 << 7)), b[1], b[2], b[3], b[4], b[5], b[6],
					b[7] };
		else
			return new byte[] { IVALS.NOTNULL, (byte) ~b[0], (byte) ~b[1], (byte) ~b[2], (byte) ~b[3], (byte) ~b[4],
					(byte) ~b[5], (byte) ~b[6], (byte) ~b[7] };
	}

	/**
	 * INT (4 bytes) transformation to binary
	 * 
	 * @param v
	 * @return
	 */
	public static byte[] inttoB(int v) {
		return new byte[] { IVALS.NOTNULL, (byte) ((0xff & (v >> 24)) ^ 0x80), (byte) ((0xff & (v >> 16))),
				(byte) ((0xff & (v >> 8))), (byte) (0xff & v) };
	}

	private static byte[] serializeBytes(String s) {
		byte[] bt = s.getBytes();
		int len = bt.length + 1;
		// calculate number of bytes
		for (byte b : bt)
			if (b == 0 || b == 1)
				len++;
		// allocate
		byte[] bx = new byte[len];
		int j = 0;
		for (int i = 0; i < bt.length; i++)
			if (bt[i] == 0 || bt[i] == 1) {
				bx[j++] = 1;
				bx[j++] = (byte) (bt[i] + 1);
			} else
				bx[j++] = bt[i];
		bx[j] = 0;
		return bx;
	}

	/**
	 * CHAR (VARCHAR, string) transformation to binary
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] sToB(String s) {
		byte[] bt = serializeBytes(s);
		byte[] bx = new byte[bt.length + 1];
		bx[0] = IVALS.NOTNULL;
		for (int i = 0; i < bt.length; i++)
			bx[i + 1] = bt[i];
		return bx;
	}

	static private void sevenByteLongToBytes(long value, byte[] dest, int offset) {
		dest[offset] = (byte) ((value >> 48) & 0xFF);
		dest[offset + 1] = (byte) ((value >> 40) & 0xFF);
		dest[offset + 2] = (byte) ((value >> 32) & 0xFF);
		dest[offset + 3] = (byte) ((value >> 24) & 0xFF);
		dest[offset + 4] = (byte) ((value >> 16) & 0xFF);
		dest[offset + 5] = (byte) ((value >> 8) & 0xFF);
		dest[offset + 6] = (byte) (value & 0xFF);
	}

	/**
	 * TIMESTAMP transformation to binary
	 * 
	 * @param t
	 * @return
	 */
	public static byte[] timestamptoB(Timestamp t) {
		long sec = t.getTime() / 1000; // number of seconds
		sec ^= SEVEN_BYTE_LONG_SIGN_FLIP;
		int nano = t.getNanos();
		byte[] b = new byte[12];
		b[0] = IVALS.NOTNULL;
		sevenByteLongToBytes(sec, b, 1);
		b[8] = (byte) ((0xff & (nano >> 24)));
		b[9] = (byte) ((0xff & (nano >> 16)));
		b[10] = (byte) ((0xff & (nano >> 8)));
		b[11] = (byte) (0xff & nano);
		return b;
	}

	/**
	 * DATE (Date) transformation to binary
	 * 
	 * @param d
	 * @return
	 */
	public static byte[] datetoB(Date d) {
		return timestamptoB(new Timestamp(d.getTime()));
	}

	/**
	 * BOOLEAN transformation to binary
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] booltoB(Boolean b) {
		return new byte[] { IVALS.NOTNULL, b.booleanValue() ? (byte) 2 : (byte) 1 };
	}

	/**
	 * Creates BigInt instance. Important: provides maximum supported precise
	 * value
	 * 
	 * @param f
	 * @return
	 */
	public static BigDecimal tBC(double f) {
		return new BigDecimal(f, new MathContext(31));
	}

	/**
	 * DECIMAL transformation to binary
	 * 
	 * @param dec
	 * @return
	 */
	public static byte[] decimaltoB(BigDecimal dec) {
		// get the sign of the big decimal
		int sign = dec.compareTo(BigDecimal.ZERO);

		// we'll encode the absolute value (sign is separate)
		dec = dec.abs();

		// get the scale factor to turn big decimal into a decimal < 1
		// This relies on the BigDecimal precision value, which as of HIVE-10270
		// is now different from HiveDecimal.precision()
		int factor = dec.precision() - dec.scale();
		factor = sign == 1 ? factor : 0 - factor;

		// convert the absolute big decimal to string
		dec = dec.scaleByPowerOfTen(Math.abs(dec.scale()));
		String digits = dec.unscaledValue().toString();

		byte[] bx = new byte[] { IVALS.NOTNULL, (byte) (sign + 1), (byte) ((0xff & (factor >> 24)) ^ 0x80),
				(byte) (0xff & (factor >> 16)), (byte) (0xff & (factor >> 8)), (byte) (0xff & factor) };

		// finally write out the pieces (sign, scale, digits)
		byte[] bt = serializeBytes(digits);
		int len = bx.length + bt.length;
		if (sign == -1)
			len++;
		byte[] bout = new byte[len];
		for (int i = 0; i < bx.length; i++)
			bout[i] = bx[i];
		for (int i = 0; i < bt.length - 1; i++)
			bout[bx.length + i] = sign != -1 ? bt[i] : (byte) (0xff ^ bt[i]);
		// Important: for negative value -1 should be inserted before final 0.
		// Discovered by experience
		if (sign == -1)
			bout[len - 2] = -1;
		bout[len - 1] = 0;
		return bout;
	}

}
