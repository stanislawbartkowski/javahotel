/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
class AttachViewFactory {

    public FormLineContainer construct(ICallContext iContext, VListHeaderContainer listHeader) {
        EditWidgetFactory eFactory = GwtGiniInjector.getI().getEditWidgetFactory();
        List<FormField> di = new ArrayList<FormField>();
        IVField vcomment = new AttachDataField(AttachDataField.F.COMMENT);
        IVField vfilename = new AttachDataField(AttachDataField.F.FILENAME);
        IVField vadddate = new AttachDataField(AttachDataField.F.ADDDATE);
        IFormLineView dComment = eFactory.constructEditWidget(vcomment);
        IFormLineView dadd = eFactory.construcDateBoxCalendar(vadddate);
        IFormLineView dfilename = eFactory.constructTextField(vfilename);
        VListHeaderDesc hCom = listHeader.getHeader(AttachDataField.vcomment);
        VListHeaderDesc hdataDod = listHeader.getHeader(AttachDataField.vadddate);
        VListHeaderDesc hFileName = listHeader.getHeader(AttachDataField.vfilename);

        di.add(new FormField(hCom.getHeaderString(), dComment, null, false, true));
        di.add(new FormField(hdataDod.getHeaderString(), dadd, null, true, true));
        di.add(new FormField(hFileName.getHeaderString(), dfilename, null, true, true));
        return new FormLineContainer(di);
    }
}
