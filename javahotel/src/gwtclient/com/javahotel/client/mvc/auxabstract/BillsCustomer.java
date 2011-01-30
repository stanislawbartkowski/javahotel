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
package com.javahotel.client.mvc.auxabstract;

import java.util.List;

import com.javahotel.common.command.BillEnumTypes;
import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;
import com.javahotel.types.LId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class BillsCustomer extends AbstractTo {

    private final CustomerP cust;
    private String name;
    private String desc;
    private BillEnumTypes billType;
    private LId id;
    private List<AddPaymentP> aP;
    private String oPrice;

    public BillsCustomer(DictionaryP dict, CustomerP cust) {
        this.name = dict.getName();
        this.desc = dict.getDescription();
        this.cust = cust;
        this.id = dict.getId();
    }

    /**
     * @return the cust
     */
    public CustomerP getCust() {
        return cust;
    }

    /**
     * @return the billType
     */
    public BillEnumTypes getBillType() {
        return billType;
    }

    /**
     * @param billType the billType to set
     */
    public void setBillType(BillEnumTypes billType) {
        this.billType = billType;
    }

    /**
     * @return the id
     */
    public LId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(LId id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the aP
     */
    public List<AddPaymentP> getAP() {
        return aP;
    }

    /**
     * @param aP the aP to set
     */
    public void setAP(List<AddPaymentP> aP) {
        this.aP = aP;
    }

    /**
     * @return the oPrice
     */
    public String getOPrice() {
        return oPrice;
    }

    /**
     * @param oPrice the oPrice to set
     */
    public void setOPrice(String oPrice) {
        this.oPrice = oPrice;
    }

    public enum F implements IField {

        billType, name, descr,oPrice
    };

    @Override
    public Object getF(IField f) {
        if (getCust().isField(f)) {
            return getCust().getF(f);
        }
        F fie = (F) f;
        switch (fie) {
            case billType:
                return getBillType();
            case name:
                return getName();
            case descr:
                return getDesc();
            case oPrice:
                return getOPrice();
        }
        return null;
    }

    @Override
    public Class getT(IField f) {
        Class cla = null;
        if (getCust().isField(f)) {
            cla = cust.getT(f);
            return cla;
        }
        F fie = (F) f;
        switch (fie) {
            case billType:
                cla = BillEnumTypes.class;
                break;
            case name:
            case descr:
            case oPrice:
                cla = String.class;
                break;
        }
        return cla;
    }

    @Override
    public void setF(IField f, Object o) {
        if (getCust().isField(f)) {
            cust.setF(f, o);
            return;
        }
        F fie = (F) f;
        switch (fie) {
            case billType:
                setBillType((BillEnumTypes) o);
                break;
            case name:
                setName((String) o);
                break;
            case descr:
                setDesc((String) o);
                break;
            case oPrice:
                setOPrice((String) o);
                break;
        }
    }

    @Override
    public IField[] getT() {
        return CommandUtil.addT(F.values(), cust.getT());
    }
}
