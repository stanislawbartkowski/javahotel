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
import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.webhelper.HotelHelper;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GWTServiceImpl extends RemoteServiceServlet implements GWTService {

	private static GetLogger lo = new GetLogger("com.javahotel.gwtserviceimpl");
	
    private String getSessionId() {

        HttpServletRequest a = getThreadLocalRequest();
        String s = a.getSession().getId();
        lo.getL().info("session:" + s);
        return s;
    }

    @Override
    public List<AbstractTo> getList(final RType r, final CommandParam p) {
        return HotelHelper.getList(getSessionId(), r, p);
    }

    @Override
    public AbstractTo getOne(final RType r, final CommandParam p) {
        return HotelHelper.getOne(getSessionId(), r, p);
    }

    @Override
    public void loginAdmin(final String user, final String password) {
        HotelHelper.loginAdmin(getSessionId(), user, password);
    }

    @Override
    public void loginUser(final String user, final String password) {
        HotelHelper.loginUser(getSessionId(), user, password);
    }

    @Override
    public void logout() {
        HotelHelper.logOut(getSessionId());
    }

    @Override
    public void addHotel(final HotelP hotel) {
        HotelHelper.addHotel(getSessionId(), hotel);
    }

    @Override
    public void removeHotel(final HotelP hotel) {
        HotelHelper.removeHotel(getSessionId(), hotel);
    }

    @Override
    public void addPerson(final PersonP person, final String password) {
        HotelHelper.addPerson(getSessionId(), person, password);
    }

    @Override
    public void removePerson(PersonP person) {
        HotelHelper.removePerson(getSessionId(), person);
    }

    @Override
    public void setRoles(final String person, final String hotel,
            final List<String> roles) {
        HotelHelper.setRoles(getSessionId(), person, hotel, roles);
    }

    @Override
    public void persistDict(final DictType d, final DictionaryP t) {
        HotelHelper.persistDict(getSessionId(), d, t);
    }

    @Override
    public ReturnPersist persistDictRet(final DictType d, final DictionaryP t) {
        return HotelHelper.persistDictRet(getSessionId(), d, t);
    }

    @Override
    public void removeDict(final DictType d, final DictionaryP t) {
        HotelHelper.removeDict(getSessionId(), d, t);

    }

    @Override
    public void hotelOp(final HotelOpType op, final CommandParam p) {
        HotelHelper.hotelOp(getSessionId(), op, p);
    }

    @Override
    public ReturnPersist persistResBookingReturn(final BookingP dp) {
        return HotelHelper.persistResBookingReturn(getSessionId(), dp);
    }

    @Override
    public ReturnPersist hotelOpRet(final HotelOpType op, final CommandParam p) {
        return HotelHelper.hotelOpRet(getSessionId(), op, p);
    }

    @Override
    public Map<String, String> getParam() {
        return HotelHelper.getParam();
    }

    @Override
    public void clearHotelData(HotelP hotel) {
        HotelHelper.clearHotelData(getSessionId(), hotel);
    }

    @Override
    public ReturnPersist testPersistHotel(PersistType t, HotelP hotel) {
        return HotelHelper.testHotelPersist(getSessionId(), t, hotel);
    }

    @Override
    public ReturnPersist testPersistPerson(PersistType t, PersonP person) {
        return HotelHelper.testPersonPersist(getSessionId(), t, person);
    }

    @Override
    public ReturnPersist testDictPersist(PersistType t, DictType da,
            DictionaryP a) {
        return HotelHelper.testDictPersist(getSessionId(), t, da, a);

    }

    @Override
    public ReturnPersist hotelOpRet(CommandParam p) {
        return HotelHelper.hotelOpRet(getSessionId(), p);
    }

    @Override
    public List<ReturnPersist> hotelOpRet(List<CommandParam> p) {
        return HotelHelper.hotelOpRet(getSessionId(), p);
    }
}
