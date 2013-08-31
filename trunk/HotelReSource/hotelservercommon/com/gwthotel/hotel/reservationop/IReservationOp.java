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
package com.gwthotel.hotel.reservationop;

import java.util.List;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.stay.ResGuest;

public interface IReservationOp {

    List<ResData> queryReservation(HotelId hotel, List<ResQuery> rQuery);

    void changeStatus(HotelId hotel, String resName, ResStatus newStatus);

    void setResGuestList(HotelId hotel, String resName, List<ResGuest> gList);

    List<ResGuest> getResGuestList(HotelId hotel, String resName);

    void addResAddPayment(HotelId hotel, String resName,
            ReservationPaymentDetail add);

    List<ReservationPaymentDetail> getResAddPaymentList(HotelId hotel,
            String resName);
    
    List<CustomerBill> findBillsForReservation(HotelId hotel, String resName);

}
