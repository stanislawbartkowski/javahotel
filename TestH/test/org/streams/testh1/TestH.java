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

package org.streams.testh1;

import java.io.IOException;


import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.ibmstreams.bigsql.mapping.BIGSQLTYPE;
import org.ibmstreams.bigsql.mapping.HVal;
import org.ibmstreams.bigsql.rows.ColumnsDef;
import org.ibmstreams.bigsql.rows.RowsVals;
import org.ibmstreams.bigsql.transform.ToBIGSQL;
import org.junit.Before;
import org.junit.Test;
import org.streams.testh.TestHelper;

public class TestH {

	private Connection conn;

	@Before
	public void setup() throws IOException {
		conn = TestHelper.connect();
	}

	public void winddown() throws IOException {
		if (conn != null)
			conn.close();
	}
	
	
//	CREATE HBASE TABLE TEST ( K INT, X VARCHAR(100)) COLUMN MAPPING ( key  mapped by (k), cf_data: cq_x mapped by (x)); 

	
	@Test
	public void test1() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.testn");
		ColumnsDef col = TestHelper.defC(BIGSQLTYPE.CHAR);
		RowsVals vals = new RowsVals(HVal.createS("key-123"));
		vals.addVal(HVal.createS("new-123"));
		TestHelper.PutR(t, col, vals);
	}

	@Test
	public void test2() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.testb");
		ColumnsDef col = TestHelper.defC(BIGSQLTYPE.TINYINT);
		RowsVals vals = new RowsVals(HVal.createS("key-125"));
		vals.addVal(HVal.createInt(-123));
		TestHelper.PutR(t, col, vals);
	}

	@Test
	public void test3() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.test");
		ColumnsDef col = TestHelper.defC3();
		RowsVals vals = new RowsVals(HVal.createInt(55));
		vals.addVal(HVal.createS("next 55"));
		TestHelper.PutR(t, col, vals);
	}

	@Test
	public void test4() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.testbig");
		ColumnsDef col = TestHelper.defC(BIGSQLTYPE.BIGINT);
		RowsVals vals = new RowsVals(HVal.createS("key"));
		vals.addVal(HVal.createLong(34555));
		TestHelper.PutR(t, col, vals);
		RowsVals val1 = new RowsVals(HVal.createS("key l"));
		val1.addVal(HVal.createLong(-1111134555555L));
		TestHelper.PutR(t, col, val1);
	}

	// CREATE HBASE TABLE TESTFLOAT ( K1 VARCHAR(100), K2 FLOAT ) COLUMN MAPPING
	// ( key mapped by (k1), cf_data: cq_x mapped by (k2));

	@Test
	public void test5() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.testfloat");
		ColumnsDef col = TestHelper.defC(BIGSQLTYPE.FLOAT);
		RowsVals vals = new RowsVals(HVal.createS("key f"));
		vals.addVal(HVal.createFloat(-1102.987f));
		TestHelper.PutR(t, col, vals);
	}

	// CREATE HBASE TABLE TESTDOUBLE ( K1 VARCHAR(100), K2 DOUBLE ) COLUMN
	// MAPPING ( key mapped by (k1), cf_data: cq_x mapped by (k2));

	@Test
	public void test6() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.testdouble");
		ColumnsDef col = TestHelper.defC(BIGSQLTYPE.DOUBLE);
		RowsVals vals = new RowsVals(HVal.createS("key double"));
		vals.addVal(HVal.createDouble(1234.67));
		TestHelper.PutR(t, col, vals);
	}

	// CREATE HBASE TABLE TESTTIMESTAMP ( K1 VARCHAR(100), K2 TIMESTAMP ) COLUMN
	// MAPPING ( key mapped by (k1), cf_data: cq_x mapped by (k2));

	@Test
	public void test7() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.testtimestamp");
		ColumnsDef col = TestHelper.defC(BIGSQLTYPE.TIMESTAMP);
		RowsVals vals = new RowsVals(HVal.createS("time sta"));
		vals.addVal(HVal.createTimestamp(TestHelper.T(116, 10, 5, 9, 12, 34, 123)));
		TestHelper.PutR(t, col, vals);
	}

	// CREATE HBASE TABLE TESTDATE( K1 VARCHAR(100), K2 DATE ) COLUMN MAPPING (
	// key mapped by (k1), cf_data: cq_x mapped by (k2));

	@Test
	public void test8() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.testdate");
		ColumnsDef col = TestHelper.defC(BIGSQLTYPE.DATE);
		RowsVals vals = new RowsVals(HVal.createS("date test"));
		vals.addVal(HVal.createDate(TestHelper.D(116, 10, 5)));
		TestHelper.PutR(t, col, vals);
	}

	// CREATE HBASE TABLE TESTBOOL( K1 VARCHAR(100), K2 BOOLEAN ) COLUMN MAPPING
	// ( key mapped by (k1), cf_data: cq_x mapped by (k2));

	@Test
	public void test9() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.testbool");
		ColumnsDef col = TestHelper.defC(BIGSQLTYPE.BOOLEAN);
		RowsVals vals = new RowsVals(HVal.createS("bool true"));
		vals.addVal(HVal.createBool(true));
		TestHelper.PutR(t, col, vals);
		vals = new RowsVals(HVal.createS("bool false"));
		vals.addVal(HVal.createBool(false));
		TestHelper.PutR(t, col, vals);
	}

	// CREATE HBASE TABLE TESTDECIMAL( K1 VARCHAR(100), K2 DECIMAL(10,2) )
	// COLUMN MAPPING ( key mapped by (k1), cf_data: cq_x mapped by (k2));
	@Test
	public void test10() throws IOException {
		Table t = TestHelper.getTable(conn, "sb.testdecimal");
		ColumnsDef col = TestHelper.defC(BIGSQLTYPE.DECIMAL);
		RowsVals vals = new RowsVals(HVal.createS("decimal test"));
		// BigDecimal dec = new BigDecimal(1.23);
		// double val = dec.doubleValue();
		// int len = dec.unscaledValue().toString().length();
		vals.addVal(HVal.createDecimal(ToBIGSQL.tBC(-123.5678)));
		TestHelper.PutR(t, col, vals);
		// vals = new RowsVals(HVal.createS("decimal test -1"));
		// vals.addVal(HVal.createDecinal(new BigDecimal(-1)));
		// TestHelper.PutRow.TestHelper.PutR(t, col, vals);
		// vals = new RowsVals(HVal.createS("decimal test 12345.99"));
		// vals.addVal(HVal.createDecinal(new BigDecimal(12345)));
		// TestHelper.PutRow.TestHelper.PutR(t, col, vals);
	}

}
