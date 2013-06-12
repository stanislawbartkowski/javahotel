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
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.Person;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.admin.gae.entities.EInstance;
import com.gwthotel.admin.gae.entities.EPermissions;
import com.gwthotel.admin.gae.entities.EPerson;
import com.gwthotel.admin.gae.entities.ElemPerm;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.JythonUIFatal;

public class HotelAdminGae implements IHotelAdmin {

    static {
        ObjectifyService.register(EPerson.class);
        ObjectifyService.register(EHotel.class);
        ObjectifyService.register(EPermissions.class);
    }

    private final IGetLogMess lMess;
    private static final Logger log = Logger.getLogger(HotelAdminGae.class
            .getName());

    private void severe(String errorC, String messId, String... pars) {
        String mess = lMess.getMess(errorC, messId, pars);
        log.severe(mess);
        throw new JythonUIFatal(mess);

    }

    @Inject
    public HotelAdminGae(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        this.lMess = lMess;
    }

    private EInstance getI(AppInstanceId i) {
        return DictUtil.findI(lMess, i);
    }

    @Override
    public List<Person> getListOfPersons(AppInstanceId i) {
        EInstance ei = getI(i);
        List<EPerson> li = ofy().load().type(EPerson.class).ancestor(ei).list();
        List<Person> outList = new ArrayList<Person>();
        for (EPerson e : li) {
            Person p = new Person();
            DictUtil.toProp(p, e);
            outList.add(p);
        }
        return outList;
    }

    @Override
    public List<Hotel> getListOfHotels(AppInstanceId i) {
        EInstance ei = getI(i);
        List<EHotel> li = ofy().load().type(EHotel.class).ancestor(ei).list();
        List<Hotel> outList = new ArrayList<Hotel>();
        for (EHotel e : li) {
            Hotel p = new Hotel();
            DictUtil.toProp(p, e);
            outList.add(p);
        }
        return outList;
    }

    private EPerson findPerson(EInstance ei, String name, boolean expected) {
        LoadResult<EPerson> p = ofy().load().type(EPerson.class).ancestor(ei)
                .filter("name ==", name).first();
        if (p.now() == null) {
            if (expected)
                severe(IHError.HERROR015,
                        IHMess.PERSONNAMEISEXPECTEDBUTNOTFOUND, name);
            return null;
        }
        return p.now();
    }

    private EPerson findPerson(EInstance ei, Long id) {
        LoadResult<EPerson> e = ofy().load().type(EPerson.class).parent(ei)
                .id(id);
        if (e.now() == null) {
            severe(IHError.HERROR016, IHMess.PERSONIDISEXPECTEDBUTNOTFOUND,
                    Long.toString(id));
            return null;
        }
        return e.now();
    }

    private EHotel findHotel(EInstance ei, String name, boolean expected) {
        LoadResult<EHotel> p = ofy().load().type(EHotel.class).ancestor(ei)
                .filter("name ==", name).first();
        if (p.now() == null) {
            if (expected)
                severe(IHError.HERROR017, IHMess.HOTELCANNOTBEFOUND, name);
            return null;
        }
        return p.now();
    }

    private EHotel findHotel(EInstance ei, Long id) {
        LoadResult<EHotel> e = ofy().load().type(EHotel.class).parent(ei)
                .id(id);
        if (e.now() == null) {
            severe(IHError.HERROR018, IHMess.HOTELBYIDNOTFOUND,
                    Long.toString(id));
            return null;
        }
        return e.now();
    }

    private EPermissions getPerm(EInstance ei) {
        LoadResult<EPermissions> e = ofy().load().type(EPermissions.class)
                .ancestor(ei).first();
        EPermissions ee;
        if (e.now() == null) {
            ee = new EPermissions();
            ee.setInstanceId(ei);
        } else
            ee = e.now();
        return ee;
    }

