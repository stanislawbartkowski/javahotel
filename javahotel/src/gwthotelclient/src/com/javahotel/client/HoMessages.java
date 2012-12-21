/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import com.google.gwt.i18n.client.Messages;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface HoMessages extends Messages {

    String noSleeps(int no);

    String noDict(String s, int no);

    String removeHoteDataQuestion(String hotelNo);

    String NoHotel(int no);

    String NotSupportedError(String e);

    String NotSupportedErrorS();

    String ResStateCannotBeNull();

    String AtLeastOneInvoiceData();

    String MustFindPeriod();

    String OkBooking(String resName);
}
