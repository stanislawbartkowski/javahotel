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
package com.gwtmodel.table.attachlist;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.attachlist.UploadFile.ISubmitRes;
import com.gwtmodel.table.common.IConstUtil;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.ValidateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
class PersistAttach extends AbstractSlotContainer implements IDataPersistAction {

    private final UploadFile u;

    private class ValidateA implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            final PersistTypeEnum e = slContext.getPersistType();
            AttachData eDa = new AttachData();
            IVModelData da = slContainer.getGetterIVModelData(dType, GetActionEnum.GetModelToPersist, eDa);
            final AttachData dData = (AttachData) da;
            if (e != PersistTypeEnum.ADD) {
                publish(dType, DataActionEnum.PersistDataAction, dData, e);
                return;
            }
            List<IVField> emptyS = new ArrayList<IVField>();
            emptyS.add(AttachDataField.vfilename);
            List<InvalidateMess> eList = ValidateUtil.checkEmpty(dData, emptyS);
            if (eList != null) {
                publish(dType, DataActionEnum.InvalidSignal,
                        new InvalidateFormContainer(eList));
                return;
            }

            ISubmitRes ha = new ISubmitRes() {

                @Override
                public void execute(String res) {
                    if (res.contains(IConstUtil.UPLOADFILEERROR)) {
                        Utils.errAlert(LogT.getT().errrorUploadingFile());
                        return;
                    }
                    int i = res.indexOf(IConstUtil.FILENAMEID);
                    if (i == -1) {
                        Utils.errAlert(LogT.getT().errrorUploadingFile());
                        return;
                    }
                    String f = res.substring(i + IConstUtil.FILENAMEID.length() + 1);
                    int i1 = f.indexOf('<');
                    String fName = f.substring(0, i1);
                    dData.setTempFileName(fName);
                    fName = dData.getFileName();
                    int l = fName.lastIndexOf('\\');
                    if (l == -1) {
                        l = fName.lastIndexOf('/');
                    }
                    if (l != -1) {
                        fName = fName.substring(l);
                        dData.setFileName(fName);
                    }

                    publish(dType, DataActionEnum.PersistDataAction, dData, e);
                }
            };

            u.submit(ha);
        }
    }

    PersistAttach(IDataType dType, UploadFile u) {
        this.u = u;
        this.dType = dType;
        registerSubscriber(dType, DataActionEnum.ValidateAction,
                new ValidateA());
    }
}
