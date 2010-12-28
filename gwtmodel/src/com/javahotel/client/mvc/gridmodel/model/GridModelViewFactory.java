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
package com.javahotel.client.mvc.gridmodel.model;

import com.google.inject.Inject;
import com.javahotel.client.dialog.GridCellType;
import com.javahotel.client.mvc.gridmodel.model.view.GetViewFactory;
import com.javahotel.client.mvc.gridmodel.model.view.IGridView;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GridModelViewFactory {

    private final GetViewFactory gFactory;
    
    @Inject
    public GridModelViewFactory(GetViewFactory gFactory) {
        this.gFactory = gFactory;
    }

    public IGridModelView getModel(GridCellType gType) {

//        IGridView g = GetViewFactory.getView(rI, gType);
        IGridView g = gFactory.getView(gType);

        return new GridModelView(g);
    }
}
