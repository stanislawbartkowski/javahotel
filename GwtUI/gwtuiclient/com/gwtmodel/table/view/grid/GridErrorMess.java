/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.view.grid;

import com.gwtmodel.table.InvalidateMess;

/**
 * @author hotel
 * 
 */
public class GridErrorMess extends InvalidateMess {

	private final int row;
	private final int col;

	public GridErrorMess(int row, int col, String errMess) {
		super(null, errMess == null, errMess, false);
		this.row = row;
		this.col = col;
	}

	/**
	 * @return the row
	 */
	int getRow() {
		return row;
	}

	/**
	 * @return the col
	 */
	int getCol() {
		return col;
	}

}
