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
package com.gwthotel.hotelejb.rooms;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.hotelejb.AbstractHotelEJB;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.defa.GuiceInterceptor;

@Stateless
@EJB(name = IHotelConsts.HOTELROOMSJNDI, beanInterface = IHotelRooms.class)
@Remote
@Interceptors(value = { GuiceInterceptor.class })
public class HotelRoomsEJB extends AbstractHotelEJB<HotelRoom> implements
        IHotelRooms {

    private IHotelRooms iRooms;

    @Inject
    public void injectHotelServices(IHotelRooms injectedServices) {
        service = injectedServices;
        this.iRooms = injectedServices;
    }

    @Override
    public void setRoomServices(HotelId hotel, String roomName,
            List<String> services) {
        iRooms.setRoomServices(hotel, roomName, services);
    }

    @Override
    public List<HotelServices> getRoomServices(HotelId hotel, String roomName) {
        return iRooms.getRoomServices(hotel, roomName);
    }

}
