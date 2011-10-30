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
package com.javahotel.nmvc.factories.advancepayment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.VSField;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType.StandClickEnum;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.view.table.IGetCellValue;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.util.SumUtil;
import com.javahotel.nmvc.factories.booking.util.BookingInfo;

/**
 * @author hotel
 * 
 */
public class AdvancePayment extends AbstractSlotContainer {

    private final ISlotable i;

    public final static String CHOOSE_STRING = "ADVANCE_PAYMENT_CHOOSE_STRING";
    public final static String PAY_STRING = "ADVANCE_PAYMENT_PAY_STRING";

    interface PaymentCell extends SafeHtmlTemplates {
        @Template("<string>{0}</strong>")
        SafeHtml input(String suma);
    }

    private final static PaymentCell templateClass = GWT
            .create(PaymentCell.class);

    private boolean isShow(IVField fie) {
        if (!(fie instanceof VSField)) {
            return false;
        }
        VSField f = (VSField) fie;
        return (f.getId().equals(CHOOSE_STRING));

    }

    private final class GetCell implements IGetCellValue {

        @Override
        public SafeHtml getValue(IVModelData v, IVField fie) {
            if (isShow(fie)) {
                SafeHtmlBuilder builder = new SafeHtmlBuilder();
                builder.appendHtmlConstant("<strong>");
                builder.appendEscaped("Pokaż");
                builder.appendHtmlConstant("</strong>");
                return builder.toSafeHtml();
            }
            BookingP p = DataUtil.getData(v);
            BigDecimal sum = SumUtil.sumPayment(p);
            String s = FUtils.getBigDecimalS(sum, fie);
            return templateClass.input(s);
        }

    }

    private class ClickCust implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {

            WChoosedLine w = SlU.getWChoosedLine(slContext);
            IVModelData v = SlU.getVDataByW(dType, i, w);
            BookingP d = DataUtil.getData(v);
            IVField fie = w.getvField();
            if (isShow(fie)) {
                VerticalPanel ve = new VerticalPanel();
                BookingInfo bInfo = new BookingInfo(d);
                ve.add(bInfo);
                new ClickPopUp(w.getwSize(), ve);
            } else {
                new AddPayment().addPayment(d, w.getwSize(), i);
            }

        }

    }

    public AdvancePayment(IDataType dType, CellId panelId) {
        this.dType = dType;
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();

        List<ControlButtonDesc> dButton = new ArrayList<ControlButtonDesc>();
        dButton.add(cButtonFactory.constructButt(StandClickEnum.FILTRLIST));
        dButton.add(cButtonFactory.constructButt(StandClickEnum.FIND));

        ListOfControlDesc cList = new ListOfControlDesc(dButton);
        DisplayListControlerParam dList = tFactory.constructParam(dType, cList,
                panelId, null, new GetCell());
        i = tFactory.constructDataControler(dList);
        i.getSlContainer().registerSubscriber(dType,
                DataActionEnum.TableCellClicked, new ClickCust());

        // AddPayment publish AddPayment.addType
        // redirect to publish type
        SlotType sFrom = slTypeFactory.construct(AddPayment.dType,
                DataActionEnum.RefreshAfterPersistActionSignal);
        SlotType sTo = slTypeFactory.construct(dType,
                DataActionEnum.RefreshAfterPersistActionSignal);
        i.getSlContainer().registerRedirector(sFrom, sTo);

        this.setSlContainer(i);
    }

    @Override
    public void startPublish(CellId cellId) {
        i.startPublish(cellId);
    }

}
