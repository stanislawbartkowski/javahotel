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
package org.ibmstreams.bigsql.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.ibmstreams.bigsql.rows.ColumnsDef;
import org.ibmstreams.bigsql.rows.RowsVals;

/**
 * 
 * @author sbartkowski
 *         <p>
 *         Package to put row into HBase. Supports batch insert.
 */

public class PutRow {

	private final List<Put> bList = new ArrayList<Put>();
	private final int batchsize;
	private final Table t;

	/**
	 * Constructor.
	 * 
	 * @param t
	 *            HBase Table
	 * @param batchsize
	 *            batch size
	 */
	public PutRow(Table t, int batchsize) {
		assert batchsize > 0;
		this.batchsize = batchsize;
		this.t = t;
	}

	/**
	 * Flushes buffer to HBase. If buffer empty then does nothing.
	 * 
	 * @throws IOException
	 */
	public void flushR() throws IOException {
		if (bList.isEmpty())
			return;
		t.put(bList);
		bList.clear();
	}

	/**
	 * Put single row into HBase. Flushes to HBase only if buffer size
	 * overflows.
	 * 
	 * @param c
	 *            HBase column mapping
	 * @param r
	 *            Row of value corresponding to c list
	 * @throws IOException
	 */
	public void PutR(ColumnsDef c, RowsVals r) throws IOException {
		assert c.getCols().size() == r.getVlist().size();
		Put p = new Put(r.getKey().toB(c.getKeyt()));
		for (int i = 0; i < c.getCols().size(); i++)
			p.addColumn(c.getCols().get(i).getCF(), c.getCols().get(i).getCQ(),
					r.getVlist().get(i).toB(c.getCols().get(i).getT()));
		bList.add(p);
		if (bList.size() >= batchsize)
			flushR();
	}

}
