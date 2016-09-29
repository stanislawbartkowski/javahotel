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
package org.ibmstreams.bigsql.mapping;

/**
 * @author sbartkowski
 * 
 * <p>
 *
 *         Column mapping for HBase. Column family, member and corresponding
 *         BigSQL column type
 */

public class ColumnMapping {

	/** Column family. */
	private final byte[] CF;

	/** Column member. */
	private final byte[] CQ;

	/** BigSQL column typ. */
	private final BIGSQLTYPE t;

	public ColumnMapping(String CF, String CQ, BIGSQLTYPE t) {
		this.CF = CF.getBytes();
		this.CQ = CQ.getBytes();
		this.t = t;
	}

	public byte[] getCF() {
		return CF;
	}

	public byte[] getCQ() {
		return CQ;
	}

	public BIGSQLTYPE getT() {
		return t;
	}

}
