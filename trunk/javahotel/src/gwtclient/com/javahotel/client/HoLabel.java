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
package com.javahotel.client;

import com.google.gwt.i18n.client.Constants;
import java.util.Map;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface HoLabel extends Constants {

    String commError();

    String pleaseWait();

    String productName();

    Map<String, String> Roles();

    Map<String, String> Services();

    Map<String, String> SeasonNames();

    Map<String, String> CustomerType();

    Map<String, String> PeriodType();

    Map<String, String> BookingStateType();

    Map<String, String> PaymentMethod();

    Map<String, String> HotelRoles();

    Map<String, String> PTitles();

    Map<String, String> DocTypes();

    Map<String, String> PanelLabelNames();

    String PasswordDifferent();

    String LogoutQuestion();

    Map<String, String> DictList();
    
    String Wersja();
    
    String Question();
    
    String LoginButton();
    
    String LoginName();
    
    String Password();
}
