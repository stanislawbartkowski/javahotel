/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.commands.bookstate;

import com.javahotel.common.command.ISignal;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.rescache.ResObjectCache;
import com.javahotel.common.rescache.ResObjectCache.IReadResCallBack;
import com.javahotel.common.rescache.ResObjectCache.IReadResData;
import com.javahotel.common.rescache.ResObjectElem;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.db.context.ICommandContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class BookCanReserv {

	private BookCanReserv() {
	}

	private static ReadResParam createResParam(final BookingP b,
			final List<ResObjectElem> col) {
		BookRecordP bR = GetMaxUtil.getLastBookRecord(b);
		PeriodT pe = new PeriodT(null, null);
		List<String> resList = new ArrayList<String>();
		for (BookElemP e : bR.getBooklist()) {
			// TODO: can be duplicated
			resList.add(e.getResObject());
			PeriodT pR = new PeriodT(e.getCheckIn(), e.getCheckOut());
			ResObjectElem re = new ResObjectElem(e.getResObject(), pR);
			col.add(re);
			pe = DateUtil.getWider(pe, pR);
		}
		return new ReadResParam(resList, pe);
	}

	public static List<ResDayObjectStateP> isConflict(
			final ICommandContext iC, final BookingP p) {
		List<ResObjectElem> col = new ArrayList<ResObjectElem>();
		ReadResParam pa = createResParam(p, col);
		final List<ResDayObjectStateP> rese = BookState.getRestState(iC,
				pa, p.getName());
		IReadResData iRes = new IReadResData() {

			public void getResData(ReadResParam para, IReadResCallBack col) {
				// pa parameter not important
				col.setCol(rese);
			}
		};
		ResObjectCache ca = new ResObjectCache(iRes);
		ISignal ii = new ISignal() {

			public void signal() {
			}
		};
		ca.ReadResState(pa, ii);
		List<ResDayObjectStateP> out = ca.isConflict(col);
		if (out.size() == 0) {
			return null;
		}
		return out;
	}
}
