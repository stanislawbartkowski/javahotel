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
package com.javahotel.client.mvc.crud.controler;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.mvc.validator.IErrorMessage;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface ICrudRecordControler {

    void showDialog(int action, ICrudView view, Widget w);

    void hideDialog(ICrudView view);

    void showInvalidate(int action, ICrudView view, 
            IErrorMessage vali);

    void showInvalidateAux(int action, ICrudView view,
            IErrorMessage vali);
}
