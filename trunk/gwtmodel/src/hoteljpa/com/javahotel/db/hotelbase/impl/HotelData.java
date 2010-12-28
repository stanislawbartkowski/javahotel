/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.hotelbase.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.db.commands.ClearHotelData;
import com.javahotel.db.commands.GetListCommand;
import com.javahotel.db.commands.PersistDictCommand;
import com.javahotel.db.commands.RemoveDictCommand;
import com.javahotel.db.commands.TestPersistCommand;
import com.javahotel.db.hoteldb.HotelStore;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IHotelData;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Stateless(mappedName = "hotelEJB")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class HotelData implements IHotelData {

	public List<AbstractTo> getDicList(final SessionT sessionId,
			final DictType d, final HotelT hotel) {
		GetListCommand co = new GetListCommand(sessionId, d, hotel);
		co.run();
		return co.getCol();
	}

	public void persistDic(final SessionT sessionId, final DictType d,
			final DictionaryP a) {
		PersistDictCommand co = new PersistDictCommand(sessionId, d, a, false);
		co.run();
	}

	public void removeDic(final SessionT sessionId, final DictType d,
			final DictionaryP a) {
		RemoveDictCommand co = new RemoveDictCommand(sessionId, d, a);
		co.run();
	}

	public ReturnPersist persistDicReturn(final SessionT sessionId,
			final DictType d, final DictionaryP a) {
		PersistDictCommand co = new PersistDictCommand(sessionId, d, a, false);
		co.run();
		return co.getRes();
	}

	public ReturnPersist persistResBookingReturn(final SessionT sessionId,
			final BookingP a) {
		PersistDictCommand co = new PersistDictCommand(sessionId,
				DictType.BookingList, a, true);
		co.run();
		return co.getRes();
	}

	public void clearHotelBase(SessionT sessionId, final HotelT hotel) {
		if (!HotelStore.isHotel(sessionId, hotel)) {
			return;
		}
		ClearHotelData cData = new ClearHotelData(sessionId, hotel);
		cData.run();
	}

	public ReturnPersist testDicPersist(SessionT sessionId, PersistType t,
			final DictType d, DictionaryP a) {
		TestPersistCommand co = new TestPersistCommand(sessionId, t, d, a);
		co.run();
		return co.getRet();
	}
}
