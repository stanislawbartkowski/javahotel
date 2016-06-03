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
package com.gwtmodel.table.view.ewidget.polymer;

import java.sql.Timestamp;
import java.util.Date;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormFieldProperties;

class DateTimePolymer extends DateCalendarPolymer {
	
	private class GetSet implements IGetSetValue {

		@Override
		public Date getVal() {
			Timestamp ta = (Timestamp) getValObj();
			if (ta == null) return null;
			return new Date(ta.getTime());
		}

		@Override
		public void setVal(Date d) {
			if (d == null) {
				setValObj(null);
				return;
			}
			Timestamp ta = (Timestamp) getValObj();
			if (ta == null) 
				setValObj(new Timestamp(d.getYear(),d.getMonth(),d.getDate(),0,0,0,0));
			else setValObj(new Timestamp(d.getYear(),d.getMonth(),d.getDate(),ta.getHours(),ta.getMinutes(),ta.getSeconds(),0));			
		}
		
	}
	
	DateTimePolymer(IVField v, IFormFieldProperties pr, String pattern, String standErrMess) {
		super(v, pr,pattern,standErrMess);
		this.iGet = new GetSet();
	}

}