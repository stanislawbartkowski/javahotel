/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.jython.ui.server.datastore.IDateLineElem;
import com.jython.ui.server.datastore.IDateLineOp;

public class DateLineOp implements IDateLineOp {

    static {
        ObjectifyService.register(DateLineElem.class);
    }

    private DateLineElem findElemNow(Long id, Date dt) {
        LoadResult<DateLineElem> p = ofy().load().type(DateLineElem.class)
                .filter("elemid ==", id).filter("date ==", dt).first();
        return p.now();
    }

    @Override
    public IDateLineElem findElem(Long id, Date da) {
        return findElemNow(id, da);
    }

    @Override
    public void addormodifElem(final Long id, final Date da, final int numb,
            final String info) {
        final DateLineElem ele = findElemNow(id, da);
        ofy().transact(new VoidWork() {

            @Override
            public void vrun() {
                DateLineElem e = ele;
                if (e == null) {
                    e = new DateLineElem();
                    e.setI(id, da);
                }
                e.setDate(numb, info);
                ofy().save().entity(e).now();
            }
        });
    }

    @Override
    public void removeElem(final Long id, final Date da) {
        final DateLineElem ele = findElemNow(id, da);
        if (ele == null)
            return;
        ofy().transact(new VoidWork() {

            @Override
            public void vrun() {
                ofy().delete().entity(ele).now();
            }
        });

    }

    @Override
    public void clearAll() {
        Iterable<Key<DateLineElem>> allKeys = ofy().load()
                .type(DateLineElem.class).keys();
        ofy().delete().keys(allKeys).now();
    }

}
