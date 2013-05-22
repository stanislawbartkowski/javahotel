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
package com.gwthotel.hotel.server.service;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwthotel.admin.IGetHotelRoles;
import com.gwthotel.admin.IGetVatTaxes;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.hotel.IHotelGetName;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.resbundle.Mess;

public class H {

    private H() {
    }

    @Inject
    @Named(IHotelConsts.MESSNAMED)
    private static IGetLogMess lMess;

    @Inject
    private static IGetHotelRoles iRoles;

    @Inject
    private static IHotelAdmin iAdmin;

    @Inject
    private static Mess mMess;

    @Inject
    private static IHotelServices iServices;

    @Inject
    private static IGetVatTaxes iTaxes;

    @Inject
    private static IHotelGetName iGetHotelName;

    public static IGetLogMess getL() {
        return lMess;
    }

    public static IGetHotelRoles getHotelRoles() {
        return iRoles;
    }

    public static IHotelAdmin getHotelAdmin() {
        return iAdmin;
    }

    public static Mess getM() {
        return mMess;
    }

    public static IHotelServices getHotelServices() {
        return iServices;
    }

    public static IGetVatTaxes getVatTaxes() {
        return iTaxes;
    }

    public static String getHotelName(String token) {
        return iGetHotelName.getHotel(token);
    }

}
