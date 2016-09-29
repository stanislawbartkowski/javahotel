package org.streams.testh1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.hbase.client.Table;
import org.ibmstreams.bigsql.hbase.PutRow;
import org.ibmstreams.bigsql.mapping.BIGSQLTYPE;
import org.ibmstreams.bigsql.mapping.HVal;
import org.ibmstreams.bigsql.rows.ColumnsDef;
import org.ibmstreams.bigsql.rows.RowsVals;
import org.junit.Test;
import org.streams.testh.TestEnv;
import org.streams.testh.TestHelper;

public class TestHR extends TestEnv {

	private final static String schemaN = "sb";
	private final static String NULL = "null";

	private void insertBig(String tableName, String[] vals) throws SQLException {
		PreparedStatement prep = conB.prepareStatement("INSERT INTO " + tableName + " VALUES(?,?)");
		prep.setString(1, vals[0]);
		prep.setString(2, vals[1]);
		prep.executeUpdate();
	}

	private void verifyBig(String tableName, BIGSQLTYPE tCol, String[] vals) throws SQLException, ParseException {
		PreparedStatement prep = conB.prepareStatement("SELECT v from " + tableName + " WHERE k = ?");
		prep.setString(1, vals[0]);
		ResultSet res = prep.executeQuery();
		assertTrue(res.next());
		if (vals[1].equals(NULL)) {
			Object o = res.getObject(1);
			assertTrue(res.wasNull());
			return;
		}
		switch (tCol) {
		case CHAR:
			String val = res.getString(1);
			System.out.println(val);
			assertEquals(vals[1], val);
			break;
		case TINYINT:
		case INT:
			int vali = res.getInt(1);
			assertEquals(Integer.parseInt(vals[1].trim()), vali);
			break;
		case BIGINT:
			long vall = res.getLong(1);
			assertEquals(Long.parseLong(vals[1].trim()), vall);
			break;
		case FLOAT:
			float valf = res.getFloat(1);
			assertEquals(Float.parseFloat(vals[1]), valf, 0.00001);
			break;
		case DOUBLE:
			double vald = res.getDouble(1);
			assertEquals(Double.parseDouble(vals[1]), vald, 0.00001);
			break;
		case TIMESTAMP:
			Timestamp valt = res.getTimestamp(1);
			assertEquals(Timestamp.valueOf(vals[1]), valt);
			break;
		case DATE:
			Date valdt = res.getDate(1);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			assertEquals(formatter.parse(vals[1]), valdt);
			break;
		case BOOLEAN:
			boolean valb = res.getBoolean(1);
			if (Boolean.parseBoolean(vals[1]))
				assertTrue(valb);
			else
				assertFalse(valb);
			break;
		case DECIMAL:
			BigDecimal valbd = res.getBigDecimal(1);
			assertEquals(Double.parseDouble(vals[1]), valbd.doubleValue(), 0.00001);
			break;
		default:
			fail();
		}
		;
	}

	private void insertHBase(Table t, BIGSQLTYPE tCol, String[] valv) throws IOException {
		ColumnsDef col = TestHelper.defC(tCol);
		RowsVals vals = new RowsVals(HVal.createS(valv[0]));
		if (valv[1].equals(NULL))
			vals.addVal(HVal.createNull());
		else
			vals.addVal(HVal.formString(tCol, valv[1]));
		TestHelper.PutR(t, col, vals);
	}

	private void testFromResource(String test)
			throws IOException, ClassNotFoundException, SQLException, ParseException {
		ClassLoader classLoader = TestHR.class.getClassLoader();
		InputStream is = classLoader.getResource("resource/" + test + ".txt").openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String tableName = reader.readLine();
		String columnType = reader.readLine();
		BIGSQLTYPE tCol = BIGSQLTYPE.valueOf(columnType);
		String createTable = reader.readLine();
		try {
			runU("DROP TABLE " + tableName);
		} catch (SQLException e) {
			// expected here, the table could not exist
		}
		runU(createTable);
		Table t = TestHelper.getTable(conH, schemaN + "." + tableName.toLowerCase());

		String dataLine = null;
		while ((dataLine = reader.readLine()) != null) {
			if (dataLine.equals(""))
				continue;
			System.out.println(dataLine);
			String[] vals = dataLine.split(",");
			assertEquals(2, vals.length);
			// insertBig(tableName,vals);
			insertHBase(t, tCol, vals);
			verifyBig(tableName, tCol, vals);
		}
		is.close();
	}

	@Test
	public void test1() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test1");
	}

	@Test
	public void test2() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test2");
	}

	@Test
	public void test3() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test3");
	}

	@Test
	public void test4() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test4");
	}

	@Test
	public void test5() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test5");
	}

	@Test
	public void test6() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test6");
	}

	@Test
	public void test7() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test7");
	}

	@Test
	public void test8() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test8");
	}

	@Test
	public void test9() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test9");
	}

	@Test
	public void test10() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test10");
	}

	@Test
	public void test11() throws IOException, ClassNotFoundException, SQLException, ParseException {
		testFromResource("test11");
	}

}
