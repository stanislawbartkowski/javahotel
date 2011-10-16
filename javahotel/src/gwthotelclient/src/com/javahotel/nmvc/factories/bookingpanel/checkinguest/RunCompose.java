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
package com.javahotel.nmvc.factories.bookingpanel.checkinguest;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controler.BoxActionMenuOptions;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.toobject.BookingP;

/**
 * @author hotel
 * 
 */
public class RunCompose {

    private final ComposeControllerFactory fFactory;
    private final SlotTypeFactory slTypeFactory;
    private static final String RESIGN_STRING = "RESIGN_STRING_GUEST";
    private final IRunComposeFactory iFactory;
    private ISlotable iSlo;

    /**
     * @return the iSlo
     */
    public ISlotable getiSlo() {
        return iSlo;
    }

    public RunCompose(IRunComposeFactory iFactory) {
        fFactory = GwtGiniInjector.getI().getComposeControllerFactory();
        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();
        this.iFactory = iFactory;
    }

    public interface IRunComposeFactory {

        IFormTitleFactory getTitle();

        ISlotable constructS(ICallContext iContext, IDataType dType,
                BookingP p, BoxActionMenuOptions bOptions, SlotType slType);
    }

    private class GetView implements IGetViewControllerFactory {

        private final IDataType dType;
        private final BookingP p;
        private BoxActionMenuOptions bOptions;
        private SlotType slType;

        /**
         * @param dType
         * @param p
         */
        GetView(IDataType dType, BookingP p) {
            super();
            this.dType = dType;
            this.p = p;
        }

        @Override
        public IComposeController construct(ICallContext iContext) {
            IComposeController iCon = fFactory.construct(iContext.getDType());
            ISlotable iSlo = iFactory.constructS(iContext, dType, p, bOptions,
                    slType);
            ComposeControllerType cType = new ComposeControllerType(iSlo,
                    dType, 0, 0);
            iCon.registerControler(cType);
            return iCon;
        }

    }

    public void runDialog(IDataType dType, final BookingP p, WSize wSize,
            boolean redirectResign, ISlotable cSlo) {
        final CellId pId = new CellId(0);
        TableDataControlerFactory tFa = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        GetView fControler = new GetView(dType, p);
        DataListParam dParam = new DataListParam(dType, null, null, null,
                iFactory.getTitle(), fControler, null);
        fControler.bOptions = dParam.getMenuOptions();
        if (redirectResign) {
            fControler.slType = slTypeFactory
                    .construct(new CustomStringDataTypeSlot(RESIGN_STRING,
                            dType));
            fControler.bOptions.setSlotType(
                    BoxActionMenuOptions.REDIRECT_RESIGN, fControler.slType);
        }

        DisplayListControlerParam cParam = tFa
                .constructParam(pId, dParam, cSlo);

        HModelData hBook = VModelDataFactory.construct(p);
        iSlo = tFa.constructDataControler(cParam,
                ClickButtonType.StandClickEnum.MODIFITEM, hBook, wSize);

    }

}
