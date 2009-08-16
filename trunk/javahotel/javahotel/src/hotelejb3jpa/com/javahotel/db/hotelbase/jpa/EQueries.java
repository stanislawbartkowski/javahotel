/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

package com.javahotel.db.hotelbase.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */

/*
 * The purspose is to have all queries in one place, not scattered between
 * different beans.
 * In order to avoid creating empty bean (and empty table) firstly it was
 * placed in @MappedSuperclass class
 * It works for toplink and openjpa, but seems not work for hibernate
 * So finally empty entity bean was created to have it working in JBoss
*/
@NamedQueries({
@NamedQuery(name = "getParamsLike", query = "SELECT Object(o) FROM ParamRegistry o WHERE (o.hotel = :hotel) AND (o.name LIKE :namelike)") ,
@NamedQuery(name = "getValidationsForDay", query = "SELECT Object(o) FROM AdvancePayment o WHERE o.bill.booking.season.hotel = :hotel"),
@NamedQuery(name = "getObjectResState", query = "SELECT Object(o) FROM PaymentRow o WHERE ((o.rowFrom<= :dTo) AND (o.rowTo >= :dFrom) AND (o.bookelem.resObject = :oName) AND (o.bookelem.bookrecord.booking.hotel = :hotel))"),
@NamedQuery(name = "getBookingList", query = "SELECT Object(o) FROM Booking o WHERE o.hotel.name = :hotel"),
@NamedQuery(name = "getOnePriceList", query = "SELECT Object(o) FROM OfferPrice o WHERE o.hotel = :hotel AND o.season = :seasonname AND o.name = :name"),
@NamedQuery(name = "getSeasonPeriod", query = "SELECT Object(o) FROM OfferSeasonPeriod o WHERE o.offerid.hotel = :hotel AND o.offerid.name = :name AND o.pId = :pId "),
@NamedQuery(name = "getPriceList", query = "SELECT Object(o) FROM OfferPrice o WHERE o.hotel.name = :hotel"),
@NamedQuery(name = "getCustomerList", query = "SELECT Object(o) FROM Customer o WHERE o.hotel.name = :hotel"),
@NamedQuery(name = "getSeasons", query = "SELECT Object(o) FROM OfferSeason o WHERE o.hotel.name = :hotel"),
@NamedQuery(name = "getListStandard", query = "SELECT Object(o) FROM RoomStandard o WHERE o.hotel.name = :hotel"),
@NamedQuery(name = "getVatDict", query = "SELECT Object(o) FROM VatDictionary o WHERE o.hotel.name = :hotel"),
@NamedQuery(name = "getListFacilities", query = "SELECT Object(o) FROM RoomFacilities o WHERE o.hotel.name = :hotel"),
@NamedQuery(name = "getListServices", query = "SELECT Object(o) FROM ServiceDictionary o WHERE o.hotel.name = :hotel"),
@NamedQuery(name = "getRoomObjects", query = "SELECT Object(o) FROM ResObject o WHERE o.hotel.name = :hotel")
})
@Entity
public class EQueries {
    
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
