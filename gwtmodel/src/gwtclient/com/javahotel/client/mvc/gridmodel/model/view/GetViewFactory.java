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
package com.javahotel.client.mvc.gridmodel.model.view;

import com.google.inject.Inject;
import com.javahotel.client.dialog.GridCellType;
import com.javahotel.view.gwt.grid.view.GridGwtGetViewFactory;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetViewFactory {

    
    private final GridGwtGetViewFactory gFactory; 
    
    @Inject
    public GetViewFactory(GridGwtGetViewFactory gFactory) {
        this.gFactory = gFactory;
    }

    public IGridView getView(GridCellType cType) {
//        public IGridView getGridView(IResLocator rI, GridCellType cType) {
//            GetViewFactory fa = HInjector.getI().getVFactory();
//            return fa.getGwtView(cType);
//            // return GetViewFactory.getGwtView(cType);
//        }

//        return rI.getView().getGridView(rI, cType);
        return gFactory.getGwtView(cType);
    }
}
