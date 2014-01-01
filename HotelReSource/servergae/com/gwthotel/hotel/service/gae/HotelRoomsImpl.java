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
package com.gwthotel.hotel.service.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.service.gae.crud.CrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.EHotelRoom;
import com.gwthotel.hotel.service.gae.entities.EHotelRoomServices;
import com.gwthotel.hotel.service.gae.entities.EHotelServices;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class HotelRoomsImpl extends CrudGaeAbstract<HotelRoom, EHotelRoom>
        implements IHotelRooms {

    static {
        ObjectifyService.register(EHotelRoom.class);
        ObjectifyService.register(EHotelRoomServices.class);
    }

    @Inject
    public HotelRoomsImpl(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess,
            IHotelObjectGenSym iGen) {
        super(lMess, EHotelRoom.class, HotelObjects.ROOM, iGen);
    }

    @Override
    public void setRoomServices(HotelId hotel, final String roomName,
            List<String> services) {
        final List<EHotelServices> sList = new ArrayList<EHotelServices>();
        final EHotel ho = findEHotel(hotel);
        for (String s : services) {
            LoadResult<EHotelServices> p = ofy().load()
                    .type(EHotelServices.class).ancestor(ho)
                    .filter("name == ", s).first();
            // TODO: p != null
            sList.add(p.now());
        }
        final List<EHotelRoomServices> li = ofy().load()
                .type(EHotelRoomServices.class).ancestor(ho)
                .filter("roomName == ", roomName).list();
        ofy().transact(new VoidWork() {

            @Override
            public void vrun() {
                ofy().delete().entities(li).now();
                for (EHotelServices e : sList) {
                    EHotelRoomServices elem = new EHotelRoomServices();
                    elem.setHotel(ho);
                    elem.setRoomName(roomName);
                    elem.setService(e);
                    ofy().save().entity(elem).now();
                }
            }
        });

    }

    @Override
    public List<HotelServices> getRoomServices(HotelId hotel, String roomName) {
        EHotel ho = findEHotel(hotel);
        List<HotelServices> outList = new ArrayList<HotelServices>();
        List<EHotelRoomServices> li = ofy().load()
                .type(EHotelRoomServices.class).ancestor(ho)
                .filter("roomName == ", roomName).list();
        for (EHotelRoomServices e : li) {
            outList.add(DictUtil.toS(e.getService()));
        }
        return outList;
    }

    @Override
    protected HotelRoom constructProp(EHotel ho, EHotelRoom e) {
        HotelRoom r = new HotelRoom();
        r.setNoPersons(e.getNoPersons());
        return r;
    }

    @Override
    protected EHotelRoom constructE() {
        return new EHotelRoom();
    }

    @Override
    protected void toE(EHotel ho, EHotelRoom e, HotelRoom t) {
        e.setNoPersons(t.getNoPersons());
    }

    @Override
    protected void beforeDelete(DeleteItem i, EHotel ho, EHotelRoom elem) {
        if (elem != null) {
            i.sList = ofy().load().type(EHotelRoomServices.class).ancestor(ho)
                    .filter("roomName == ", elem.getName()).list();
        } else {
            i.readAllRoomServices(ho);
        }

    }
}