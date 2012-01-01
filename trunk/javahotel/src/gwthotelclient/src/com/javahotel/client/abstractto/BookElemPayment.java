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
package com.javahotel.client.abstractto;

import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.PaymentRowP;

/**
 * @author hotel
 * 
 */
@SuppressWarnings("serial")
public class BookElemPayment extends Compose2AbstractTo<BookElemP, PaymentRowP> {

    public BookElemPayment() {
        super(new BookElemP(), new PaymentRowP());
    }

    public BookElemPayment(BookElemP e, PaymentRowP p) {
        super(e, p);
    }

}
