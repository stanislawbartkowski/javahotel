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
package com.gwthotel.admin.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.Person;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.admin.gae.entities.EPermissions;
import com.gwthotel.admin.gae.entities.EPerson;
import com.gwthotel.admin.gae.entities.ElemPerm;

public class HotelAdminGae implements IHotelAdmin {

    static {
        ObjectifyService.register(EPerson.class);
        ObjectifyService.register(EHotel.class);
        ObjectifyService.register(EPermissions.class);
    }

    @Override
    public List<Person> getListOfPersons() {
        List<EPerson> li = ofy().load().type(EPerson.class).list();
        List<Person> outList = new ArrayList<Person>();
        for (EPerson e : li) {
            Person p = new Person();
            DictUtil.toProp(p, e);
            outList.add(p);
        }
        return outList;
    }

    @Override
    public List<Hotel> getListOfHotels() {
        List<EHotel> li = ofy().load().type(EHotel.class).list();
        List<Hotel> outList = new ArrayList<Hotel>();
        for (EHotel e : li) {
            Hotel p = new Hotel();
            DictUtil.toProp(p, e);
            outList.add(p);
        }
        return outList;
    }

    private EPerson findPerson(String name) {
        LoadResult<EPerson> p = ofy().load().type(EPerson.class)
                .filter("name ==", name).first();
        if (p == null) {
            return null;
        }
        return p.get();

    }

    private EPerson findPerson(Long id) {
        LoadResult<EPerson> e = ofy().load().type(EPerson.class).id(id);
        if (e == null)
            return null;
        return e.get();
    }

    private EHotel findHotel(String name) {
        LoadResult<EHotel> p = ofy().load().type(EHotel.class).filter("name ==", name)
                .first();
        if (p == null) {
            return null;
        }
        return p.get();
    }

    private EHotel findHotel(Long id) {
        LoadResult<EHotel> e = ofy().load().type(EHotel.class).id(id);
        if (e == null)
            return null;
        return e.get();
    }

    private EPermissions getPerm() {
        LoadResult<EPermissions> e = ofy().load().type(EPermissions.class).first();
        if (e == null)
            return new EPermissions();
        if (e.get() == null)
            return new EPermissions();
        return e.get();
    }

    @Override
    public List<HotelRoles> getListOfRolesForPerson(String person) {
        List<HotelRoles> li = new ArrayList<HotelRoles>();
        EPerson pe = findPerson(person);
        if (pe == null)
            return null;
        EPermissions eperm = getPerm();
        Map<Long, List<String>> ro = new HashMap<Long, List<String>>();
        for (ElemPerm e : eperm.geteList()) {
            if (!e.getPersonId().equals(pe.getId()))
                continue;
            List<String> rol = ro.get(e.getHotelId());
            if (rol == null) {
                rol = new ArrayList<String>();
                ro.put(e.getHotelId(), rol);
            }
            rol.add(e.getPerm());
        }
        for (Long hId : ro.keySet()) {
            EHotel eho = findHotel(hId);
            if (eho == null) // TODO: not expected
                continue;
            Hotel ho = new Hotel();
            DictUtil.toProp(ho, eho);
            HotelRoles rol = new HotelRoles(ho);
            rol.getRoles().addAll(ro.get(hId));
            li.add(rol);
        }
        return li;
    }

    @Override
    public List<HotelRoles> getListOfRolesForHotel(String hotel) {
        List<HotelRoles> li = new ArrayList<HotelRoles>();
        EHotel ho = findHotel(hotel);
        if (ho == null)
            return null;
        EPermissions eperm = getPerm();
        Map<Long, List<String>> ro = new HashMap<Long, List<String>>();
        for (ElemPerm e : eperm.geteList()) {
            if (!ho.getId().equals(e.getHotelId()))
                continue;
            List<String> rol = ro.get(e.getPersonId());
            if (rol == null) {
                rol = new ArrayList<String>();
                ro.put(e.getPersonId(), rol);
            }
            rol.add(e.getPerm());
        }
        for (Long hId : ro.keySet()) {
            EPerson epe = findPerson(hId);
            if (epe == null) // TODO: not expected
                continue;
            Person pe = new Person();
            DictUtil.toProp(pe, epe);
            HotelRoles rol = new HotelRoles(pe);
            rol.getRoles().addAll(ro.get(hId));
            li.add(rol);
        }
        return li;
    }

    private void removeHotelPerm(EPermissions eperm, Long hotelId) {
        List<ElemPerm> remList = new ArrayList<ElemPerm>();
        for (ElemPerm p : eperm.geteList()) {
            if (p.getHotelId().equals(hotelId)) {
                remList.add(p);
            }
        }
        eperm.geteList().removeAll(remList);
    }

