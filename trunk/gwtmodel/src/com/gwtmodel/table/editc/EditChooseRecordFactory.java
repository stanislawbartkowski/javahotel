/*
 *  Copyright 2011 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.editc;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.LogT;

/**
 * 
 * @author hotel
 */
public class EditChooseRecordFactory {

    private class ChangeObject implements IChangeObject {

        private final int what;
        private final boolean set;
        private final IGWidget w;

        @Override
        public String toString() {
            String changeS = Utils.LToS(what == IChangeObject.NEW);
            String setS = Utils.LToS(set);
            return LogT.getT().clickChangeNewLog(changeS, setS);
        }

        ChangeObject(int what, boolean set, IGWidget w) {
            this.what = what;
            this.set = set;
            this.w = w;
        }

        @Override
        public int getWhat() {
            return what;
        }

        @Override
        public boolean getSet() {
            return set;
        }

        @Override
        public IGWidget getW() {
            return w;
        }
    }

    public IChangeObject constructChangeObject(int what, boolean set, IGWidget w) {
        return new ChangeObject(what, set, w);
    }

    public IChangeObject constructChangeObject(int what, boolean set) {
        return new ChangeObject(what, set, null);
    }

    public IEditChooseRecordContainer constructEditChooseRecord(
            ICallContext iContext, IDataType publishdType) {
        return new EditChooseRecordContainer(iContext, publishdType, false);
    }

    public IEditChooseRecordContainer constructEditChooseRecordWithoutForm(
            ICallContext iContext, IDataType publishdType) {
        return new EditChooseRecordContainer(iContext, publishdType, true);
    }

    public IChooseRecordContainer constructChooseRecord(IDataType dType) {
        return new ChooseFromList(dType, dType);
    }

}
