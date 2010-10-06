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
package com.gwtmodel.table.stringlist;

import com.gwtmodel.table.common.GetMaxUtil;
import com.gwtmodel.table.common.GetMaxUtil.IGetLp;

public class AddStringNext {

    private AddStringNext() {

    }

    static void addNext(MemoryStringTable dList, AbstractStringE e) {

        IGetLp<AbstractStringE> iget = new IGetLp<AbstractStringE>() {

            public Long getLp(AbstractStringE t) {
                return t.getLp();
            }
        };
        Long ma = GetMaxUtil.getNextMax(dList.getLi(), iget, new Long(1));
        e.setLp(ma);
        dList.getLi().add(e); // not dList.append !
    }

}
