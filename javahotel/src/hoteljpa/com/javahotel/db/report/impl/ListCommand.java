/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.report.impl;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.StringP;
import com.javahotel.db.authentication.impl.GetList;
import com.javahotel.db.commands.GetDownPaymentsList;
import com.javahotel.db.commands.GetListCommand;
import com.javahotel.db.commands.GetObjectBookState;
import com.javahotel.db.commands.GetOneCommand;
import com.javahotel.db.security.impl.SecurityFilter;
import com.javahotel.dbjpa.ejb3.JpaManagerData;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.ELog;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IList;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.security.login.HotelLoginP;
import java.util.List;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Stateless(mappedName = "listEJB")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ListCommand implements IList {

    private static List<StringP> getDataBaseList(final SessionT sessionId) {
        List<String> d = JpaManagerData.getDataBaseNames();
        return ReportUtil.toS(d, GetProp.getSeID());
    }

    private static HotelT getH(final CommandParam p) {
        String ho = p.getHotel();
        return new HotelT(ho);
    }

    private static List<AbstractTo> getDictList(final SessionT sessionId,
            final RType r, final CommandParam p) {
        DictType d = p.getDict(HLog.getILog());
        GetListCommand com = new GetListCommand(sessionId, d, getH(p));
        com.run();
        return com.getCol();
    }

    private static List<? extends AbstractTo> getDownPayments(
            final SessionT sessionId, final CommandParam p) {
        HotelT ho = getH(p);
        Date dFrom = p.getDateFrom();
        Date dTo = p.getDateTo();
        GetDownPaymentsList com = new GetDownPaymentsList(sessionId, ho, dFrom,
                dTo);
        com.run();
        return com.getRes();
    }

    public List<AbstractTo> getList(final SessionT sessionId,
            final RType r, final CommandParam p) throws HotelException {
        List<? extends AbstractTo> co = null;
        switch (r) {
            case ListDict:
                co = getDictList(sessionId, r, p);
                return (List<AbstractTo>) co;
            case DownPayments:
                co = getDownPayments(sessionId, p);
                return (List<AbstractTo>) co;
            default:
                break;
        }

        HotelLoginP hp = SecurityFilter.isLogged(sessionId, false);
        String logs = ELog.logListS(sessionId.getName(), hp.getUser(), r.name());
        HLog.getLo().info(logs);
        switch (r) {
            case AllHotels:
                co = GetList.getHotelList(sessionId);
                break;
            case DataBases:
                co = getDataBaseList(sessionId);
                break;
            case AllPersons:
                co = GetList.getPersonList(sessionId);
                break;
            case PersonHotelRoles:
                String pe = p.getPerson();
                HotelT ho = new HotelT(p.getHotel());
                List<String> d = GetList.getPersonHotelRoles(sessionId, pe,
                        ho);
                co = ReportUtil.toS(d, null);
                break;
            case ResObjectState:
                BookStateParam bS = new BookStateParam(p);
                logs = ELog.logDrawPeriodS(IMessId.READRESTATE,
                        sessionId.getName(), hp.getUser(), p.getHotel(), bS.getRParam().getPe().getFrom(), bS.getRParam().getPe().getTo());
                HLog.getLo().info(logs);
                logs = ELog.drawColStringS(bS.getRParam().getResList());
                HLog.getLo().info(logs);
                ho = new HotelT(p.getHotel());
                GetObjectBookState oS = new GetObjectBookState(sessionId, ho, bS.getRParam());
                oS.run();
                co = oS.getROut();
                break;
        }
        return (List<AbstractTo>) co;
    }

    public AbstractTo getOne(final SessionT sessionId, final RType r,
            final CommandParam p) {
        DictType d = p.getDict(HLog.getILog());
        GetOneCommand com = new GetOneCommand(sessionId, d, getH(p), p);
        com.run();
        return com.getRes();
    }
}
