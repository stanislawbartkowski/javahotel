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
package com.javahotel.nmvc.persist.dict;

import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.view.callback.CommonCallBack;

abstract class APersonHotelPersist implements IPersistHotelPerson {
    
    protected class CallBack extends CommonCallBack<Object> {

        private final ISuccess iRes;

        CallBack(ISuccess iRes) {
            this.iRes = iRes;
        }

        @Override
        public void onMySuccess(Object arg) {
            iRes.success();
        }
    }

}