    @Override
    public List<HotelRoles> getListOfRolesForPerson(AppInstanceId i,
            String person) {
        EInstance ei = getI(i);
        List<HotelRoles> li = new ArrayList<HotelRoles>();
        EPerson pe = findPerson(ei, person, false);
        if (pe == null)
            return null;
        EPermissions eperm = getPerm(ei);
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
            EHotel eho = findHotel(ei, hId);
            Hotel ho = new Hotel();
            DictUtil.toProp(ho, eho);
            HotelRoles rol = new HotelRoles(ho);
            rol.getRoles().addAll(ro.get(hId));
            li.add(rol);
        }
        return li;
    }

    @Override
    public List<HotelRoles> getListOfRolesForHotel(AppInstanceId i, String hotel) {
        EInstance ei = getI(i);
        List<HotelRoles> li = new ArrayList<HotelRoles>();
        EHotel ho = findHotel(ei, hotel, false);
        if (ho == null)
            return null;
        EPermissions eperm = getPerm(ei);
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
            EPerson epe = findPerson(ei, hId);
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
    public void addOrModifHotel(AppInstanceId i, final Hotel hotel,
            final List<HotelRoles> roles) {
        EInstance ei = getI(i);
        EHotel eho = findHotel(ei, hotel.getName(), false);
        if (eho == null) {
            eho = new EHotel();
            eho.setInstanceId(ei);
        }
        final EHotel ho = eho;
        DictUtil.toEDict(ho, hotel);
        final List<Long> personIds = new ArrayList<Long>();
        final List<HotelRoles> aroles = new ArrayList<HotelRoles>();

        for (HotelRoles ro : roles) {
            Person pe = (Person) ro.getObject();
            EPerson epe = findPerson(getI(i), pe.getName(), true);
            personIds.add(epe.getId());
            aroles.add(ro);
        }
        final EPermissions eperm = getPerm(ei);
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().save().entity((EHotel) ho).now();
                removeHotelPerm(eperm, ho.getId());
                for (int i = 0; i < personIds.size(); i++) {
                    addroles(eperm, aroles.get(i), ho.getId(), personIds.get(i));
                }
                ofy().save().entity(eperm).now();
            }
        });
    }

    @Override
    public void addOrModifPerson(AppInstanceId i, final Person person,
            final List<HotelRoles> roles) {
        EInstance ei = getI(i);
        EPerson epo = findPerson(ei, person.getName(), false);
        if (epo == null) {
            epo = new EPerson();
            epo.setInstanceId(ei);
        }
        final EPerson po = epo;
        DictUtil.toEDict(po, person);
        final List<Long> hotelIds = new ArrayList<Long>();
        final List<HotelRoles> aroles = new ArrayList<HotelRoles>();

        for (HotelRoles ro : roles) {
            Hotel ho = (Hotel) ro.getObject();
            EHotel eho = findHotel(ei, ho.getName(), true);
            hotelIds.add(eho.getId());
            aroles.add(ro);
        }
        final EPermissions eperm = getPerm(ei);
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
    public void changePasswordForPerson(AppInstanceId i, final String person,
            final String password) {
        EInstance ei = getI(i);
        final EPerson epe = findPerson(ei, person, false);
        if (epe == null)
            return;
        epe.setPassword(password);
        ofy().save().entity((EPerson) epe).now();
    }

    @Override
    public boolean validatePasswordForPerson(AppInstanceId i, String person,
            String password) {
        EInstance ei = getI(i);
        EPerson epe = findPerson(ei, person, false);
        if (epe == null)
            return false;
        if (epe.getPassword() == null)
            return false;
        return epe.getPassword().equals(password);
    }

    @Override
    public void clearAll(AppInstanceId i) {
        EInstance ei = getI(i);
        final List<EHotel> liH = ofy().load().type(EHotel.class).ancestor(ei)
                .list();
        final List<EPerson> liP = ofy().load().type(EPerson.class).ancestor(ei)
                .list();
        final List<EPermissions> liPe = ofy().load().type(EPermissions.class)
                .ancestor(ei).list();

        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().delete().entities(liH);
                ofy().delete().entities(liP);
                ofy().delete().entities(liPe);
            }
        });

    }

    @Override
    public void removePerson(AppInstanceId i, final String person) {
        EInstance ei = getI(i);
        final EPerson pe = findPerson(ei, person, false);
        if (pe == null)
            return;
        final EPermissions eperm = getPerm(ei);
        ofy().transact(new VoidWork() {
            public void vrun() {
                removePersonPerm(eperm, pe.getId());
                ofy().delete().entity(pe);
                ofy().save().entity((EPermissions) eperm).now();
            }
        });

    }

    @Override
    public void removeHotel(AppInstanceId i, final String hotel) {
        EInstance ei = getI(i);
        final EHotel ho = findHotel(ei, hotel, false);
        if (ho == null)
            return;
        final EPermissions eperm = getPerm(ei);
        ofy().transact(new VoidWork() {
            public void vrun() {
                removeHotelPerm(eperm, ho.getId());
                ofy().delete().entity(ho);
                ofy().save().entity((EPermissions) eperm).now();
            }
        });

    }

    @Override
    public String getPassword(AppInstanceId i, String person) {
        EInstance ei = getI(i);
        EPerson epe = findPerson(ei, person, false);
        if (epe == null)
            return null;
        return epe.getPassword();
    }

}
