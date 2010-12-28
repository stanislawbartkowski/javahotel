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

import java.util.List;

import com.javahotel.client.dialog.IMvcView;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface IGridModelView extends IMvcView, IGridBaseModel {

    void setRowVal(int row, List<? extends Object> vals);

    void setColVal(int col, List<? extends Object> vals);

    List<? extends Object> getRows(int row);

    List<? extends Object> getCols(int col);

}
