/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import com.google.gwt.user.client.rpc.AsyncCallback;
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
public interface GWTServiceAsync {

    void getList(RType r, CommandParam p,
            AsyncCallback<List<AbstractTo>> callback);

    void getOne(RType r, CommandParam p, AsyncCallback<AbstractTo> callback);

    @SuppressWarnings("rawtypes")
    void loginUser(String user, String password, AsyncCallback callback);

    @SuppressWarnings("rawtypes")
    void loginAdmin(String user, String password, AsyncCallback callback);

    @SuppressWarnings("rawtypes")
    void logout(AsyncCallback callback);

    void addHotel(HotelP hotel, AsyncCallback<ReturnPersist> callback);

    void removeHotel(HotelP hotel, AsyncCallback<ReturnPersist> callback);

    void addPerson(PersonP person, String password,
            AsyncCallback<ReturnPersist> callback);

    void removePerson(PersonP person, AsyncCallback<ReturnPersist> callback);

    void setRoles(String person, String hotel, List<String> roles,
            AsyncCallback<ReturnPersist> callback);

    void persistDict(DictType d, DictionaryP t,
            AsyncCallback<ReturnPersist> callback);

    void removeDict(DictType d, DictionaryP t,
            AsyncCallback<ReturnPersist> callback);

    void hotelOp(HotelOpType op, CommandParam p,
            AsyncCallback<ReturnPersist> callback);

    void hotelOp(CommandParam p, AsyncCallback<ReturnPersist> callback);

    void hotelOp(List<CommandParam> p,
            AsyncCallback<List<ReturnPersist>> callback);

    void persistResBookingReturn(BookingP dp, @SuppressWarnings("rawtypes") AsyncCallback callback);

    void getParam(AsyncCallback<Map<String, String>> callback);

    void clearHotelData(HotelP hotel, AsyncCallback<ReturnPersist> callback);

    void validatePersistHotel(PersistType t, HotelP hotel,
            AsyncCallback<ReturnPersist> callback);

    void validatePersistPerson(PersistType t, PersonP hotel,
            AsyncCallback<ReturnPersist> callback);

    void validateDictPersist(PersistType t, DictType da, DictionaryP a,
            AsyncCallback<ReturnPersist> callback);

}
