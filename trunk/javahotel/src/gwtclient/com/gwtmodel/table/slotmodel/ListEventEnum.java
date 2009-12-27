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
package com.gwtmodel.table.slotmodel;

public enum ListEventEnum {

    /** After adding new item. */
    AfterAdd,
    
    /** After modifying item. */
    AfterModify,
    
    /** After removing item. */
    AfterRemove,
    
    /** Refresh list. */
    Refresh,
    
    /** Read list. */
    ReadList,
    
    /** List read with success. */
    ReadListSuccess,
    
    /** Get list data (datalist,choosed,size). */
    GetListData,
}
