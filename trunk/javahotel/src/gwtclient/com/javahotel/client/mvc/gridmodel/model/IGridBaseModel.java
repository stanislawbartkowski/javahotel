/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface IGridBaseModel {

    void setRows(List<String> rows);

    void setCols(ColsHeader rowTitle,List<ColsHeader> cols);

    List<String> getSRow();

    List<ColsHeader> getSCol();

    int RowNo();

    int ColNo();

    void setEnable(boolean enable);

    void reset();
}
