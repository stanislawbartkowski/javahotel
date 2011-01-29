/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.command;

import java.text.MessageFormat;
import java.util.logging.Level;

import com.javahotel.common.command.DictType;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.context.IPersistCache;
import com.javahotel.db.hotelbase.jpa.RHotel;
import com.javahotel.db.hotelbase.types.IPureDictionary;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.security.impl.SecurityFilter;
import com.javahotel.db.util.CommonHelper;
import com.javahotel.dbjpa.ejb3.JpaEntity;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.ELog;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetLMess;
import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.security.login.HotelLoginP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
abstract public class CommandTemplate {

    protected abstract void prevRun();

    private class ContextC implements ICommandContext {

        private JpaEntity jpa;
        private final HotelLoginP hp;
        private final SessionT sessionId;
        private RHotel rHotel;
        private final IPersistCache iCache;

        ContextC(final HotelLoginP php, final SessionT se) {
            this.hp = php;
            this.sessionId = se;
            jpa = null;
            iCache = new PersistCache();
        }

        public JpaEntity getJpa() {
            return jpa;
        }

        public void setJpa(final JpaEntity jpa) {
            this.jpa = jpa;
        }

        public HotelLoginP getHP() {
            return hp;
        }

        public SessionT getSession() {
            return sessionId;
        }

        public void setHotel(final String h) {
            rHotel = CommonHelper.getH(this, new HotelT(h));
        }

        public String getHotel() {
            return rHotel.getName();
        }

        public RHotel getRHotel() {
            return rHotel;
        }

        public String logEvent(String eId, final Object... params) {
            String se = sessionId.getName();
            String user = hp.getUser();
            return ELog.logEventHS(eId, eId, se, user, getHotel(), params);
        }

        public void logFatal(final String messid, final Object... params) {
            String se = sessionId.getName();
            String user = hp.getUser();
            HLog.logFailureSE(se, user, getHotel(), messid, params);
        }

        public void logFatalE(Exception e) {
            HLog.fatalFailureE(e);
        }

        public GetLogger getLog() {
            return HLog.getL();
        }

        public IPersistCache getC() {
            return iCache;
        }

        public boolean isNull(HId id) {
            if (id == null) {
                return true;
            }
            return id.isNull();
        }

        public String getRecordDescr(DictType d, IPureDictionary o) {
            HId id = o.getId();
            String name = o.getName();
            String descr = o.getDescription();
            String idS = "(no)";
            if (!isNull(id)) {
                idS = id.toString();
            }
            String cName = o.getName();
            if (d != null) {
                cName = d.toString();
            }
            String logs = MessageFormat.format(GetLMess.getM(IMessId.DICTRECORDDESCRIPTION), cName, idS, name,
                    descr);
            return logs;
        }
    }
    private final boolean blockP;
    protected boolean trasuccess;
    private boolean startedt = false;
    protected final ICommandContext iC;
    private final boolean startTraAutom;

    protected abstract void command();

    protected void aftercommit() {
    }

    protected boolean startedTra() {
        return startedt;
    }

    public CommandTemplate(final SessionT sessionId, final boolean admin,
            final boolean blockP, final boolean startTraAutom) {
        HotelLoginP hp = SecurityFilter.isLogged(sessionId, admin);
        this.blockP = blockP;
        trasuccess = true;
        SessionT s = sessionId;
        this.startTraAutom = startTraAutom;
        iC = new ContextC(hp, s);
    }

    protected void startTra() {
        if (startedt) {
            return;
        }
        iC.getJpa().startTransaction(blockP);
        startedt = true;
    }

    protected void closeJpa(final boolean success) {

        if (iC.getJpa() == null) {
            return;
        }
        if (startedt) {
            try {
                iC.getJpa().endTransaction(blockP, success);
            } catch (Exception e) {
                iC.getJpa().stopSemTransaction(blockP);
                iC.getJpa().closeEntity();
                HLog.getLo().log(Level.SEVERE, "", e);
                throw new HotelException(e);
            }
        } else {
            iC.getJpa().stopSemTransaction(blockP);
        }
        iC.getJpa().closeEntity();
    }

    public void run() {
        try {
            prevRun(); // before
            iC.getJpa().startSemTransaction(blockP);
            if (startTraAutom) {
                startTra();
            }
            command();
//            GetNextSym.FlushReg(iC);
            iC.getC().persistRecords(iC);
        } catch (HotelException e) {
            closeJpa(false);
            throw e;
        } catch (Exception e) {
            HLog.getLo().log(Level.SEVERE, "", e);
            closeJpa(false);
            throw new HotelException(e);
        }
        closeJpa(trasuccess);
        aftercommit();
    }
}
