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
package com.mygwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.mailcommon.CListOfMailProperties;
import com.gwtmodel.table.mailcommon.CMailToSend;
import com.mygwt.common.data.TOItemRecord;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MyRemoteServiceAsync {

	void getListOfMailBoxes(AsyncCallback<CListOfMailProperties> callback);

	void sendMail(CMailToSend mail, AsyncCallback<String> callback);

    void getItemList(AsyncCallback<List<TOItemRecord>> callback);

    void ItemRecordOp(PersistTypeEnum op, TOItemRecord re,
            AsyncCallback<Void> callback);
}
