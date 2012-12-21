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
package com.javahotel.db.hotelbase.jpa;

import com.javahotel.common.command.PersonTitle;
import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@MappedSuperclass
public abstract class AbstractPersonalData {

    @Basic
    private String firstName;
    @Basic
    private String lastName;
    @Basic(optional=false)
    private PersonTitle pTitle;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public PersonTitle getPTitle() {
        return pTitle;
    }

    public void setPTitle(PersonTitle pTitle) {
        this.pTitle = pTitle;
    }
}
