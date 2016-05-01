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
package com.jython.ui.server.jpastoragekey.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
abstract public class AbstractRegistryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String registryRealm;
    @Column(nullable = false)
    private String registryEntry;
// Important:
// Seems not working for hibernate and postgresql
// Remove @Lob    
//    @Lob
// force BLOB type without @Lob
    
// nonpostgresql (Derby and H2)    
//    @Column(columnDefinition="BLOB")
    
// postgresql only    
//    @Column(columnDefinition="bytea")
    private byte[] value;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifDate;

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

    public String getRegistryRealm() {
        return registryRealm;
    }

    public void setRegistryRealm(String registryRealm) {
        this.registryRealm = registryRealm;
    }

    public String getRegistryEntry() {
        return registryEntry;
    }

    public void setRegistryEntry(String registryEntry) {
        this.registryEntry = registryEntry;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

}
