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
package com.mygwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.mailcommon.CListOfMailProperties;
import com.gwtmodel.table.mailcommon.CMailToSend;
import com.mygwt.common.data.TOItemRecord;
import com.mygwt.common.data.TOMarkRecord;
import com.mygwt.common.data.ToEditRecord;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("testservice")
public interface MyRemoteService extends RemoteService {

    CListOfMailProperties getListOfMailBoxes();

    String sendMail(CMailToSend mail);

    List<TOItemRecord> getItemList();

    void ItemRecordOp(PersistTypeEnum op, TOItemRecord re);

    List<TOMarkRecord> getItemMarkList();

    void ItemMarkRecordOp(PersistTypeEnum op, TOMarkRecord re);
    
    List<ToEditRecord> getItemEditList();
    
    void ItemEditRecordOp(PersistTypeEnum op, ToEditRecord re);
    
    void saveEditItemList(List<ToEditRecord> rList);
}
