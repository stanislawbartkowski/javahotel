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
package org.streams.testh;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.ibmstreams.bigsql.hbase.PutRow;
import org.ibmstreams.bigsql.mapping.BIGSQLTYPE;
import org.ibmstreams.bigsql.rows.ColumnsDef;
import org.ibmstreams.bigsql.rows.RowsVals;

public class TestHelper {

	private TestHelper() {

	}

	public static final String CF = "cf_data";
	public static final String CQ = "cq_x";
	
	public static void PutR(Table t, ColumnsDef c, RowsVals r) throws IOException {
		PutRow p = new PutRow(t,1);
		p.PutR(c, r);
		p.flushR();
	}


	public static Table getTable(Connection conn, String tableName) throws IOException {
		Admin admin = conn.getAdmin();
		HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableName));
		Table tables = conn.getTable(table.getTableName());
		boolean e = admin.tableExists(table.getTableName());
		return tables;
	}

	public static Connection connect() throws IOException {
		Configuration hBaseConfig = HBaseConfiguration.create();
		InputStream confResourceAsInputStream = hBaseConfig.getConfResourceAsInputStream("hbase-site.xml");
		int available = confResourceAsInputStream.available();

		// hBaseConfig.setInt("timeout", 120000);
		// hBaseConfig.set("hbase.master", hbaseHost + ":60000");
		// hBaseConfig.set("hbase.zookeeper.quorum", zookeeperHost);
		// hBaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
		Connection conn = ConnectionFactory.createConnection(hBaseConfig);
		return conn;
	}

	public static ColumnsDef defC(BIGSQLTYPE t) {
		ColumnsDef col = new ColumnsDef(BIGSQLTYPE.CHAR);
		col.addCol(CF, CQ, t);
		return col;
	}

	public static ColumnsDef defC3() {
		ColumnsDef col = new ColumnsDef(BIGSQLTYPE.INT);
		col.addCol(CF, CQ, BIGSQLTYPE.CHAR);
		return col;
	}

	public static Timestamp T(int yy, int mm, int dd, int ho, int mi, int sec, int nano) {
		Timestamp t = new Timestamp(yy, mm, dd, ho, mi, sec, nano);
		return t;
	}

	public static Date D(int yy, int mm, int dd) {
		return new Date(yy, mm, dd);
	}

	private static final String urlS = "jdbc:db2://big64:32051/BIGSQL";
	private static final String user = "sb";
	private static final String password = "leszek123";

	public static java.sql.Connection getC() throws ClassNotFoundException, SQLException {
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		java.sql.Connection con = DriverManager.getConnection(urlS, user, password);
		return con;
	}

}
