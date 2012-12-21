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

    // Authentication
    void loginUser(String user, String password);

    void loginAdmin(String user, String password);

    void logout();

    // Hotel/User administration

    ReturnPersist clearHotelData(HotelP hotel);

    ReturnPersist addHotel(HotelP hotel);

    ReturnPersist removeHotel(HotelP hotel);

    ReturnPersist addPerson(PersonP person, String password);

    ReturnPersist removePerson(PersonP person);

    ReturnPersist setRoles(String person, String hotel, List<String> roles);

    ReturnPersist validatePersistHotel(PersistType t, HotelP hotel);

    ReturnPersist validatePersistPerson(PersistType t, PersonP person);

    // Hotel management

    AbstractTo getOne(RType r, CommandParam p);

    ReturnPersist persistDict(DictType d, DictionaryP t);

    ReturnPersist removeDict(DictType d, DictionaryP t);

    ReturnPersist hotelOp(HotelOpType op, CommandParam p);

    ReturnPersist hotelOp(CommandParam p);

    ReturnPersist validateDictPersist(PersistType t, DictType da, DictionaryP a);

    List<ReturnPersist> hotelOp(List<CommandParam> p);

    List<AbstractTo> getList(RType r, CommandParam p);

    // Booking
    ReturnPersist persistResBookingReturn(BookingP dp);

    // Info

    Map<String, String> getParam();

}
