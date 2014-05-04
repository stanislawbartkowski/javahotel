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
package com.gwthotel.hotelejb.reservationop;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import com.google.inject.Inject;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.reservationop.ResData;
import com.gwthotel.hotel.reservationop.ResQuery;
import com.gwthotel.hotel.stay.ResGuest;
import com.gwthotel.shared.IHotelConsts;
import com.jython.serversecurity.OObjectId;
import com.jythonui.server.defa.GuiceInterceptor;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@EJB(name = IHotelConsts.HOTELRESERVATIONOPJNDI, beanInterface = IReservationOp.class)
@Remote
@Interceptors(value = { GuiceInterceptor.class })
public class HotelReservationOpEJB implements IReservationOp {

    private IReservationOp rOp;

    @Inject
    public void injectHotelServices(IReservationOp injectedServices) {
        rOp = injectedServices;
    }

    @Override
    public List<ResData> queryReservation(OObjectId hotel, List<ResQuery> rQuery) {
        return rOp.queryReservation(hotel, rQuery);
    }

    @Override
    public void changeStatus(OObjectId hotel, String resName,
            ResStatus newStatus) {
        rOp.changeStatus(hotel, resName, newStatus);
    }

    @Override
    public void setResGuestList(OObjectId hotel, String resName,
            List<ResGuest> gList) {
        rOp.setResGuestList(hotel, resName, gList);
    }

    @Override
    public List<ResGuest> getResGuestList(OObjectId hotel, String resName) {
        return rOp.getResGuestList(hotel, resName);
    }

    @Override
    public void addResAddPayment(OObjectId hotel, String resName,
            ReservationPaymentDetail add) {
        rOp.addResAddPayment(hotel, resName, add);
    }

    @Override
    public List<ReservationPaymentDetail> getResAddPaymentList(OObjectId hotel,
            String resName) {
        return rOp.getResAddPaymentList(hotel, resName);
    }

    @Override
    public List<CustomerBill> findBillsForReservation(OObjectId hotel,
            String resName) {
        return rOp.findBillsForReservation(hotel, resName);
    }

    @Override
    public List<ResData> searchReservation(OObjectId hotel, ResQuery rQuery) {
        return rOp.searchReservation(hotel, rQuery);
    }

}