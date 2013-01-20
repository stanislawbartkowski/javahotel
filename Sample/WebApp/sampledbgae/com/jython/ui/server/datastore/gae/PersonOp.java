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
import com.googlecode.objectify.Ref;
import com.jython.ui.server.datastore.IPerson;
import com.jython.ui.server.datastore.IPersonOp;

/**
 * @author hotel
 * 
 */
public class PersonOp implements IPersonOp {

    static {
        ObjectifyService.register(Person.class);
    }

    @Override
    public void savePerson(IPerson p) {
        ofy().save().entity((Person)p).now();
    }

    @Override
    public void clearAll() {
        Iterable<Key<Person>> allKeys = ofy().load().type(Person.class).keys();
        // Useful for deleting items
        ofy().delete().keys(allKeys).now();
    }

    @Override
    public List<IPerson> getAllPersons() {
        List<Person> li =ofy().load().type(Person.class).list();
        List<IPerson> outList = new ArrayList<IPerson>();
        outList.addAll(li);
        return outList;
    }

    @Override
    public void removePerson(IPerson p) {
        ofy().delete().entity((Person)p).now();

    }

    @Override
    public void changePerson(IPerson p) {
        ofy().save().entity((Person)p).now();
    }

    @Override
    public IPerson findPersonByNumb(String pNumb) {
        Ref<Person> p = ofy().load().type(Person.class)
                .filter("personNumb ==", pNumb).first();
        if (p == null) {
            return null;
        }
        return p.get();
    }

    @Override
    public IPerson construct() {
        return new Person();
    }

}
