/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.mygwt.server.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

/**
 * @author hotel
 *
 */
@Entity 
public class ItemRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    
    private String iName;
    
    private Date iDate;
    
    private Integer iNumber;

    /**
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * @return the iName
     */
    public String getiName() {
        return iName;
    }

    /**
     * @param iName the iName to set
     */
    public void setiName(String iName) {
        this.iName = iName;
    }

    /**
     * @return the iNumber
     */
    public Integer getiNumber() {
        return iNumber;
    }

    /**
     * @param iNumber the iNumber to set
     */
    public void setiNumber(Integer iNumber) {
        this.iNumber = iNumber;
    }

    /**
     * @return the iDate
     */
    public Date getiDate() {
        return iDate;
    }

    /**
     * @param iDate the iDate to set
     */
    public void setiDate(Date iDate) {
        this.iDate = iDate;
    }        

}
