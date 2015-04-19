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

import com.jython.ui.server.datastore.IDateLineElem;

@Entity
@NamedQueries({
    @NamedQuery(name = "removeAllElem", query = "DELETE FROM DateLineElem"),
    @NamedQuery(name = "findDateElem", query = "SELECT x FROM DateLineElem x WHERE x.id = ?1 AND x.dt = ?2"),
    @NamedQuery(name = "removeDateElem", query = "DELETE FROM DateLineElem x WHERE x.id = ?1 AND x.dt = ?2") })
public class DateLineElem implements IDateLineElem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemid;

    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dt;
    private int numb;
    private String info;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Date getDate() {
        return dt;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
    
    

}
