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

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.service.gae.crud.HotelCrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.EHotelRoom;
import com.gwthotel.hotel.service.gae.entities.EHotelRoomServices;
import com.gwthotel.hotel.service.gae.entities.EHotelServices;
import com.gwthotel.hotel.services.HotelServices;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jythonui.server.crud.ICrudObjectGenSym;

public class HotelRoomsImpl extends HotelCrudGaeAbstract<HotelRoom, EHotelRoom>
        implements IHotelRooms {

    static {
        ObjectifyService.register(EHotelRoom.class);
        ObjectifyService.register(EHotelRoomServices.class);
    }

    @Inject
    public HotelRoomsImpl(ICrudObjectGenSym iGen) {
        super(EHotelRoom.class, HotelObjects.ROOM, iGen);
    }

    @Override
    public void setRoomServices(OObjectId hotel, final String roomName,
            List<String> services) {
        final List<EHotelServices> sList = new ArrayList<EHotelServices>();
        final EObject ho = findEHotel(hotel);
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
    public List<HotelServices> getRoomServices(OObjectId hotel, String roomName) {
        EObject ho = findEHotel(hotel);
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
    protected HotelRoom constructProp(EObject ho, EHotelRoom e) {
        HotelRoom r = new HotelRoom();
        r.setNoPersons(e.getNoPersons());
        r.setNoChildren(e.getNoChildren());
        r.setNoExtraBeds(e.getNoExtraBeds());
        return r;
    }

//    @Override
//    protected EHotelRoom constructE() {
//        return new EHotelRoom();
//    }

    @Override
    protected void toE(EObject ho, EHotelRoom e, HotelRoom t) {
        e.setNoPersons(t.getNoPersons());
        e.setNoChildren(t.getNoChildren());
        e.setNoExtraBeds(t.getNoExtraBeds());
    }

    @Override
    protected void beforeDelete(DeleteItem i, EObject ho, EHotelRoom elem) {
        if (elem != null) {
            i.sList = ofy().load().type(EHotelRoomServices.class).ancestor(ho)
                    .filter("roomName == ", elem.getName()).list();
        } else {
            i.readAllRoomServices(ho);
        }

    }
}