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

package com.javahotel.db.hotelbase.jpa;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;

import com.javahotel.db.hotelbase.types.IPureDictionary;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */

@MappedSuperclass
public abstract class AbstractPureDictionary extends AbstractIID implements IPureDictionary {
        
    @Basic(optional = false)
    private String name;
    @Basic
    private String description;
    
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
