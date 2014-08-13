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
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;
import com.jython.ui.server.gae.security.entities.EInstance;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.entities.EPermissions;
import com.jython.ui.server.gae.security.entities.EPerson;
import com.jython.ui.server.gae.security.entities.ElemPerm;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.JythonUIFatal;

public class ObjectAdminGae implements IOObjectAdmin {

    static {
        ObjectifyService.register(EPerson.class);
        ObjectifyService.register(EObject.class);
        ObjectifyService.register(EPermissions.class);
    }

    private final IGetLogMess lMess;
    private static final Logger log = Logger.getLogger(ObjectAdminGae.class
            .getName());

    private void severe(String errorC, String messId, String... pars) {
        String mess = lMess.getMess(errorC, messId, pars);
        log.severe(mess);
        throw new JythonUIFatal(mess);

    }

    @Inject
    public ObjectAdminGae(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess lMess) {
        this.lMess = lMess;
    }

    private EInstance getI(AppInstanceId i) {
        return EntUtil.findI(i);
    }

    @Override
    public List<Person> getListOfPersons(AppInstanceId i) {
        EInstance ei = getI(i);
        List<EPerson> li = ofy().load().type(EPerson.class).ancestor(ei).list();
        List<Person> outList = new ArrayList<Person>();
        for (EPerson e : li) {
            Person p = new Person();
            EntUtil.toProp(p, e);
            outList.add(p);
        }
        return outList;
    }

    @Override
    public List<OObject> getListOfObjects(AppInstanceId i) {
        EInstance ei = getI(i);
        List<EObject> li = ofy().load().type(EObject.class).ancestor(ei).list();
        List<OObject> outList = new ArrayList<OObject>();
        for (EObject e : li) {
            OObject p = new OObject();
            EntUtil.toProp(p, e);
            outList.add(p);
        }
        return outList;
    }

    private EPerson findPerson(EInstance ei, String name, boolean expected) {
        LoadResult<EPerson> p = ofy().load().type(EPerson.class).ancestor(ei)
                .filter("name ==", name).first();
        if (p.now() == null) {
            if (expected)
                severe(IErrorCode.ERRORCODE90,
                        ILogMess.PERSONNAMEISEXPECTEDBUTNOTFOUND, name);
            return null;
        }
        return p.now();
    }

    private EPerson findPerson(EInstance ei, Long id) {
        LoadResult<EPerson> e = ofy().load().type(EPerson.class).parent(ei)
                .id(id);
        if (e.now() == null) {
            severe(IErrorCode.ERRORCODE91,
                    ILogMess.PERSONIDISEXPECTEDBUTNOTFOUND, Long.toString(id));
            return null;
        }
        return e.now();
    }

    private EObject findHotel(EInstance ei, String name, boolean expected) {
        LoadResult<EObject> p = ofy().load().type(EObject.class).ancestor(ei)
                .filter("name ==", name).first();
        if (p.now() == null) {
            if (expected)
                severe(IErrorCode.ERRORCODE88, ILogMess.OBJECTCANNOTBEFOUND,
                        name);
            return null;
        }
        return p.now();
    }

    private EObject findHotel(EInstance ei, Long id) {
        LoadResult<EObject> e = ofy().load().type(EObject.class).parent(ei)
                .id(id);
        if (e.now() == null) {
            severe(IErrorCode.ERRORCODE89, ILogMess.OBJECTBYIDNOTFOUND,
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
    public List<OObjectRoles> getListOfRolesForPerson(AppInstanceId i,
            String person) {
        EInstance ei = getI(i);
        List<OObjectRoles> li = new ArrayList<OObjectRoles>();
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
            EObject eho = findHotel(ei, hId);
            OObject ho = new OObject();
            EntUtil.toProp(ho, eho);
            OObjectRoles rol = new OObjectRoles(ho);
            rol.getRoles().addAll(ro.get(hId));
            li.add(rol);
        }
        return li;
    }

    @Override
    public List<OObjectRoles> getListOfRolesForObject(AppInstanceId i,
            String hotel) {
        EInstance ei = getI(i);
        List<OObjectRoles> li = new ArrayList<OObjectRoles>();
        EObject ho = findHotel(ei, hotel, false);
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
            EntUtil.toProp(pe, epe);
            OObjectRoles rol = new OObjectRoles(pe);
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

    private void addroles(EPermissions eperm, OObjectRoles ro, Long hotelId,
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
    public void addOrModifObject(AppInstanceId i, final OObject hotel,
            final List<OObjectRoles> roles) {
        EInstance ei = getI(i);
        EObject eho = findHotel(ei, hotel.getName(), false);
        if (eho == null) {
            eho = new EObject();
            eho.setInstanceId(ei);
        }
        final EObject ho = eho;
        EntUtil.toEDict(ho, hotel);
        final List<Long> personIds = new ArrayList<Long>();
        final List<OObjectRoles> aroles = new ArrayList<OObjectRoles>();

        for (OObjectRoles ro : roles) {
            Person pe = (Person) ro.getObject();
            EPerson epe = findPerson(getI(i), pe.getName(), true);
            personIds.add(epe.getId());
            aroles.add(ro);
        }
        final EPermissions eperm = getPerm(ei);
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().save().entity((EObject) ho).now();
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
            final List<OObjectRoles> roles) {
        EInstance ei = getI(i);
        EPerson epo = findPerson(ei, person.getName(), false);
        if (epo == null) {
            epo = new EPerson();
            epo.setInstanceId(ei);
        }
        final EPerson po = epo;
        EntUtil.toEDict(po, person);
        final List<Long> hotelIds = new ArrayList<Long>();
        final List<OObjectRoles> aroles = new ArrayList<OObjectRoles>();

        for (OObjectRoles ro : roles) {
            OObject ho = (OObject) ro.getObject();
            EObject eho = findHotel(ei, ho.getName(), true);
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
        final List<EObject> liH = ofy().load().type(EObject.class).ancestor(ei)
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
    public void removeObject(AppInstanceId i, final String hotel) {
        EInstance ei = getI(i);
        final EObject ho = findHotel(ei, hotel, false);
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
