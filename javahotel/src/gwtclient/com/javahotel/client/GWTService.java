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
package com.javahotel.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface GWTService extends RemoteService {

	List<AbstractTo> getList(RType r, CommandParam p);

	AbstractTo getOne(RType r, CommandParam p);

	void loginUser(String user, String password);

	void loginAdmin(String user, String password);

	void logout();

	void addHotel(HotelP hotel);

	void removeHotel(HotelP hotel);

	void addPerson(PersonP person, String password);

	void removePerson(PersonP person);

	void setRoles(String person, String hotel, List<String> roles);

	void persistDict(DictType d, DictionaryP t);

	ReturnPersist persistDictRet(DictType d, DictionaryP t);

	void removeDict(DictType d, DictionaryP t);

	void hotelOp(HotelOpType op, CommandParam p);

	ReturnPersist hotelOpRet(HotelOpType op, CommandParam p);

	ReturnPersist hotelOpRet(CommandParam p);

	List<ReturnPersist> hotelOpRet(List<CommandParam> p);

	ReturnPersist persistResBookingReturn(BookingP dp);

	Map<String, String> getParam();

	void clearHotelData(HotelP hotel);

	ReturnPersist testPersistHotel(PersistType t, HotelP hotel);

	ReturnPersist testPersistPerson(PersistType t, PersonP person);

	ReturnPersist testDictPersist(PersistType t, DictType da, DictionaryP a);

}
