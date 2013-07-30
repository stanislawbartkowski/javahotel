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
package com.jython.ui.server.datastore.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.jython.ui.server.datastore.IDateRecord;
import com.jython.ui.server.datastore.IDateRecordOp;

public class DateRecordOp implements IDateRecordOp {

    static {
        ObjectifyService.register(DateRecord.class);
    }

    @Override
    public void clearAll() {
        Iterable<Key<DateRecord>> allKeys = ofy().load().type(DateRecord.class)
                .keys();
        // Useful for deleting items
        ofy().delete().keys(allKeys).now();
    }

    @Override
    public List<IDateRecord> getList() {
        List<DateRecord> li = ofy().load().type(DateRecord.class).list();
        List<IDateRecord> outList = new ArrayList<IDateRecord>();
        outList.addAll(li);
        return outList;
    }

    @Override
    public Long addRecord(IDateRecord re) {
        ofy().save().entity((DateRecord) re).now();
        return re.getId();
    }

    @Override
    public void changeRecord(IDateRecord re) {
        ofy().save().entity((DateRecord) re).now();
    }

    @Override
    public void removeRecord(Long id) {
        ofy().delete().type(DateRecord.class).id(id).now();
    }

    @Override
    public IDateRecord findRecord(Long id) {
        IDateRecord p = ofy().load().type(DateRecord.class).id(id).now();
        return p;
    }

    @Override
    public IDateRecord construct() {
        return new DateRecord();
    }

}
