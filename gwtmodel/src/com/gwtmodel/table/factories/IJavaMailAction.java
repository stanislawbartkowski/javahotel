/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.factories;

import com.gwtmodel.table.slotmodel.ISlotable;

/**
 *
 * @author perseus
 */
public interface IJavaMailAction extends ISlotable {

    String ACTIONGETLISTMAILPROPERTIES = "ACTION_GET_MAIL_PROPERTIES";
    String SENDLISTMAILPROPERTIES = "SEND_ACTION_GET_MAIL_PROPERTIES";
    String GET_LIST_OF_TEMPLATES = "GET_LIST_OF_TEMPLATES";
    String GET_MAIL_TEMPLATE = "GET_MAIL_TEMPLAITE";
    String SEND_MAIL = "SEND_MAIL";
    String SEND_RESULT = "SEND_RESULT";
}
