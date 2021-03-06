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

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.jython.ui.server.datastore.IPerson;

@Entity
class Person implements IPerson {
    @Id
    private Long id;
    @Index
    private String personNumb;
    private String personName;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the personNumb
     */
    public String getPersonNumb() {
        return personNumb;
    }

    /**
     * @param personNumb
     *            the personNumb to set
     */
    public void setPersonNumb(String personNumb) {
        this.personNumb = personNumb;
    }

    /**
     * @return the personName
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName
     *            the personName to set
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

}