/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.persist.dict;

import com.gwtmodel.table.common.PersistTypeEnum;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface IPersistResult {

    class PersistResultContext {

        private final PersistTypeEnum action;
        private final AbstractTo a;
        private final ReturnPersist ret;

        private Object auxContext;

        PersistResultContext(PersistTypeEnum action, AbstractTo a,
                ReturnPersist ret) {
            this.action = action;
            this.a = a;
            this.ret = ret;
            auxContext = null;
        }

        /**
         * @return the action
         */
        public PersistTypeEnum getAction() {
            return action;
        }

        /**
         * @return the a
         */
        public AbstractTo getA() {
            return a;
        }

        /**
         * @return the ret
         */
        public ReturnPersist getRet() {
            return ret;
        }

        /**
         * @return the auxContext
         */
        public Object getAuxContext() {
            return auxContext;
        }

        /**
         * @param auxContext
         *            the auxContext to set
         */
        public void setAuxContext(Object auxContext) {
            this.auxContext = auxContext;
        }

    }

    void success(PersistResultContext re);
}
