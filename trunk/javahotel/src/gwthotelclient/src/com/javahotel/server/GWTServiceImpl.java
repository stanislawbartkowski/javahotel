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
@SuppressWarnings("serial")
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
    public ReturnPersist addHotel(final HotelP hotel) {
        return HotelHelper.addHotel(getSessionId(), hotel);
    }

    @Override
    public ReturnPersist removeHotel(final HotelP hotel) {
        return HotelHelper.removeHotel(getSessionId(), hotel);
    }

    @Override
    public ReturnPersist addPerson(final PersonP person, final String password) {
        return HotelHelper.addPerson(getSessionId(), person, password);
    }

    @Override
    public ReturnPersist removePerson(PersonP person) {
        return HotelHelper.removePerson(getSessionId(), person);
    }

    @Override
    public ReturnPersist setRoles(final String person, final String hotel,
            final List<String> roles) {
        return HotelHelper.setRoles(getSessionId(), person, hotel, roles);
    }

    @Override
    public ReturnPersist persistDict(final DictType d, final DictionaryP t) {
        return HotelHelper.persistDict(getSessionId(), d, t);
    }

    @Override
    public ReturnPersist removeDict(final DictType d, final DictionaryP t) {
        return HotelHelper.removeDict(getSessionId(), d, t);

    }

    @Override
    public ReturnPersist hotelOp(final HotelOpType op, final CommandParam p) {
        return HotelHelper.hotelOp(getSessionId(), op, p);
    }

    @Override
    public ReturnPersist persistResBookingReturn(final BookingP dp) {
        return HotelHelper.persistResBookingReturn(getSessionId(), dp);
    }

    @Override
    public Map<String, String> getParam() {
        return HotelHelper.getParam();
    }

    @Override
    public ReturnPersist clearHotelData(HotelP hotel) {
        return HotelHelper.clearHotelData(getSessionId(), hotel);
    }

    @Override
    public ReturnPersist validatePersistHotel(PersistType t, HotelP hotel) {
        return HotelHelper.validateHotelPersist(getSessionId(), t, hotel);
    }

    @Override
    public ReturnPersist validatePersistPerson(PersistType t, PersonP person) {
        return HotelHelper.validatePersonPersist(getSessionId(), t, person);
    }

    @Override
    public ReturnPersist validateDictPersist(PersistType t, DictType da,
            DictionaryP a) {
        return HotelHelper.validateDictPersist(getSessionId(), t, da, a);

    }

    @Override
    public ReturnPersist hotelOp(CommandParam p) {
        return HotelHelper.hotelOp(getSessionId(), p);
    }

    @Override
    public List<ReturnPersist> hotelOp(List<CommandParam> p) {
        return HotelHelper.hotelOp(getSessionId(), p);
    }
}
