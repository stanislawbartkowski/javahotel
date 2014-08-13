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
package com.jython.ui.server.gae.security.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.LoadResult;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.gae.security.entities.EDictionary;
import com.jython.ui.server.gae.security.entities.EInstance;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jythonui.server.RUtils;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.RMap;

public class EntUtil extends UtilHelper {

    private EntUtil() {
    }

    public static void toProp(RMap dest, EDictionary e) {
        dest.setName(e.getName());
        dest.setDescription(e.getDescription());
        dest.setId(e.getId());
        RUtils.retrieveCreateModif(dest, e);
    }

    public static void toEDict(EDictionary dest, RMap sou) {
        dest.setName(sou.getName());
        dest.setDescription(sou.getDescription());
    }

    public static EInstance findI(AppInstanceId i) {
        LoadResult<EInstance> p = ofy().load().type(EInstance.class)
                .id(i.getId());
        if (p == null) {
            String mess = L().getMess(IErrorCode.ERRORCODE87,
                    ILogMess.INSTANCEBYIDCANNOTBEFOUND,
                    Long.toString(i.getId()));
            errorLog(mess);
        }
        return p.now();
    }
    
    public static EObject findEOObject(OObjectId hotel) {
        EInstance eI = EntUtil.findI(hotel.getInstanceId());
        LoadResult<EObject> p = ofy().load().type(EObject.class).parent(eI)
                .id(hotel.getId());
        // System.out.println("Hotel " + hotel.getId() + " instance :" +
        // hotel.getInstanceId().getId() );
        if (p.now() == null) {
            String mess = L().getMess(IErrorCode.ERRORCODE116,
                    ILogMess.OBJECTBYIDNOTFOUND, hotel.getId().toString(), eI
                            .getId().toString());
            errorLog(mess);
        }
        return p.now();
    }
    
    public static <E> E findE(EObject eh, String name, Class<E> cl) {
        LoadResult<E> p = ofy().load().type(cl).ancestor(eh)
                .filter("name == ", name).first();
        if (p == null) {
            return null;
        }
        return p.now();
    }



}
