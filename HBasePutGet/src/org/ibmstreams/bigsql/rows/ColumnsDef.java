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
package org.ibmstreams.bigsql.rows;

import java.util.ArrayList;
import java.util.List;

import org.ibmstreams.bigsql.mapping.BIGSQLTYPE;
import org.ibmstreams.bigsql.mapping.ColumnMapping;

/**
 * 
 * @author sbartkowski
 *         <p>
 *         HBase-BigSQL column mapping
 */

public class ColumnsDef {

	private final BIGSQLTYPE keyt;

	private final List<ColumnMapping> cols = new ArrayList<ColumnMapping>();

	/**
	 * Constructor, creates empty column mapping list
	 * 
	 * @param keyt
	 *            HBase index type
	 */
	public ColumnsDef(BIGSQLTYPE keyt) {
		this.keyt = keyt;
	}

	public BIGSQLTYPE getKeyt() {
		return keyt;
	}

	public List<ColumnMapping> getCols() {
		return cols;
	}

	/**
	 * Adds next column to list
	 * 
	 * @param CF
	 *            Column family
	 * @param CQ
	 *            Column member
	 * @param t
	 *            BigSQL column type
	 */
	public void addCol(String CF, String CQ, BIGSQLTYPE t) {
		cols.add(new ColumnMapping(CF, CQ, t));
	}

}
