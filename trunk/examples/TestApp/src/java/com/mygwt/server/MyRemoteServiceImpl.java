/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.mygwt.server;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwtmodel.table.DataTreeLevel;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.mailcommon.CListOfMailProperties;
import com.gwtmodel.table.mailcommon.CMailToSend;
import com.gwtmodel.util.FileUtil;
import com.gwtmodel.util.SendMail;
import com.mygwt.client.MyRemoteService;
import com.mygwt.common.data.TOItemRecord;
import com.mygwt.common.data.TOMarkRecord;
import com.mygwt.common.data.ToEditRecord;
import com.mygwt.server.jpa.EMF;
import com.mygwt.server.jpa.EditRecord;
import com.mygwt.server.jpa.ItemRecord;
import com.mygwt.server.jpa.MarkRekord;

@SuppressWarnings("serial")
public class MyRemoteServiceImpl extends RemoteServiceServlet implements
        MyRemoteService {

    @Override
    public CListOfMailProperties getListOfMailBoxes() {
        File f = FileUtil.getResourceDir(this.getClass());
        FileFilter filter = new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return false;
                }
                return pathname.getName().endsWith(".properties");
            }
        };
        File[] dir = f.listFiles(filter);
        CListOfMailProperties cma = new CListOfMailProperties();
        if ((dir == null) || (dir.length == 0)) {
            cma.setErrMess("No mail boxes defined ");
            return cma;
        }
        for (int i = 0; i < dir.length; i++) {
            Properties pro = new Properties();
            try {
                pro.load(new FileReader(dir[i]));
            } catch (FileNotFoundException e) {
                cma.setErrMess(e.getMessage());
                return cma;
            } catch (IOException e) {
                cma.setErrMess(e.getMessage());
                return cma;
            }
            Map<String, String> ma = FileUtil.PropertyToMap(pro);
            ma.put(SendMail.PROP_FILE_NAME, dir[i].getAbsolutePath());
            cma.addMap(ma);
        }
        return cma;
    }

    @Override
    public String sendMail(CMailToSend mail) {
        return SendMail.postMail(mail.isText(),
                FileUtil.MapToProperty(mail.getBox()), mail.getTo(),
                mail.getHeader(), mail.getContent(), mail.getFrom());
    }

    /**
     * Read list of all objects for persistence storage
     */
    @Override
    public List<TOItemRecord> getItemList() {
        EntityManager em = null;
        List<TOItemRecord> tList = new ArrayList<TOItemRecord>();
        try {
            em = EMF.get().createEntityManager();
            Query query = em.createQuery("SELECT ir FROM ItemRecord ir");
            List rList = query.getResultList();
            if (rList.isEmpty()) {
                rList = new ArrayList();
                Date da = DateUtil.getToday();
                EntityTransaction entr = em.getTransaction();
                for (int i = 0; i < 100; i++) {
                    ItemRecord re = new ItemRecord();
                    int rootLevel = DataTreeLevel.toLeaf(1);
                    if (i % 10 == 0) {
                        rootLevel = DataTreeLevel.toNode(0);
                    }
                    re.setRootLevel(rootLevel);
                    re.setiDate(da);
                    re.setiNumber(i);
                    re.setiName("NAME-" + CUtil.NumbToS(i));
                    rList.add(re);
                    entr.begin();
                    em.persist(re);
                    entr.commit();
                    da = DateUtil.NextDayD(da);
                }
            }
            for (Object o : rList) {
                ItemRecord r = (ItemRecord) o;
                TOItemRecord te = new TOItemRecord();
                te.setiDate(r.getiDate());
                te.setiName(r.getiName());
                te.setiNumber(r.getiNumber());
                te.setRootLevel(r.getRootLevel());
                tList.add(te);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return tList;
    }

    /**
     * Modify table. Add new, remove or modify
     */
    @Override
    public void ItemRecordOp(PersistTypeEnum op, TOItemRecord re) {
        EntityManager em = null;
        try {
            em = EMF.get().createEntityManager();
            EntityTransaction entr = em.getTransaction();

            Query q = em
                    .createQuery("SELECT ir FROM ItemRecord ir WHERE ir.iNumber = :iNumber");
            q.setParameter("iNumber", re.getiNumber());
            Object result = null;
            try {
                result = q.getSingleResult();
            } catch (NoResultException e) {
                // nothing
            }
            entr.begin();

            switch (op) {
            case ADD:
            case MODIF:
                ItemRecord ire;
                if (result == null) {
                    ire = new ItemRecord();
                    ire.setiNumber(re.getiNumber());
                } else {
                    ire = (ItemRecord) result;
                }
                ire.setiName(re.getiName());
                ire.setiDate(re.getiDate());
                ire.setRootLevel(re.getRootLevel());
                em.persist(ire);
                break;
            case REMOVE:
                if (result != null) {
                    em.remove(result);
                }
                break;
            }
            entr.commit();
            em.close();

        } catch (Exception e) {
            e.printStackTrace();
            em.close();
        }

    }

    /* (non-Javadoc)
     * @see com.mygwt.client.MyRemoteService#getItemMarkList()
     */
    @Override
    public List<TOMarkRecord> getItemMarkList() {
        EntityManager em = null;
        List<TOMarkRecord> tList = new ArrayList<TOMarkRecord>();
        try {
            em = EMF.get().createEntityManager();
            Query query = em.createQuery("SELECT ir FROM MarkRekord ir");
            List rList = query.getResultList();
            if (rList.isEmpty()) {
                rList = new ArrayList();
                Date da = DateUtil.getToday();
                EntityTransaction entr = em.getTransaction();
                for (int i = 0; i < 5; i++) {
                    MarkRekord re = new MarkRekord();
                    re.setMarked(false);
                    re.setEditMark(false);
                    re.setiDate(da);
                    re.setNumber(i);
                    re.setiName("NAME-" + CUtil.NumbToS(i));
                    rList.add(re);
                    entr.begin();
                    em.persist(re);
                    entr.commit();
                    da = DateUtil.NextDayD(da);
                }
            }
            for (Object o : rList) {
                MarkRekord r = (MarkRekord) o;
                TOMarkRecord te = new TOMarkRecord();
                te.setiDate(r.getiDate());
                te.setiName(r.getiName());
                te.setNumber(r.getNumber());
                te.setMarked(r.isMarked());
                te.setEditMark(r.isEditMark());
                tList.add(te);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return tList;
    }

    /* (non-Javadoc)
     * @see com.mygwt.client.MyRemoteService#ItemMarkRecordOp(com.gwtmodel.table.common.PersistTypeEnum, com.mygwt.common.data.TOMarkRecord)
     */
    @Override
    public void ItemMarkRecordOp(PersistTypeEnum op, TOMarkRecord re) {
        EntityManager em = null;
        try {
            em = EMF.get().createEntityManager();
            EntityTransaction entr = em.getTransaction();

            Query q = em
                    .createQuery("SELECT ir FROM MarkRekord ir WHERE ir.number = :iNumber");
            q.setParameter("iNumber", re.getNumber());
            Object result = null;
            try {
                result = q.getSingleResult();
            } catch (NoResultException e) {
                // nothing
            }
            entr.begin();

            switch (op) {
            case ADD:
            case MODIF:
                MarkRekord ire;
                if (result == null) {
                    ire = new MarkRekord();
                    ire.setNumber(re.getNumber());
                } else {
                    ire = (MarkRekord) result;
                }
                ire.setiName(re.getiName());
                ire.setiDate(re.getiDate());
                ire.setMarked(re.isMarked());
                ire.setEditMark(re.isMarked());
                em.persist(ire);
                break;
            case REMOVE:
                if (result != null) {
                    em.remove(result);
                }
                break;
            }
            entr.commit();
            em.close();

        } catch (Exception e) {
            e.printStackTrace();
            em.close();
        }

    }

    @Override
    public List<ToEditRecord> getItemEditList() {
        EntityManager em = null;
        List<ToEditRecord> tList = new ArrayList<ToEditRecord>();
        try {
            em = EMF.get().createEntityManager();
            Query query = em.createQuery("SELECT ir FROM EditRecord ir");
            List rList = query.getResultList();
            if (rList.isEmpty()) {
                rList = new ArrayList();
                Date da = DateUtil.getToday();
                EntityTransaction entr = em.getTransaction();
                for (int i = 0; i < 5; i++) {
                    EditRecord re = new EditRecord();
                    re.setMark(false);
                    re.setDate(da);
                    re.setName("NAME " + i);
                    re.setRecId(i);
                    re.setNumber(new BigDecimal(i));
                    rList.add(re);
                    entr.begin();
                    em.persist(re);
                    entr.commit();
                    da = DateUtil.NextDayD(da);
                }
            }
            for (Object o : rList) {
                EditRecord r = (EditRecord) o;
                ToEditRecord te = new ToEditRecord();
                te.setDate(r.getDate());
                te.setName(r.getName());
                te.setMark(r.isMark());
                te.setNumber(r.getNumber());
                te.setRecId(r.getRecId());
                tList.add(te);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return tList;
    }

    private void toER(EditRecord ire, ToEditRecord re) {
        ire.setDate(re.getDate());
        ire.setMark(re.isMark());
        ire.setName(re.getName());
        ire.setNumber(re.getNumber());
    }

    @Override
    public void ItemEditRecordOp(PersistTypeEnum op, ToEditRecord re) {
        EntityManager em = null;
        try {
            em = EMF.get().createEntityManager();
            EntityTransaction entr = em.getTransaction();

            Query q = em
                    .createQuery("SELECT ir FROM EditRecord ir WHERE ir.recId = :iNumber");
            q.setParameter("iNumber", re.getRecId());
            Object result = null;
            try {
                result = q.getSingleResult();
            } catch (NoResultException e) {
                // nothing
            }
            entr.begin();

            switch (op) {
            case ADD:
            case MODIF:
                EditRecord ire;
                if (result == null) {
                    ire = new EditRecord();
                    ire.setRecId(re.getRecId());
                } else {
                    ire = (EditRecord) result;
                }
                toER(ire, re);
                em.persist(ire);
                break;
            case REMOVE:
                if (result != null) {
                    em.remove(result);
                }
                break;
            }
            entr.commit();
            em.close();

        } catch (Exception e) {
            e.printStackTrace();
            em.close();
        }

    }

    private void removeAll() {
        EntityManager  em = EMF.get().createEntityManager();
        Query query = em.createQuery("SELECT ir FROM EditRecord ir");
        List rList = query.getResultList();
        EntityTransaction entr = em.getTransaction();
        for (Object o : rList) {
            entr.begin();
            em.remove(o);
            entr.commit();
        }
        em.close();
    }

    @Override
    public void saveEditItemList(List<ToEditRecord> rList) {
        removeAll();
        for (ToEditRecord re : rList) {
            ItemEditRecordOp(PersistTypeEnum.ADD, re);
        }
    }

}
