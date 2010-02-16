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
package com.gwtmodel.table.stringlist;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.persist.IVModelDataEquable;

public abstract class AbstractStringE implements IVModelDataEquable {

    private long lp;

    public long getLp() {
        return lp;
    }

    public void setLp(long lp) {
        this.lp = lp;
    }

    private String s;

    public boolean eq(IVModelDataEquable o) {
        AbstractStringE e = (AbstractStringE) o;
        return lp == e.lp;
    }

    public String getS(IVField fie) {
        return s;
    }

    public boolean isEmpty(IVField fie) {
        return CUtil.EmptyS(s);
    }

    public void setS(IVField fie, String s) {
        this.s = s;
    }

}
