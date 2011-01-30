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
package com.javahotel.client.mvc.util;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.crud.controler.ICrudChooseTable;
import com.javahotel.client.mvc.dictcrud.controler.ICrudControlerDialog;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.table.model.ITableFilter;
import com.javahotel.client.widgets.popup.PopupUtil;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ChooseCrudDialog {

    private final DialogBox dBox;

    public ChooseCrudDialog(final IResLocator rI,
            final DictData da, final ICrudChooseTable c,
            final Widget w, final ITableFilter iF) {

        RecordAuxParam aux = new RecordAuxParam();
        aux.setIChoose(c);
        aux.setIFilter(iF);
        ICrudControlerDialog iD = HInjector.getI().getDictCrudControlerFactory().getCrudD(
                da, aux, null,null);
        dBox = (DialogBox) iD.getMWidget().getWidget();
        PopupUtil.setPos(dBox, w);
        iD.show();
    }

    /**
     * @return the dBox
     */
    public DialogBox getDBox() {
        return dBox;
    }
}
