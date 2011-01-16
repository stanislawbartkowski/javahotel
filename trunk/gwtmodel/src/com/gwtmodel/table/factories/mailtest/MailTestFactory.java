/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.factories.mailtest;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
public class MailTestFactory {

    private final TablesFactories tFactories;

    @Inject
    public MailTestFactory(TablesFactories tFactories) {
        this.tFactories = tFactories;
    }

    public IMailTest construct(IDataType dt) {
//        ControlButtonFactory bFactory = tFactories.getControlButtonFactory();
        List<ControlButtonDesc> lButton = new ArrayList<ControlButtonDesc>();
        ControlButtonDesc dMail = new ControlButtonDesc("Wyślij",
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT));
        lButton.add(dMail);
//        lButton.add(bFactory.constructButt(ClickButtonType.StandClickEnum.RESIGN));
        ListOfControlDesc ldesc = new ListOfControlDesc(lButton);

        return new MailTest(dt, ldesc);
    }

    public IMailTest construct(IDataType dt, ListOfControlDesc ldesc) {
        return new MailTest(dt, ldesc);
    }
}
