/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jython.serversecurity.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"instanceId","name"}))
@NamedQueries({
//        @NamedQuery(name = "findObjectByLong", query = "SELECT x FROM EObject x WHERE x.id = ?1"),
        @NamedQuery(name = "findAllObjects", query = "SELECT x FROM EObject x WHERE x.instanceId = ?1"),
        @NamedQuery(name = "removeAllObjects", query = "DELETE FROM EObject x WHERE x.instanceId= ?1"),
        @NamedQuery(name = "findObjectByName", query = "SELECT x FROM EObject x WHERE x.instanceId= ?1 AND x.name = ?2") })
public class EObject extends EDictInstance {

}