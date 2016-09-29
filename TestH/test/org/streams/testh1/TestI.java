package org.streams.testh1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.hbase.client.Table;
import org.ibmstreams.bigsql.hbase.PutRow;
import org.ibmstreams.bigsql.mapping.BIGSQLTYPE;
import org.ibmstreams.bigsql.mapping.ColumnMapping;
import org.ibmstreams.bigsql.mapping.HVal;
import org.ibmstreams.bigsql.rows.ColumnsDef;
import org.ibmstreams.bigsql.rows.RowsVals;
import org.junit.Test;
import org.streams.testh.TestEnv;
import org.streams.testh.TestHelper;

public class TestI extends TestEnv {

	private final static String tName = "GOSALESDW.SLS_SALES_FACT";

	private final static String fileName = "/home/sbartkowski/Pobrane/IBD-1687A_Data/gosalesdw/SLS_SALES_FACT.10p.txt";

	public static final String CF = "cf_data";

	private void createTable() throws IOException, SQLException {
		BufferedReader buffer = getB("createt");
		StringBuffer r = null;
		String l = null;
		while ((l = buffer.readLine()) != null) {
			if (r == null)
				r = new StringBuffer(l);
			else
				r.append(" " + l);
		}
		try {
			runU("DROP TABLE " + tName);
		} catch (SQLException e) {
			// is expected
		}
		runU(r.toString());
	}

	private void loadData(ColumnsDef col, Table t) throws MalformedURLException, IOException {
		File f = new File(fileName);
		InputStream i = f.toURI().toURL().openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(i));
		String l = null;
		int c = 0;
		PutRow put = new PutRow(t, 100);
		while ((l = reader.readLine()) != null) {
			c++;
			String[] vals = l.split("\t");
			// System.out.println(vals.length);
			RowsVals row = new RowsVals(HVal.createInt(c));
			int j = 0;
			for (ColumnMapping co : col.getCols()) {
				row.addVal(HVal.formString(co.getT(), vals[j++]));
			} // for
			put.PutR(col, row);
			if (c % 100 == 0)
				System.out.println(c);
		} // while
		put.flushR();
	}

	private ColumnsDef prepareCol() {
		ColumnsDef col = new ColumnsDef(BIGSQLTYPE.INT);
		col.addCol(CF, "cq_ORDER_DAY_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_ORGANIZATION_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_EMPLOYEE_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_RETAILER_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_RETAILER_SITE_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_PRODUCT_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_PROMOTION_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_ORDER_METHOD_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_SALES_ORDER_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_SHIP_DAY_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_CLOSE_DAY_KEY", BIGSQLTYPE.INT);
		col.addCol(CF, "cq_QUANTITY", BIGSQLTYPE.INT);

		col.addCol(CF, "cq_UNIT_COST", BIGSQLTYPE.DECIMAL);
		col.addCol(CF, "cq_UNIT_PRICE", BIGSQLTYPE.DECIMAL);
		col.addCol(CF, "cq_UNIT_SALE_PRICE", BIGSQLTYPE.DECIMAL);

		col.addCol(CF, "cq_GROSS_MARGIN", BIGSQLTYPE.DOUBLE);
		col.addCol(CF, "cq_SALE_TOTAL", BIGSQLTYPE.DECIMAL);
		col.addCol(CF, "cq_GROSS_PROFIT", BIGSQLTYPE.DECIMAL);

		return col;

	}

	private void verifyS(String stmt, int expected) throws SQLException {
		ResultSet res = conB.prepareStatement(stmt).executeQuery();
		assertTrue(res.next());
		int r = res.getInt(1);
		assertEquals(expected, r);
	}

	private void verifyB() throws SQLException {
		verifyS("SELECT COUNT(*) FROM GOSALESDW.SLS_SALES_FACT", 44603);
		verifyS("SELECT COUNT(organization_key) FROM gosalesdw.sls_sales_fact WHERE order_day_key = 20070720", 1405);
	}

	@Test
	public void test1() throws IOException, SQLException {
		createTable();
		Table t = TestHelper.getTable(conH, tName.toLowerCase());
		ColumnsDef col = prepareCol();
		loadData(col, t);
	}

	@Test
	public void test2() throws SQLException {
		verifyB();
	}

}
