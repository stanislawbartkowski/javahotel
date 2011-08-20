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
package com.javahotel.db.commands;

import java.util.List;

import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.db.commands.bookstate.BookCanReserv;
import com.javahotel.db.copy.BeanPrepareKeys;
import com.javahotel.db.copy.CommonCopyBean;
import com.javahotel.db.hotelbase.types.IPureDictionary;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class PersistDictCommand extends CommandAbstract {

	private final DictType d;
	private final DictionaryP a;
	private GetObjectRes o = null;
	private final boolean checkBook;

	public PersistDictCommand(final SessionT se, final DictType d,
			final DictionaryP a, final boolean checkBook) {
//		super(se, true, new HotelT(a.getHotel()), false);
        super(se, true, new HotelT(a.getHotel()));
		this.d = d;
		this.a = a;
		this.checkBook = checkBook;
	}

	private boolean canBookRes() {
		List<ResDayObjectStateP> conflict = BookCanReserv.isConflict(iC,
				(BookingP) a);
		if (conflict == null) {
			return true;
		}
		ret.setResState(conflict);
		return false;
	}
	
	private void logS() {
		IPureDictionary oo = o.getO();
		if (oo == null) { return ; }
		String logs = iC.getRecordDescr(d,oo);
		String logm;
		if (iC.isNull(oo.getId())) {
			logm = iC.logEvent(IMessId.ADDDICTRECORD,logs);
		}
		else {
			logm = iC.logEvent(IMessId.MODIFDICTRECORD,logs);
		}
		iC.getLog().getL().info(logm);		
	}
	

	@Override
	protected void command() {
		if (checkBook) {
			if (!canBookRes()) {
				return;
			}
		}
		o = getObject(d, a);
		// before start transaction
		BeanPrepareKeys.prepareKeys(iC, a);
//		startTra();
		o.refresh();
		CommonCopyBean.copyB(iC, a, o.getO());		
		startTra();
		logS();
//        iC.getC().persistRecords(iC);
		// GAE: move out - cannot be different transactions
		// 2011/08/09
		iC.getJpa().changeRecord(o.getO());
	}

	@Override
	protected void aftercommit() {
		if (o == null) {
			getRet(ret, null);
		} else {
			getRet(ret, o.getO());
		}
	}

}
