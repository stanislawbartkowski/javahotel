/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.datastore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.jython.ui.server.datastore.IDateRecord;

@Entity
@NamedQueries({
    @NamedQuery(name = "DateRecord_removeAllElem", query = "DELETE FROM DateRecord"),
    @NamedQuery(name = "DateRecord_findDateElem", query = "SELECT x FROM DateRecord x WHERE x.itemid = ?1"),
    @NamedQuery(name = "DateRecord_removeDateElem", query = "DELETE FROM DateRecord x WHERE x.itemid = ?1") })
public class DateRecord implements IDateRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemid;

    @Temporal(TemporalType.DATE)
    private Date d1;
    @Temporal(TemporalType.DATE)
    private Date d2;

    @Override
    public void setDates(Date d1, Date d2) {
        this.d1 = d1;
        this.d2 = d2;
    }

    @Override
    public Date getD1() {
        return d1;
    }

    @Override
    public Date getD2() {
        return d2;
    }

    @Override
    public Long getId() {
        return itemid;
    }

}
