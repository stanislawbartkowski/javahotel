/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sampledb.shared;

import java.io.Serializable;

import com.ibm.sampledb.shared.GetField.FieldType;

/**
 * Description of one column in IRecord object
 * 
 * @author sbartkowski
 * 
 */
@SuppressWarnings("serial")
public class RowFieldInfo implements Serializable {
	/** Column identifier. */
	private String fId;
	/** Column type. */
	private FieldType fType;
	/** Column description, title. */
	private String fDescr;

	/** Column size to display (in pixels). */
	private int cSize;
	/** Position in ResultSer. */
	private int colPos;

	public RowFieldInfo() {

	}

	public RowFieldInfo(String fId, FieldType fType, String fDescr, int cSize,
			int colPos) {
		this.fId = fId;
		this.fType = fType;
		this.fDescr = fDescr;
		this.cSize = cSize;
		this.colPos = colPos;
	}

	public int getColPos() {
		return colPos;
	}

	public int getcSize() {
		return cSize;
	}

	public String getfId() {
		return fId;
	}

	public FieldType getfType() {
		return fType;
	}

	public String getfDescr() {
		return fDescr;
	}

}
