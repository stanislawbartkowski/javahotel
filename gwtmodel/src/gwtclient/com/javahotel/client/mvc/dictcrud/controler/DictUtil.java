/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.dictcrud.controler;

import com.javahotel.client.mvc.auxabstract.ANumAbstractTo;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.common.toobject.AbstractTo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DictUtil {

    private DictUtil() {
    }

    public static <T extends AbstractTo> void readList(ITableModel mo,
            List<T> col) {
        if (mo.getList() == null) { return; }
        for (AbstractTo a : mo.getList()) {
            ANumAbstractTo aa = (ANumAbstractTo) a;
            T t = (T) aa.getO();
            col.add(t);
        }
    }

    public static <T extends AbstractTo> void setList(ITableModel mo,
            List<T> col) {
        int lp = 0;
        List<ANumAbstractTo> out = new ArrayList<ANumAbstractTo>();
        if (col != null) {
            for (T t : col) {
                ANumAbstractTo a = new ANumAbstractTo(t, t.getT());
                a.setLp(new Integer(lp));
                lp++;
                out.add(a);
            }
        }
        mo.setList((ArrayList<? extends AbstractTo>) out);
    }
}
