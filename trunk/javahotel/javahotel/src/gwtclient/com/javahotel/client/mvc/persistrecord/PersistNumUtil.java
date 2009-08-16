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
package com.javahotel.client.mvc.persistrecord;

import com.javahotel.client.mvc.auxabstract.NumAbstractTo;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.common.util.GetMaxUtil;
import java.util.Collection;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PersistNumUtil {

    private PersistNumUtil() {
    }

    interface IGenNextLp {

        Integer getNext();
    }

    static class GenNextLp implements IGenNextLp {

        private final ITableModel mo;

        GenNextLp(ITableModel mo) {
            this.mo = mo;
        }

        public Integer getNext() {
            Collection<NumAbstractTo> col = (Collection<NumAbstractTo>) mo.getList();
            Integer i = GetMaxUtil.getMaxL(col);
            if (i == null) {
                i = new Integer(0);
            }
            return new Integer(i.intValue() + 1);
        }
    }

    static NumAbstractTo persist(final int action, final ITableModel mo,
            final IGenNextLp gNext, final NumAbstractTo a) {
        Collection<NumAbstractTo> col = (Collection<NumAbstractTo>) mo.getList();
        Integer i;
        NumAbstractTo res = null;
        switch (action) {
            case IPersistAction.ADDACION:
                i = gNext.getNext();
                a.setLp(i);
                col.add(a);
                res = a;
                break;
            case IPersistAction.DELACTION:
            case IPersistAction.MODIFACTION:
                i = a.getLp();
                for (NumAbstractTo aa : col) {
                    if (aa.getLp().intValue() == i.intValue()) {
                        if (action == IPersistAction.MODIFACTION) {
                            aa.setO(a.getO());
                        } else {
                            col.remove(aa);
                        }
                        res = aa;
                        break;
                    }
                }
        }
        return res;
    }
}
