/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.controler.ui;

import com.google.gwt.user.client.ui.PopupPanel;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.view.util.PopupUtil;

/**
 * @author hotel
 * 
 */
public class ChangePageSizeDialog {

    private ChangePageSizeDialog() {
    }

    public interface IChangeAction {

        void toDefault();

        void toChange(int newsize);
    }

    private static class FinishClick implements ICommand {

        private final PopupPanel p;

        FinishClick(PopupPanel p) {
            this.p = p;
        }

        @Override
        public void execute() {
            p.hide();
        }

    }

    private static class ChangeToDefault implements ICommand {

        private final IChangeAction i;

        ChangeToDefault(IChangeAction i) {
            this.i = i;
        }

        @Override
        public void execute() {
            i.toDefault();
        }

    }

    private static class ChangeSize implements ICommand {

        private final IChangeAction i;
        private final ChangePageSize pUp;

        ChangeSize(IChangeAction i, ChangePageSize pUp) {
            this.i = i;
            this.pUp = pUp;
        }

        @Override
        public void execute() {
            String s = pUp.currentPageSize.getText();
            if (!CUtil.OkInteger(s)) {
                return;
            }
            int si = CUtil.getInteger(s);
            if (si <= 0) {
                return;
            }
            i.toChange(si);
        }

    }

    public static void doDialog(WSize w, int startSize, int currentSize,
            IChangeAction i) {
        ChangePageSize pUp = new ChangePageSize();
        pUp.startPageSize.setText(CUtil.NumbToS(startSize));
        pUp.currentPageSize.setText(CUtil.NumbToS(currentSize));
        pUp.finishClick = new FinishClick(pUp);
        pUp.restoreClick = new ChangeToDefault(i);
        pUp.changeClick = new ChangeSize(i, pUp);
        PopupUtil.setPos(pUp, w);
        pUp.show();
    }

}
