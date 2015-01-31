/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.reservationop;

import java.util.List;

import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.stay.ResGuest;
import com.jython.serversecurity.cache.OObjectId;

public interface IReservationOp {

    List<ResData> queryReservation(OObjectId hotel, List<ResQuery> rQuery);

    List<ResData> searchReservation(OObjectId hotel, ResQuery rQuery);

    void changeStatus(OObjectId hotel, String resName, ResStatus newStatus);

    void setResGuestList(OObjectId hotel, String resName, List<ResGuest> gList);

    List<ResGuest> getResGuestList(OObjectId hotel, String resName);

    void addResAddPayment(OObjectId hotel, String resName,
            ReservationPaymentDetail add);

    List<ReservationPaymentDetail> getResAddPaymentList(OObjectId hotel,
            String resName);

    List<CustomerBill> findBillsForReservation(OObjectId hotel, String resName);

    enum ResInfoType {
        FORSERVICE, FORROOM, FORCUSTOMER, FORGUEST, FORPAYER, FORPRICELIST
    }

    List<String> getReseForInfoType(OObjectId hotel, ResInfoType iType,
            String serviceName);

}
