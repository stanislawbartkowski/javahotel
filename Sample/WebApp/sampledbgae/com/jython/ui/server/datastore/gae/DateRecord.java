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
package com.jython.ui.server.datastore.gae;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.jython.ui.server.datastore.IDateRecord;

@Entity
class DateRecord implements IDateRecord {

    @Id
    private Long id;
    
    private Date d1;
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
        return id;
    }

}
