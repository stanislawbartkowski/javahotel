/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwthotel.admin.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.admin.gae.entities.EInstance;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.JythonUIFatal;

public class HotelInstanceImpl implements IAppInstanceHotel {

    static {
        ObjectifyService.register(EInstance.class);
    }

    private final IGetLogMess lMess;
    private static final Logger log = Logger.getLogger(HotelInstanceImpl.class
            .getName());

    private void severe(String errorC, String messId, String... pars) {
        String mess = lMess.getMess(errorC, messId, pars);
        log.severe(mess);
        throw new JythonUIFatal(mess);

    }

    @Inject
    public HotelInstanceImpl(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        this.lMess = lMess;
    }

    @Override
    public AppInstanceId getInstanceId(String instanceName, String userName) {
        LoadResult<EInstance> p = ofy().load().type(EInstance.class)
                .filter("name ==", instanceName).first();
        EInstance e = null;
        if (p.now() == null) {
            // test if default instances
            if (instanceName.equals(IHotelConsts.INSTANCETEST)
                    || instanceName.equals(IHotelConsts.INSTANCEDEFAULT)) {
                e = new EInstance();
                e.setName(instanceName);
                ofy().save().entity((EInstance) e).now();
            } else {
                severe(IHError.HERROR012, IHMess.INSTANCENOTFOUND, instanceName);
            }
        } else
            e = p.now();
        AppInstanceId i = new AppInstanceId();
        i.setInstanceName(instanceName);
        i.setId(e.getId());
        i.setPerson(userName);
        return i;
    }

    @Override
    public HotelId getHotelId(AppInstanceId instanceId, String hotelName,
            String userName) {
        EInstance ei = DictUtil.findI(lMess, instanceId);
        LoadResult<EHotel> p = ofy().load().type(EHotel.class).ancestor(ei)
                .filter("name ==", hotelName).first();
        if (p.now() == null) {
            severe(IHError.HERROR013, IHMess.HOTELCANNOTBEFOUND, hotelName);
        }
        HotelId ho = new HotelId();
        ho.setHotel(hotelName);
        ho.setId(p.now().getId());
        ho.setInstanceId(instanceId);
        ho.setUserName(userName);
        return ho;
    }

}
