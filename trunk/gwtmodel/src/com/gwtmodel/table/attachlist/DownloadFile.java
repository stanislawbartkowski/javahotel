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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.IConstUtil;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.view.util.ClickPopUp;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author perseus
 */
class DownloadFile extends AbstractSlotContainer {

    private IGWidget w;

    DownloadFile(IDataType dType, ClickButtonType bType) {
        this.dType = dType;
        this.getSlContainer().registerSubscriber(bType, new DownloadFileSig());
        this.getSlContainer().registerSubscriber(
                IAttachDataView.SENDDOWNLOADFILENAME, new GetTempFile());
    }

    private String getLink(AttachData da) {
        String u = GWT.getHostPageBaseURL() + "/downLoadHandler";
        Map<String, String> val = new HashMap<String, String>();
        val.put(IConstUtil.TEMPORARYFILENAME, da.getTempFileName());
        String link = Utils.createURL(u, IConstUtil.FILENAMEID,
                da.getFileName(), val);
        return link;
    }

    private class DownLoad extends Composite {

        private final VerticalPanel vp = new VerticalPanel();

        DownLoad(AttachData da) {
            String link = getLink(da);
            Anchor a = new Anchor(MM.getL().ClockToDownload(), link);
            vp.add(a);
            initWidget(vp);
        }
    }

    private class GetTempFile implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IAttachDataView.ICustomData cu = (IAttachDataView.ICustomData) slContext
                    .getCustom();
            AttachData a = cu.getA();
            ClickPopUp pUp = new ClickPopUp(w.getGWidget(), new DownLoad(a));
            pUp.setVisible(true);
        }
    }

    private class AttachD implements IAttachDataView.ICustomData {

        private final AttachData da;

        AttachD(AttachData da) {
            this.da = da;
        }

        @Override
        public AttachData getA() {
            return da;
        }
    }

    class DownloadFileSig implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData da = getGetterIVModelData(dType,
                    GetActionEnum.GetListLineChecked);
            if (da == null) {
                return;
            }
            w = slContext.getGwtWidget();
            AttachData a = (AttachData) da;
            publish(IAttachDataView.CALLFORDOWNLOADEDFILENAME, new AttachD(a));
        }
    }
}
