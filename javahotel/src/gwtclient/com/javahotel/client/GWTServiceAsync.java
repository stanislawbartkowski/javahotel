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
package com.javahotel.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.webhelper.HotelHelper;

import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface GWTServiceAsync {

	void getList(RType r, CommandParam p, AsyncCallback callback);

	void getOne(RType r, CommandParam p, AsyncCallback callback);

	void loginUser(String user, String password, AsyncCallback callback);

	void loginAdmin(String user, String password, AsyncCallback callback);

	void logout(AsyncCallback callback);

	void addHotel(HotelP hotel, AsyncCallback callback);

	void removeHotel(HotelP hotel, AsyncCallback callback);

	void addPerson(PersonP person, String password, AsyncCallback callback);

	void removePerson(PersonP person, AsyncCallback callback);

	void setRoles(String person, String hotel, List<String> roles,
			AsyncCallback callback);

	void persistDict(DictType d, DictionaryP t, AsyncCallback callback);

	void persistDictRet(DictType d, DictionaryP t, AsyncCallback callback);

	void removeDict(DictType d, DictionaryP t, AsyncCallback callback);

	void hotelOp(HotelOpType op, CommandParam p, AsyncCallback callback);

	void hotelOpRet(HotelOpType op, CommandParam p, AsyncCallback<ReturnPersist> callback);

	void hotelOpRet(CommandParam p, AsyncCallback<ReturnPersist> callback);

	void hotelOpRet(List<CommandParam> p, AsyncCallback<List<ReturnPersist>> callback);

	void persistResBookingReturn(BookingP dp, AsyncCallback callback);

	void getParam(AsyncCallback callback);

	void clearHotelData(HotelP hotel, AsyncCallback callback);

	void testPersistHotel(PersistType t, HotelP hotel,
			AsyncCallback<ReturnPersist> callback);

	void testPersistPerson(PersistType t, PersonP hotel,
			AsyncCallback<ReturnPersist> callback);

	void testDictPersist(PersistType t, DictType da, DictionaryP a,
			AsyncCallback<ReturnPersist> callback);

}
