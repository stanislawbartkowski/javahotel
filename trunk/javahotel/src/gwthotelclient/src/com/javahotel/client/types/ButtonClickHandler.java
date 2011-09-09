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
package com.javahotel.client.types;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;

/**
 * @author hotel Handler for clicking at the button. Display information in
 *         read-only mode for one object
 * 
 */
public class ButtonClickHandler<T extends AbstractTo> implements ClickHandler {

    private final T r;
    private final DictType d;

    public ButtonClickHandler(final T r, DictType d) {
        this.r = r;
        this.d = d;
    }

    @Override
    public void onClick(ClickEvent event) {
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        DisplayListControlerParam lParam = tFactory
                .constructParam(new DataType(d));
        IVModelData peData = VModelDataFactory.construct(r);
        WSize wSize = new WSize(event.getRelativeElement());
        ISlotable dControler = tFactory.constructDataControler(lParam,
                ClickButtonType.StandClickEnum.SHOWITEM, peData, wSize);
// redundant        
//        dControler.startPublish(null);
       
    }

}
