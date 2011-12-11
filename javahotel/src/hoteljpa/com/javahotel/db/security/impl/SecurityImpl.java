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
package com.javahotel.db.security.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.security.auth.login.LoginException;

import com.javahotel.db.authentication.impl.AuthLoginUser;
import com.javahotel.db.hotelbase.jpa.AfterLoadActionFactory;
import com.javahotel.db.hoteldb.HotelStore;
import com.javahotel.db.security.login.HotelLoginFactory;
import com.javahotel.db.start.IStart;
import com.javahotel.dbjpa.ejb3.IAfterBeforeLoadAction;
import com.javahotel.dbjpa.ejb3.JpaManagerData;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.ELog;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.dbutil.container.ContainerInfo;
import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.dbutil.prop.IGetPropertiesFactory;
import com.javahotel.remoteinterfaces.HotelServerType;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.ISecurity;
import com.javahotel.remoteinterfaces.ISecurityLocal;
import com.javahotel.remoteinterfaces.PasswordT;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.security.login.HotelLoginP;
import com.javahotel.security.login.IHotelLogin;
import com.javahotel.security.login.IHotelLoginJDBC;

@Stateless(mappedName = "securityEJB")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SecurityImpl implements ISecurity, ISecurityLocal {

    @EJB
    private IStart w;
    private IHotelLogin lo;

    public void setIStart(IStart w) {
        this.w = w;
    }

    @PostConstruct
    public void init() {
        w.start();
        IHotelLoginJDBC i = new AuthLoginUser();
        lo = HotelLoginFactory.getHotelLogin();
        lo.setLoginJDBC(i);
    }

    public SecurityImpl() {
    }

    @Override
    public void logoutSession(final SessionT sessionId) {
        try {
            HotelLoginP p = SecurityFilter.isLogged(sessionId, false);
            p.logout();
            LoginSession.removeLogin(sessionId.getName());
            String logs = ELog.logoutS(sessionId.getName(), p.getUser());
            HLog.getLo().warning(logs);
        } catch (LoginException ex) {
            HLog.getLo().log(Level.SEVERE, "", ex);
            throw new HotelException(ex);
        }
    }

    @Override
    public SessionT loginSession(final String sessionId, final String name,
            final PasswordT password) {
        try {
            HotelLoginP hp = lo.loginuser(name, password, GetProp.getConfP());
            LoginSession.addLogin(sessionId, hp);
        } catch (HotelException e) {
            ELog.loginAdminFailure(sessionId, name, e);
            throw e;
        }
        String logs = ELog.loginAdminSuccessS(sessionId, name);
        HLog.getLo().info(logs);
        return new SessionT(sessionId);
    }

    public SessionT loginadminSession(final String sessionId,
            final String name, final PasswordT password) {

        try {
            HotelLoginP hp = lo.loginadmin(name, password, GetProp.getConfP());
            LoginSession.addLogin(sessionId, hp);
        } catch (HotelException e) {
            ELog.loginAdminFailure(sessionId, name, e);
            throw e;
        }
        String logs = ELog.loginAdminSuccessS(sessionId, name);
        HLog.getLo().warning(logs);
        return new SessionT(sessionId);
    }

    @Override
    public boolean isValidSession(final SessionT sessionId) {
        HotelLoginP h = LoginSession.getLogin(sessionId.getName());
        return h != null;
    }

    @Override
    public boolean isAdminSession(final SessionT sessionId) {
        HotelLoginP p = SecurityFilter.isLogged(sessionId, false);
        return p.isAdmin();
    }

    @Override
    public void setDatabaseDefinition(final SessionT sessionT,
            final Map<String, String> prop) {
        // initialize
        String na = GetProp.getConfP().get(IMess.PUPREFIX);
        JpaManagerData.setPuName(na);
        JpaManagerData.setLog(HLog.getL());
        JpaManagerData.setDataBaseMapping(GetProp.getSeID(), IMess.PUSECURITY);

        HotelLoginP hp = SecurityFilter.isLogged(sessionT, true);
        String logs = ELog.cleardefS(sessionT.getName(), hp.getUser());
        HLog.getLo().info(logs);
        JpaManagerData.clearAll();
        HotelStore.invalidateCache();
        IGetPropertiesFactory pa = new IGetPropertiesFactory() {

            @Override
            public Map<String, String> getPersistProperties(GetLogger log) {
                return prop;
            }
        };
        HotelServerType hType = ContainerInfo.getContainerType();
        JpaManagerData.setGetPropFactory(pa, hType == HotelServerType.APPENGINE);
        IAfterBeforeLoadAction ia = AfterLoadActionFactory.getAction();
        JpaManagerData.setIA(ia);
    }

    @Override
    public List<HotelT> getListHotels(final SessionT sessionT) {
        HotelLoginP hp = SecurityFilter.isLogged(sessionT, false);
        return hp.getHotels();
    }

    @Override
    public List<String> getListRoles(final SessionT sessionT,
            final HotelT ho) {
        HotelLoginP hp = SecurityFilter.isLogged(sessionT, false);
        return hp.getRoles(ho);
    }

    @Override
    public void setNewProperties(final SessionT sessionT,
            final Map<String, String> prop) {
        GetProp.setNewProp(prop);
    }
    // @PreDestroy
    // public void destroy() {
    // HLog.getL().info("Destroy secutity bean");
    // }
}
