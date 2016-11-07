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
package com.gwtmodel.table.view.grid;

import java.math.BigDecimal;

public class GridViewFactory {

	private GridViewFactory() {
	}

	private static class Decimal extends GridView implements IGridViewDecimal {

		Decimal(GridViewType gType) {
			super(gType);
		}

		@Override
		public BigDecimal getCellDecimal(int row, int c) {
			return (BigDecimal) getCell(row, c);
		}

		@Override
		public void setRowDecimal(int row, int c, BigDecimal b) {
			setRowVal(row, c, b);
		}

	}

	private static class GBoolean extends GridView implements IGridViewBoolean {

		GBoolean(GridViewType gType) {
			super(gType);
		}

		@Override
		public Boolean getCellBoolean(int row, int c) {
			return (Boolean) getCell(row, c);
		}

		@Override
		public void setRowBoolean(int row, int c, Boolean b) {
			setRowVal(row, c, b);
		}

	}

	public static IGridViewDecimal constructDecimal(boolean horizontal, boolean rowBeginning, boolean colHeaders) {
		return new Decimal(new GridViewType(GridViewType.GridType.DECIMAL, horizontal, rowBeginning, colHeaders));
	}

	public static IGridViewBoolean constructBoolean(boolean horizontal, boolean rowBeginning, boolean colHeaders) {
		return new GBoolean(new GridViewType(GridViewType.GridType.BOOLEAN, horizontal, rowBeginning, colHeaders));
	}
}
