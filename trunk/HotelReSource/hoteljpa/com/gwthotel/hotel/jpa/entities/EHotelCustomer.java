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
package com.gwthotel.hotel.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jython.serversecurity.jpa.entities.EObjectDict;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel", "name" }))
@NamedQueries({
        @NamedQuery(name = "findAllCustomers", query = "SELECT x FROM EHotelCustomer x WHERE x.hotel = ?1"),
        @NamedQuery(name = "deleteAllCustomers", query = "DELETE FROM EHotelCustomer x WHERE x.hotel = ?1"),
        @NamedQuery(name = "findOneCustomer", query = "SELECT x FROM EHotelCustomer x WHERE x.hotel = ?1 AND x.name = ?2") })
public class EHotelCustomer extends EObjectDict {

    private String firstname;
    private String surname;
    
    @Column(nullable = false)
    private char sex; // F - female, M - male

    @Column(nullable = false)
    private char doctype; // I - Id card, P - passport
    
    private String docnumb;
    private String email;
    private String phone1;
    private String phone2;
    private String fax;

    @Column(length = 2)
    private String country;
    private String street;
    private String postalcode;
    private String city;
    private String region;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public char getDoctype() {
        return doctype;
    }

    public void setDoctype(char doctype) {
        this.doctype = doctype;
    }

    public String getDocnumb() {
        return docnumb;
    }

    public void setDocnumb(String docnumb) {
        this.docnumb = docnumb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

}
