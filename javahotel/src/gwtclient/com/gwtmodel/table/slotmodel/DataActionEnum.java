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

public enum DataActionEnum {

    /** Draw list / DataListType. */
    DrawListAction,

    /** Change view mode / PersistTypeEnum . */
    ChangeViewFormModeAction,

    /** Change view mode / PersistTypeEnum . */
    ChangeViewComposeFormModeAction,

    /** Change view mode to invalid state / InvalidateFormContainer. */
    ChangeViewFormToInvalidAction,

    /** Draw form. / IVModeData. */
    DrawViewFormAction,

    /** Draw form. / IVModeData. */
    DrawViewComposeFormAction,

    /** ValidateActionSignal / IVModelData / PersistTypeEnum. */
    ValidateAction,

    /** InvalidSignal / InvalidateFormContainer / PersistEnumType. */
    InvalidSignal,

    /** ValidSignal / PersistEnumType . */
    ValidSignal,

    /** Read list / DataListType. */
    ReadListAction,

    /** List read successfully. */
    ReadListPersistedSignal,

    /** Persist data / IVModelData / PersistEnumType. */
    PersistDataAction,

    /** Persist data successful. */
    PersistDataSuccessSignal,

    /** List read successfully. / DataListType */
    ListReadSuccessSignal,

    /** Data persisted successfully / PersisEnumType . */
    DataPersistedSuccessSignal,

    /** RefreshAfterPersistSignal / PersistEnumType. */
    RefreshAfterPersistActionSignal,

    /** Validate compose form / PersistEnumType . */
    ValidateComposeFormAction,

    /** Compose form valid . */
    ValidComposeFormSuccessSignal,

    /** Persist compose form / PersistEnumType. */
    PersistComposeFormAction,

    /** Persist action successful / PersistEnumType */
    PersistComposeFormSuccessSignal

}
