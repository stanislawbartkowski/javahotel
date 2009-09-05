/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.panelcommand;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.controller.onerecord.RecordFa;
import com.javahotel.client.mvc.controller.onerecord.RecordFaParam;
import com.javahotel.common.command.DictType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class CustTest extends AbstractPanelCommand {

    private RecordFa fa;
    private final IResLocator rI;

    CustTest(IResLocator rI) {
        this.rI = rI;

    }

    public void beforeDrawAction(ISetGwtWidget iSet) {
        RecordFaParam pa = new RecordFaParam();
        pa.setNewchoosetag(true);
        fa = new RecordFa(rI,
                new DictData(DictType.CustomerList), pa);
        fa.setModifWidgetStatus(true);
        fa.setNewWidgetStatus(true);
        iSet.setGwtWidget(fa.getMWidget());
    }

    public void drawAction() {
    }
}
