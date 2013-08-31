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
package com.gwthotel.admin.jpa;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.admin.jpa.entities.EHotel;
import com.gwthotel.admin.jpa.entities.EInstance;
import com.gwthotel.hotel.HUtils;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.JythonUIFatal;

class HotelAdminInstance implements IAppInstanceHotel {

    private final ITransactionContextFactory iC;
    private static final Logger log = Logger.getLogger(HotelAdminInstance.class
            .getName());
    private final IGetLogMess lMess;

    HotelAdminInstance(ITransactionContextFactory iC, IGetLogMess lMess) {
        this.iC = iC;
        this.lMess = lMess;
    }

    private abstract class doTransaction extends JpaTransaction {

        private doTransaction() {
            super(iC);
        }
    }

    private class getInstance extends doTransaction {

        private final String instanceName;
        private final String userName;
        Long id;

        getInstance(String instanceName, String userName) {
            this.instanceName = instanceName;
            this.userName = userName;
        }

        @Override
        protected void dosth(EntityManager em) {
            EInstance insta = null;
            Query q = em.createNamedQuery("findInstanceByName");
            q.setParameter(1, instanceName);
            try {
                insta = (EInstance) q.getSingleResult();
            } catch (NoResultException e) {
                // test if default instances
                if (instanceName.equals(IHotelConsts.INSTANCETEST)
                        || instanceName.equals(IHotelConsts.INSTANCEDEFAULT)) {
                    insta = new EInstance();
                    insta.setName(instanceName);
                    HUtils.setCreateModif(userName, insta, true);
                    em.persist(insta);
                    makekeys();
                    log.info(lMess.getMessN(
                            IHMess.DEFAULTINSTANCEHASBEENCREATED, instanceName));
                } else
                    throw (e);
            }
            id = insta.getId();
            if (id == null) {
                String mess = lMess.getMess(IHError.HERROR011,
                        IHMess.INSTANCEIDCANNOTNENULLHERE);
                log.severe(mess);
                throw new JythonUIFatal(mess);
            }

        }
    }

    @Override
    public AppInstanceId getInstanceId(String instanceName, String userName) {
        getInstance comm = new getInstance(instanceName, userName);
        comm.executeTran();
        AppInstanceId id = new AppInstanceId();
        id.setId(comm.id);
        id.setInstanceName(instanceName);
        id.setPerson(userName);
        return id;
    }

    private class getHotelId extends doTransaction {

        private final AppInstanceId instanceId;
        private final String hotelName;
        Long id;

        getHotelId(AppInstanceId instanceId, String hotelName) {
            this.instanceId = instanceId;
            this.hotelName = hotelName;
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = em.createNamedQuery("findHotelByName");
            q.setParameter(1, instanceId.getId());
            q.setParameter(2, hotelName);
            EHotel h = (EHotel) q.getSingleResult();
            // do not catch exception, not expected here
            id = h.getId();
        }

    }

    @Override
    public HotelId getHotelId(AppInstanceId instanceId, String hotelName,
            String userName) {
        getHotelId comm = new getHotelId(instanceId, hotelName);
        comm.executeTran();
        HotelId h = new HotelId();
        h.setHotel(hotelName);
        h.setId(comm.id);
        h.setInstanceId(instanceId);
        h.setUserName(userName);
        return h;
    }

}
