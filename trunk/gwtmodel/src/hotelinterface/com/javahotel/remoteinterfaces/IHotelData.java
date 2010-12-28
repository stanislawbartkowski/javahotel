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
package com.javahotel.remoteinterfaces;

import java.util.List;

import javax.ejb.Remote;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Remote
public interface IHotelData {

	List<AbstractTo> getDicList(SessionT sessionId, DictType d,
			HotelT hotel);

	void persistDic(SessionT sessionId, DictType d, DictionaryP a);

	ReturnPersist persistDicReturn(SessionT sessionId, DictType d, DictionaryP a);

	ReturnPersist persistResBookingReturn(SessionT sessionId, BookingP a);

	void removeDic(SessionT sessionId, DictType d, DictionaryP a);

	void clearHotelBase(SessionT sessionId, HotelT hotel);

	ReturnPersist testDicPersist(SessionT sessionId, PersistType t,
			DictType da, DictionaryP a);
}
