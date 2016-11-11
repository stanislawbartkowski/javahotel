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
package com.gwtmodel.shared;

import java.util.Date;

import com.gwtmodel.table.map.XMap;
import com.jythonui.server.ISharedConsts;

abstract public class RMap extends XMap {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date creationDate;
    private Date modifDate;

    public String getDescription() {
        return getAttr(ISharedConsts.DESCRIPTION);
    }

    public String getName() {
        return getAttr(ISharedConsts.NAME);
    }

    public void setName(String name) {
        setAttr(ISharedConsts.NAME, name);
    }

    public void setDescription(String de) {
        setAttr(ISharedConsts.DESCRIPTION, de);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifDate() {
        return modifDate;
    }

    public void setModifDate(Date modifDate) {
        this.modifDate = modifDate;
    }

    public void setCreationPerson(String person) {
        setAttr(ISharedConsts.CREATIONPERSONPROPERTY, person);
    }

    public String getCreationPerson() {
        return getAttr(ISharedConsts.CREATIONPERSONPROPERTY);
    }

    public void setModifPerson(String person) {
        setAttr(ISharedConsts.MODIFPERSONPROPERTY, person);
    }

    public String getModifPerson() {
        return getAttr(ISharedConsts.MODIFPERSONPROPERTY);
    }

}
