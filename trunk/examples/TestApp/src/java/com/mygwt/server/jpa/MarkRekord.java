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
public class MarkRekord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private int number;

    private String iName;

    private Date iDate;

    private boolean marked;

    private boolean editMark;

    /**
     * @return the iName
     */
    public String getiName() {
        return iName;
    }

    /**
     * @return the editMark
     */
    public boolean isEditMark() {
        return editMark;
    }

    /**
     * @param editMark the editMark to set
     */
    public void setEditMark(boolean editMark) {
        this.editMark = editMark;
    }

    /**
     * @param iName
     *            the iName to set
     */
    public void setiName(String iName) {
        this.iName = iName;
    }

    /**
     * @return the iDate
     */
    public Date getiDate() {
        return iDate;
    }

    /**
     * @param iDate
     *            the iDate to set
     */
    public void setiDate(Date iDate) {
        this.iDate = iDate;
    }

    /**
     * @return the marked
     */
    public boolean isMarked() {
        return marked;
    }

    /**
     * @param marked
     *            the marked to set
     */
    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number
     *            the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

}
