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
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.table.model.ITableModel;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PersistRecordPhoneBank implements IPersistRecord {

    public void persist(int action, RecordModel a, IPersistResult ires) {
        NumAbstractTo an = (NumAbstractTo) a.getA();
        ITableModel mo = (ITableModel) a.getAuxData();
        assert mo.getList() != null;
        NumAbstractTo ares = (NumAbstractTo) PersistNumUtil.persist(action,
                mo, new PersistNumUtil.GenNextLp(mo), an);
        CallSuccess.callI(ires, action, ares,null);
    }

}
