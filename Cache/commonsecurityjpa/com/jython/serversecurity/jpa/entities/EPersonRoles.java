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

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "person_id", "object_id" }))
@NamedQueries({
        @NamedQuery(name = "getListOfRolesForPerson", query = "SELECT x FROM EPersonRoles x WHERE x.person = ?1"),
        @NamedQuery(name = "removeRolesForPerson", query = "DELETE FROM EPersonRoles x WHERE x.person = ?1"),
        @NamedQuery(name = "removeRolesForObject", query = "DELETE FROM EPersonRoles x WHERE x.object = ?1"),
        @NamedQuery(name = "getListOfRolesForObject", query = "SELECT x FROM EPersonRoles x WHERE x.object = ?1") })
public class EPersonRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private EPersonPassword person;

    @ManyToOne
    @JoinColumn(name = "object_id", nullable = false)
    private EObject object;

    private ArrayList<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public EPersonPassword getPerson() {
        return person;
    }

    public void setPerson(EPersonPassword person) {
        this.person = person;
    }

    public EObject getObject() {
        return object;
    }

    public void setObject(EObject object) {
        this.object = object;
    }        

}
