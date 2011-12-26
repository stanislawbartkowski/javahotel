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
package com.javahotel.common.command;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public enum RType {

    /** Get list of all hotels defined. */
    AllHotels, 
    /** Get list of all database defined. */
    DataBases, 
    /** Get list of all persons defined. */
    AllPersons, 
    /** Get list of all roles for person and hotel. */
    PersonHotelRoles, 
    /** Get list of all object if type dictionary. */
    ListDict,
    /** Get reservation related to one object. */
    ResObjectState, 
    /** Get list of all advance payments */
    DownPayments, 
}
