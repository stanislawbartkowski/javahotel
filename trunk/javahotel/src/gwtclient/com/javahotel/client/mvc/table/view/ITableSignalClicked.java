/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.table.view;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.dialog.IWidgetSize;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface ITableSignalClicked {

	public class ClickedContext {

		final int row;
		final int col;
		final IField fCol;
		private final IWidgetSize w;

		public ClickedContext(int row, int col, IField fcol, IWidgetSize w) {
			this.row = row;
			this.col = col;
			this.fCol = fcol;
			this.w = w;
		}

		/**
		 * @return the row
		 */
		public int getRow() {
			return row;
		}

		/**
		 * @return the col
		 */
		public int getCol() {
			return col;
		}

		/**
		 * @return the fCol
		 */
		public IField getFCol() {
			return fCol;
		}

		/**
		 * @return the w
		 */
		public IWidgetSize getW() {
			return w;
		}
	}

	void signal(ClickedContext co);
}
