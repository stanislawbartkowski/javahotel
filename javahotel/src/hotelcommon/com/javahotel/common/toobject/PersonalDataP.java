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

package com.javahotel.common.toobject;

import com.javahotel.common.command.PersonTitle;
import java.io.Serializable;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
class PersonalDataP implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private PersonTitle pTitle;

    String getFirstName() {
        return firstName;
    }

    void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    PersonTitle getPTitle() {
        return pTitle;
    }

    void setPTitle(PersonTitle pTitle) {
        this.pTitle = pTitle;
    }

    Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

}
