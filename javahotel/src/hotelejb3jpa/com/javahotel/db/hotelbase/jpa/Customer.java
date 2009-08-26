/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import com.javahotel.common.command.CustomerType;
import com.javahotel.common.command.IdentDocType;
import com.javahotel.common.command.PersonTitle;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class Customer extends AbstractDictionary {

    @Basic
    private String zipCode;
    @Basic
    private String name1;
    @Basic
    private String name2;
    @Basic
    private String city;
    @Basic
    private String stateUS;
    @Basic
    private String country;
    @Basic
    private String address1;
    @Basic
    private String address2;
    @Basic(optional = false)
    private CustomerType cType;
    @Basic
    private String NIP;
    @Basic
    private String PESEL;
    @Basic
    private IdentDocType docType;
    @Basic
    private String docNumber;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
    private CustomerPersonalData customerperson;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Collection<CustomerRemark> remarks;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Collection<CustomerPhoneNumber> phones;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Collection<CustomerBankAccount> accounts;


    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(final String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(final String name2) {
        this.name2 = name2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    public CustomerType getCType() {
        return cType;
    }

    public void setCType(CustomerType cType) {
        this.cType = cType;
    }

    public String getNIP() {
        return NIP;
    }

    public void setNIP(final String NIP) {
        this.NIP = NIP;
    }

    public String getPESEL() {
        return PESEL;
    }

    public void setPESEL(final String PESEL) {
        this.PESEL = PESEL;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(final String docNumber) {
        this.docNumber = docNumber;
    }

    public String getStateUS() {
        return stateUS;
    }

    public void setStateUS(final String stateUS) {
        this.stateUS = stateUS;
    }

    public Collection<CustomerRemark> getRemarks() {
        return remarks;
    }

    public void setRemarks(final Collection<CustomerRemark> remarks) {
        this.remarks = remarks;
    }

    public Collection<CustomerPhoneNumber> getPhones() {
        return phones;
    }

    public void setPhones(final Collection<CustomerPhoneNumber> phones) {
        this.phones = phones;
    }

    public Collection<CustomerBankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(final Collection<CustomerBankAccount> accounts) {
        this.accounts = accounts;
    }

    public IdentDocType getDocType() {
        return docType;
    }

    public void setDocType(IdentDocType docType) {
        this.docType = docType;
    }

    public CustomerPersonalData getCustomerperson() {
        return customerperson;
    }

    public void setCustomerperson(CustomerPersonalData customerperson) {
        this.customerperson = customerperson;
    }

    public String getFirstName() {
        if (customerperson == null) {
            return null;
        }
        return customerperson.getFirstName();
    }

    public String getLastName() {
        if (customerperson == null) {
            return null;
        }
        return customerperson.getLastName();
    }

    public PersonTitle getPTitle() {
        if (customerperson == null) {
            return null;
        }
        return customerperson.getPTitle();
    }

    public void setFirstName(final String s) {
        if (s == null) { return; }
        checkPD();
        customerperson.setFirstName(s);
    }
    
    private void checkPD() {
        if (customerperson == null) {
            customerperson = new CustomerPersonalData();
            customerperson.setCustomer(this);
        }
        
    }

    public void setLastName(final String s) {
        if (s == null) { return; }
        checkPD();
        customerperson.setLastName(s);
    }

    public void setPTitle(PersonTitle s) {
        if (s == null) { return; }
        checkPD();
        customerperson.setPTitle(s);
    }
}
