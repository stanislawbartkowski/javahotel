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

public enum SlotEventEnum {
    /** Validate action. */
    ValidateAction,

    /** Published after observed value has changed. */
    ChangeValue,

    /** Published after control button was clicked. */
    ClickButton,

    /** Published after widget has been created. */
    CallBackWidget,

    /** To do persist action */
    PersistSignal,

    /** List of slots, composite slot. */
    CompositeSlot,
    
    /** Getter. */
    GetterCaller,
}
