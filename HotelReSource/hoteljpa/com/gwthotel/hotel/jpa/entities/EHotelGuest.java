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
package com.gwthotel.hotel.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(name = "countAllGuests", query = "SELECT COUNT(x) FROM EHotelGuest x,EHotelCustomer c WHERE x.customer = c AND c.hotel=?1"),
        @NamedQuery(name = "deleteGuestForRoom", query = "DELETE FROM EHotelGuest x WHERE x.room=?1"),
        @NamedQuery(name = "deleteGuestForCustomer", query = "DELETE FROM EHotelGuest x WHERE x.customer=?1"),
        @NamedQuery(name = "findGuestForReservation", query = "SELECT x FROM EHotelGuest x WHERE x.reservation = ?1"),
        @NamedQuery(name = "deleteGuestsFromReservation", query = "DELETE FROM EHotelGuest x WHERE x.reservation=?1"),
        @NamedQuery(name = "deleteAllGuestsReservationFromHotel", query = "DELETE FROM EHotelGuest x WHERE x.customer IN (SELECT c FROM EHotelCustomer c WHERE c.hotel = ?1)") })
public class EHotelGuest extends EHotelRoomCustomer {

}
