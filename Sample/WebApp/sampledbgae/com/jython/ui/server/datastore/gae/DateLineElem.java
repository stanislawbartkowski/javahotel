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
package com.jython.ui.server.datastore.gae;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.jython.ui.server.datastore.IDateLineElem;

@Entity
public class DateLineElem implements IDateLineElem {

    @Id
    private Long id;

    @Index
    private Long elemid;
    private int numb;
    private String info;
    @Index
    private Date date;

    @Override
    public Long getId() {
        return elemid;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public int getNumb() {
        return numb;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setDate(int numb, String info) {
        this.numb = numb;
        this.info = info;
    }

    void setI(Long elemid, Date dt) {
        this.elemid = elemid;
        this.date = dt;
    }

}
