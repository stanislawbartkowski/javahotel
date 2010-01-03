/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.record.view.helper;

import java.math.BigDecimal;
import java.util.Date;

import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.command.IdentDocType;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.command.PersonTitle;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.ServiceType;
import com.javahotel.common.toobject.VatDictionaryP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ExtractFields {

    private ExtractFields() {
    }

    public static ILineField getE(final IRecordDef model, final IField f) {
        for (RecordField re : model.getFields()) {
            if (re.getFie().equals(f)) {
                return re.getELine();
            }
        }
        assert false : f.toString() + " cannot find in view";
        return null;
    }

    public static String getString(final IRecordDef model, IField f) {
        ILineField e = getE(model, f);
        return e.getVal();
    }

    public static void setFields(final IRecordDef model, final RecordModel a) {
        for (RecordField re : model.getFields()) {
            String s = a.getA().getS(re.getFie());
            re.getELine().setVal(s);
        }
    }

    public static void setString(final IRecordDef model, IField f, String val) {
        ILineField e = getE(model, f);
        e.setVal(val);
    }

    public static void fromViewToA(IField fie, ILineField eField, AbstractTo a) {
        Class<?> cla = a.getT(fie);
        Object val = null;
        if (cla == String.class) {
            val = eField.getVal();
        } else if (cla == DictionaryP.class) {
            String sta = eField.getVal();
            DictionaryP st = new DictionaryP();
            st.setName(sta);
            val = st;
        } else if (cla == Integer.class) {
            int no = eField.getIntVal();
            Integer va = new Integer(no);
            val = va;
        } else if (cla == BigDecimal.class) {
            BigDecimal b = eField.getDecimal();
            val = b;
        } else if (cla == ServiceType.class) {
            String sVal = eField.getVal();
            ServiceType se = ServiceType.valueOf(sVal);
            val = se;
        } else if (cla == VatDictionaryP.class) {
            VatDictionaryP vat = new VatDictionaryP();
            vat.setName(eField.getVal());
            val = vat;
        } else if (cla == Date.class) {
            Date de = eField.getDate();
            val = de;
        } else if (cla == CustomerType.class) {
            String sVal = eField.getVal();
            CustomerType cType = CustomerType.valueOf(sVal);
            val = cType;
        } else if (cla == IdentDocType.class) {
            String sVal = eField.getVal();
            IdentDocType cType = IdentDocType.valueOf(sVal);
            val = cType;
        } else if (cla == PaymentMethod.class) {
            String sVal = eField.getVal();
            PaymentMethod pMet = PaymentMethod.valueOf(sVal);
            val = pMet;
        } else if (cla == BookingStateType.class) {
            String sVal = eField.getVal();
            BookingStateType pMet = BookingStateType.valueOf(sVal);
            val = pMet;
        } else if (cla == PersonTitle.class) {
            String sVal = eField.getVal();
            PersonTitle pMet = PersonTitle.valueOf(sVal);
            val = pMet;
        } else if (cla == IdentDocType.class) {
            String sVal = eField.getVal();
            IdentDocType pMet = IdentDocType.valueOf(sVal);
            val = pMet;
        } else {
            assert false : cla.getName() + " not implemented";
        }
        a.setF(fie, val);
    }

    public static void extractFields(final IRecordDef model,
            final RecordModel aM) {
        AbstractTo a = aM.getA();
        for (RecordField re : model.getFields()) {
            IField fie = re.getFie();
            ILineField eField = re.getELine();
            fromViewToA(fie, eField, a);
        }
    }

    // public static void extractFields(final IRecordDef model, final
    // RecordModel a) {
    // for (RecordField re : model.getFields()) {
    // Class cla = a.getA().getT(re.getFie());
    // Object val = null;
    // if (cla == String.class) {
    // val = re.getELine().getVal();
    // } else if (cla == DictionaryP.class) {
    // String sta = re.getELine().getVal();
    // DictionaryP st = new DictionaryP();
    // st.setName(sta);
    // val = st;
    // } else if (cla == Integer.class) {
    // int no = re.getELine().getIntVal();
    // Integer va = new Integer(no);
    // val = va;
    // } else if (cla == BigDecimal.class) {
    // BigDecimal b = re.getELine().getDecimal();
    // val = b;
    // } else if (cla == ServiceType.class) {
    // String sVal = re.getELine().getVal();
    // ServiceType se = ServiceType.valueOf(sVal);
    // val = se;
    // } else if (cla == VatDictionaryP.class) {
    // VatDictionaryP vat = new VatDictionaryP();
    // vat.setName(re.getELine().getVal());
    // val = vat;
    // } else if (cla == Date.class) {
    // Date de = re.getELine().getDate();
    // val = de;
    // } else if (cla == CustomerType.class) {
    // String sVal = re.getELine().getVal();
    // CustomerType cType = CustomerType.valueOf(sVal);
    // val = cType;
    // } else if (cla == IdentDocType.class) {
    // String sVal = re.getELine().getVal();
    // IdentDocType cType = IdentDocType.valueOf(sVal);
    // val = cType;
    // } else if (cla == PaymentMethod.class) {
    // String sVal = re.getELine().getVal();
    // PaymentMethod pMet = PaymentMethod.valueOf(sVal);
    // val = pMet;
    // } else if (cla == BookingStateType.class) {
    // String sVal = re.getELine().getVal();
    // BookingStateType pMet = BookingStateType.valueOf(sVal);
    // val = pMet;
    // } else if (cla == PersonTitle.class) {
    // String sVal = re.getELine().getVal();
    // PersonTitle pMet = PersonTitle.valueOf(sVal);
    // val = pMet;
    // } else if (cla == IdentDocType.class) {
    // String sVal = re.getELine().getVal();
    // IdentDocType pMet = IdentDocType.valueOf(sVal);
    // val = pMet;
    // } else {
    // assert false : cla.getName() + " not implemented";
    // }
    // a.getA().setF(re.getFie(), val);
    // }
    // }
}
