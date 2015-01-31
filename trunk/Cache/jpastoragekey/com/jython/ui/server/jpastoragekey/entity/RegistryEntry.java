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
package com.jython.ui.server.jpastoragekey.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "registryRealm",
        "registryEntry" }))
@NamedQueries({
        @NamedQuery(name = "findRegistryEntry", query = "SELECT x FROM RegistryEntry x WHERE x.registryRealm = ?1 AND x.registryEntry = ?2"),
        @NamedQuery(name = "getListRegistryEntry", query = "SELECT x.registryEntry FROM RegistryEntry x WHERE x.registryRealm = ?1"),
        @NamedQuery(name = "removeRegistryEntry", query = "DELETE FROM RegistryEntry x WHERE x.registryRealm = ?1 AND x.registryEntry = ?2") })
public class RegistryEntry extends AbstractRegistryEntry {


}