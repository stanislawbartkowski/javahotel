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
package com.jython.ui.server.jpastoragekey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "findRegistryEntry", query = "SELECT x FROM RegistryEntry x WHERE x.registryRealm = ?1 AND x.registryEntry = ?2"), })
public class RegistryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String registryRealm;
    private String registryEntry;
    @Lob
    private byte[] value;

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