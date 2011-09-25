/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.common.toobject;

import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.command.IdentDocType;
import com.javahotel.common.command.PersonTitle;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class CustomerP extends DictionaryP {
	
    private String zipCode;
    private String name1;
    private String name2;
    private String city;
    private String stateUS;
    private String country;
    private String address1;
    private String address2;
    private CustomerType cType;
    private String NIP;
    private String PESEL;
    private IdentDocType docType;
    private String docNumber;
    private PersonalDataP customerperson;
    private List<RemarkP> remarks;
    private List<PhoneNumberP> phones;
    private List<BankAccountP> accounts;
    private String mailAddress;


    /**
     * @return the mailAddress
     */
    public String getMailAddress() {
        return mailAddress;
    }

    /**
     * @param mailAddress the mailAddress to set
     */
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public IdentDocType getDocType() {
        return docType;
    }

    public void setDocType(IdentDocType docType) {
        this.docType = docType;
    }

    public enum F implements IField {

        zipCode, name1, name2, city, stateUS, country, address1, address2,
        cType, NIP, PESEL, docType, docNumber, mailAddress,
        firstName, lastName, pTitle
    }

    @Override
    public Class<?> getT(final IField f) {
        Class<?> cla = super.getT(f);
        if (cla != null) {
            return cla;
        }
        cla = String.class;
        F fie = (F) f;
        switch (fie) {
            case cType: cla = CustomerType.class; break;
            case docType: cla = IdentDocType.class; break;
            case pTitle: cla = PersonTitle.class; break;
        }
        return cla;
    }


    @Override
    public IField[] getT() {
        return CommandUtil.addTD(F.values());
    }

    @Override
    public Object getF(IField f) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            return super.getF(pfi);
        }

        F fi = (F) f;
        switch (fi) {
            case zipCode:
                return getZipCode();
            case name1:
                return getName1();
            case name2:
                return getName2();
            case city:
                return getCity();
            case stateUS:
                return getStateUS();
            case country:
                return getCountry();
            case address1:
                return getAddress1();
            case address2:
                return getAddress2();
            case cType:
                return getCType();
            case NIP:
                return getNIP();
            case PESEL:
                return getPESEL();
            case docType:
                return getDocType();
            case docNumber:
                return getDocNumber();
            case firstName:
                return getFirstName();
            case lastName:
                return getLastName();
            case pTitle:
                return getPTitle();
            case mailAddress:
                return getMailAddress();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            super.setF(pfi, o);
            return;
        }

        F fi = (F) f;
        switch (fi) {
            case zipCode:
                setZipCode((String) o);
                break;
            case name1:
                setName1((String) o);
                break;
            case name2:
                setName2((String) o);
                break;
            case city:
                setCity((String) o);
                break;
            case stateUS:
                setStateUS((String) o);
                break;
            case country:
                setCountry((String) o);
                break;
            case address1:
                setAddress1((String) o);
                break;
            case address2:
                setAddress2((String) o);
                break;
            case cType:
                setCType((CustomerType) o);
                break;
            case NIP:
                setNIP((String) o);
                break;
            case PESEL:
                setPESEL((String) o);
                break;
            case docType:
                setDocType((IdentDocType) o);
                break;
            case docNumber:
                setDocNumber((String) o);
                break;
            case firstName:
                setFirstName((String) o);
                break;
            case lastName:
                setLastName((String) o);
                break;
            case pTitle:
                setPTitle((PersonTitle) o);
                break;
            case mailAddress:
                this.setMailAddress((String) o);
                break;
        }
    }

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

    public String getStateUS() {
        return stateUS;
    }

    public void setStateUS(final String stateUS) {
        this.stateUS = stateUS;
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

    public PersonalDataP getCustomerperson() {
        return customerperson;
    }

    public void setCustomerperson(PersonalDataP customerperson) {
        this.customerperson = customerperson;
    }

    public List<RemarkP> getRemarks() {
        return remarks;
    }

    public void setRemarks(final List<RemarkP> remarks) {
        this.remarks = remarks;
    }

    public List<PhoneNumberP> getPhones() {
        return phones;
    }

    public void setPhones(final List<PhoneNumberP> phones) {
        this.phones = phones;
    }

    public List<BankAccountP> getAccounts() {
        return accounts;
    }

    public void setAccounts(final List<BankAccountP> accounts) {
        this.accounts = accounts;
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
        if (s == null) {
            return;
        }
        if (customerperson == null) {
            customerperson = new PersonalDataP();
        }
        customerperson.setFirstName(s);
    }

    public void setLastName(final String s) {
        if (s == null) {
            return;
        }
        if (customerperson == null) {
            customerperson = new PersonalDataP();
        }
        customerperson.setLastName(s);
    }

    public void setPTitle(PersonTitle s) {
        if (s == null) {
            return;
        }
        if (customerperson == null) {
            customerperson = new PersonalDataP();
        }
        customerperson.setPTitle(s);
    }
}
