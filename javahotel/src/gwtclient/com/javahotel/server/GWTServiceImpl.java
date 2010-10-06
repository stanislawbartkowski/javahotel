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
package com.javahotel.server;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.javahotel.client.GWTService;
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
import com.javahotel.webhelper.HotelHelper;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GWTServiceImpl extends RemoteServiceServlet implements GWTService {

    private String getSessionId() {

        HttpServletRequest a = getThreadLocalRequest();
        String s = a.getSession().getId();
        return s;
    }

    public List<AbstractTo> getList(final RType r, final CommandParam p) {
        return HotelHelper.getList(getSessionId(), r, p);
    }

    public AbstractTo getOne(final RType r, final CommandParam p) {
        return HotelHelper.getOne(getSessionId(), r, p);
    }

    public void loginAdmin(final String user, final String password) {
        HotelHelper.loginAdmin(getSessionId(), user, password);
    }

    public void loginUser(final String user, final String password) {
        HotelHelper.loginUser(getSessionId(), user, password);
    }

    public void logout() {
        HotelHelper.logOut(getSessionId());
    }

    public void addHotel(final HotelP hotel) {
        HotelHelper.addHotel(getSessionId(), hotel);
    }

    public void removeHotel(final HotelP hotel) {
        HotelHelper.removeHotel(getSessionId(), hotel);
    }

    public void addPerson(final PersonP person, final String password) {
        HotelHelper.addPerson(getSessionId(), person, password);
    }

    public void removePerson(PersonP person) {
        HotelHelper.removePerson(getSessionId(), person);
    }

    public void setRoles(final String person, final String hotel,
            final List<String> roles) {
        HotelHelper.setRoles(getSessionId(), person, hotel, roles);
    }

    public void persistDict(final DictType d, final DictionaryP t) {
        HotelHelper.persistDict(getSessionId(), d, t);
    }

    public ReturnPersist persistDictRet(final DictType d, final DictionaryP t) {
        return HotelHelper.persistDictRet(getSessionId(), d, t);
    }

    public void removeDict(final DictType d, final DictionaryP t) {
        HotelHelper.removeDict(getSessionId(), d, t);

    }

    public void hotelOp(final HotelOpType op, final CommandParam p) {
        HotelHelper.hotelOp(getSessionId(), op, p);
    }

    public ReturnPersist persistResBookingReturn(final BookingP dp) {
        return HotelHelper.persistResBookingReturn(getSessionId(), dp);
    }

    public ReturnPersist hotelOpRet(final HotelOpType op, final CommandParam p) {
        return HotelHelper.hotelOpRet(getSessionId(), op, p);
    }

    public Map<String, String> getParam() {
        return HotelHelper.getParam();
    }

    public void clearHotelData(HotelP hotel) {
        HotelHelper.clearHotelData(getSessionId(), hotel);
    }

    public ReturnPersist testPersistHotel(PersistType t, HotelP hotel) {
        return HotelHelper.testHotelPersist(getSessionId(), t, hotel);
    }

    public ReturnPersist testPersistPerson(PersistType t, PersonP person) {
        return HotelHelper.testPersonPersist(getSessionId(), t, person);
    }

    public ReturnPersist testDictPersist(PersistType t, DictType da,
            DictionaryP a) {
        return HotelHelper.testDictPersist(getSessionId(), t, da, a);

    }

    public ReturnPersist hotelOpRet(CommandParam p) {
        return HotelHelper.hotelOpRet(getSessionId(), p);
    }

    public List<ReturnPersist> hotelOpRet(List<CommandParam> p) {
        return HotelHelper.hotelOpRet(getSessionId(), p);
    }
}