    private void removePersonPerm(EPermissions eperm, Long personId) {
        List<ElemPerm> remList = new ArrayList<ElemPerm>();
        for (ElemPerm p : eperm.geteList()) {
            if (p.getPersonId().equals(personId)) {
                remList.add(p);
            }
        }
        eperm.geteList().removeAll(remList);
    }

    private void addroles(EPermissions eperm, HotelRoles ro, Long hotelId,
            Long personId) {
        for (String s : ro.getRoles()) {
            ElemPerm ele = new ElemPerm();
            ele.setHotelId(hotelId);
            ele.setPersonId(personId);
            ele.setPerm(s);
            eperm.geteList().add(ele);
        }

    }

    @Override
    public void addOrModifHotel(final Hotel hotel, final List<HotelRoles> roles) {
        EHotel eho = findHotel(hotel.getName());
        if (eho == null)
            eho = new EHotel();
        final EHotel ho = eho;
        DictUtil.toEDict(ho, hotel);
        final List<Long> personIds = new ArrayList<Long>();
        final List<HotelRoles> aroles = new ArrayList<HotelRoles>();

        for (HotelRoles ro : roles) {
            Person pe = (Person) ro.getObject();
            EPerson epe = findPerson(pe.getName());
            if (epe == null)
                continue; // TODO: not expected
            personIds.add(epe.getId());
            aroles.add(ro);
        }
        final EPermissions eperm = getPerm();
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().save().entity((EHotel) ho).now();
                removeHotelPerm(eperm, ho.getId());
                for (int i = 0; i < personIds.size(); i++) {
                    addroles(eperm, aroles.get(i), ho.getId(), personIds.get(i));
                }
                ofy().save().entity((EPermissions) eperm).now();
            }
        });
    }

    @Override
    public void addOrModifPerson(final Person person,
            final List<HotelRoles> roles) {
        EPerson epo = findPerson(person.getName());
        if (epo == null)
            epo = new EPerson();
        final EPerson po = epo;
        DictUtil.toEDict(po, person);
        final List<Long> hotelIds = new ArrayList<Long>();
        final List<HotelRoles> aroles = new ArrayList<HotelRoles>();

        for (HotelRoles ro : roles) {
            Hotel ho = (Hotel) ro.getObject();
            EHotel eho = findHotel(ho.getName());
            if (eho == null)
                continue; // TODO: not expected
            hotelIds.add(eho.getId());
            aroles.add(ro);
        }
        final EPermissions eperm = getPerm();
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().save().entity((EPerson) po).now();
                removePersonPerm(eperm, po.getId());
                for (int i = 0; i < hotelIds.size(); i++) {
                    addroles(eperm, aroles.get(i), hotelIds.get(i), po.getId());
                }
                ofy().save().entity((EPermissions) eperm).now();
            }
        });

    }

    @Override
    public void changePasswordForPerson(final String person,
            final String password) {
        final EPerson epe = findPerson(person);
        if (epe == null) // TODO: unexpected
            return;
        epe.setPassword(password);
        // transaction not necessary here, one update only
        ofy().save().entity((EPerson) epe).now();
    }

    @Override
    public boolean validatePasswordForPerson(String person, String password) {
        EPerson epe = findPerson(person);
        if (epe == null) // TODO: unexpected
            return false;
        if (epe.getPassword() == null)
            return false;
        return epe.getPassword().equals(password);
    }

    @Override
    public void clearAll() {
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().delete().type(EHotel.class);
                ofy().delete().type(EPerson.class);
                ofy().delete().type(EPermissions.class);
            }
        });

    }

    @Override
    public void removePerson(final String person) {
        final EPerson pe = findPerson(person);
        if (pe == null) // TODO: unexpected
            return;
        final EPermissions eperm = getPerm();
        ofy().transact(new VoidWork() {
            public void vrun() {
                removePersonPerm(eperm, pe.getId());
                ofy().delete().entity(pe);
                ofy().save().entity((EPermissions) eperm).now();
            }
        });

    }

    @Override
    public void removeHotel(final String hotel) {
        final EHotel ho = findHotel(hotel);
        if (ho == null) // TODO: unexpected
            return;
        final EPermissions eperm = getPerm();
        ofy().transact(new VoidWork() {
            public void vrun() {
                removeHotelPerm(eperm, ho.getId());
                ofy().delete().entity(ho);
                ofy().save().entity((EPermissions) eperm).now();
            }
        });

    }

    @Override
    public String getPassword(String person) {
        EPerson epe = findPerson(person);
        if (epe == null) // TODO: unexpected
            return null;
        return epe.getPassword();
    }

}
