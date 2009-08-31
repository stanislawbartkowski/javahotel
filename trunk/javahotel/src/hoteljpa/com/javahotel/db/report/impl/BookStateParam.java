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
package com.javahotel.db.report.impl;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.rescache.ReadResParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class BookStateParam {

	private final ReadResParam rParam;

	BookStateParam(final CommandParam p) {
		List<String> resList;
		Date dFrom = p.getDateFrom();
		Date dTo = p.getDateTo();
		resList = new ArrayList<String>();
		for (int no = 0;; no++) {
			String s = p.getResListNo(no);
			if (s == null) {
				break;
			}
			resList.add(s);
		}
		rParam = new ReadResParam(resList, new PeriodT(dFrom, dTo));
	}

	public ReadResParam getRParam() {
		return rParam;
	}
}
