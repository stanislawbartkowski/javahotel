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
package com.javahotel.client.abstractto;

import java.util.List;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.VSField;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.CreateList;
import com.javahotel.client.types.VField;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.InvoiceP;

/**
 * @author hotel
 * 
 */
public class InvoicePVData implements IVModelData {

    private final InvoiceP p;

    public InvoicePVData() {
        p = new InvoiceP();
    }

    public InvoicePVData(InvoiceP p) {
        this.p = p;
    }

    private IField getI(IVField fie) {
        if (fie instanceof VField) {
            VField f = (VField) fie;
            IField fi = f.getFie();
            return fi;
        }
        return null;
    }

    @Override
    public Object getF(IVField fie) {
        IField fi = getI(fie);
        if (fi != null) {
            return p.getF(fi);
        }
        VSField vs = (VSField) fie;
        String s = vs.getId();
        return p.getInvoiceD().getdFields().get(s);
    }

    @Override
    public void setF(IVField fie, Object o) {
        IField fi = getI(fie);
        if (fi != null) {
            p.setF(fi, o);
            return;
        }
        VSField vs = (VSField) fie;
        String s = vs.getId();
        p.getInvoiceD().getdFields().put(s, o);
    }

    @Override
    public boolean isValid(IVField fie) {
        return true;
    }

    @Override
    public List<IVField> getF() {
        IInvoicePListOfVFields i = HInjector.getI().getIInvoicePListOfVFields();
        List<IVField> li = i.getListOfFields();
        List<IVField> c = CreateList.getF(p);
        li.addAll(c);
        return li;
    }

    /**
     * Return InvoiceP element
     * 
     * @return the p
     */
    public InvoiceP getP() {
        return p;
    }

    @Override
    public Object getCustomData() {
        return null;
    }

    @Override
    public void setCustomData(Object o) {        
    }

}
