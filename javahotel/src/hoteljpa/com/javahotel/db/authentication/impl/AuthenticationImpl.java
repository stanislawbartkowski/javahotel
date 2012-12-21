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
package com.javahotel.db.authentication.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IAuthentication;
import com.javahotel.remoteinterfaces.IAuthenticationLocal;
import com.javahotel.remoteinterfaces.PasswordT;
import com.javahotel.remoteinterfaces.SessionT;

@Stateless(mappedName = "authenticationEJB")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AuthenticationImpl implements IAuthentication,
        IAuthenticationLocal {

    public AuthenticationImpl() {
    }

    @Override
    public ReturnPersist persistHotel(final SessionT sessionId,
            final HotelT hotel, final String description, final String database) {
        PersistHotelCommand p = new PersistHotelCommand(sessionId, hotel,
                description, database);
        p.run();
        return p.getRet();
    }

    @Override
    public List<HotelP> getHotelList(final SessionT sessionId) {
        return GetList.getHotelList(sessionId);
    }

    @Override
    public ReturnPersist removeHotel(final SessionT sessionId,
            final HotelT hotel) {
        RemoveHotelCommand p = new RemoveHotelCommand(sessionId, hotel);
        p.run();
        return p.getRet();
    }

    @Override
    public ReturnPersist removePerson(final SessionT sessionId,
            final String person) {
        RemovePersonCommand p = new RemovePersonCommand(sessionId, person);
        p.run();
        return p.getRet();
    }

    @Override
    public ReturnPersist persistPersonHotel(final SessionT sessionId,
            final String person, final HotelT hotel,
            final List<String> principals) {
        PersistPersonHotelCommand p = new PersistPersonHotelCommand(sessionId,
                person, hotel, principals);
        p.run();
        return p.getRet();
    }

    @Override
    public ReturnPersist persistPerson(final SessionT sessionId,
            final String person, final PasswordT password) {
        PersistPersonCommand p = new PersistPersonCommand(sessionId, person,
                password);
        p.run();
        return p.getRet();
    }

    @Override
    public ReturnPersist clearAuthBase(final SessionT sessionId) {
        ClearAuthBaseCommand p = new ClearAuthBaseCommand(sessionId);
        p.run();
        return p.getRet();
    }

    @Override
    public List<PersonP> getPersonList(final SessionT sessionId) {
        return GetList.getPersonList(sessionId);
    }

    @Override
    public List<String> getPersonHotelRoles(final SessionT sessionId,
            final String person, final HotelT hotel) {
        return GetList.getPersonHotelRoles(sessionId, person, hotel);
    }

    @Override
    public ReturnPersist validatePersistHotel(SessionT sessionId,
            PersistType t, HotelP ho) {
        TestPersist te = new TestPersist(sessionId, t, ho);
        te.command();
        return te.getRet();
    }

    @Override
    public ReturnPersist validatePersistPerson(SessionT sessionId,
            PersistType t, PersonP pe) {
        TestPersist te = new TestPersist(sessionId, t, pe);
        te.command();
        return te.getRet();
    }
}
