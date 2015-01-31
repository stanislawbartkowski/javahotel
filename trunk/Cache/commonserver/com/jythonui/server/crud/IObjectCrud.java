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
package com.jythonui.server.crud;

import java.util.List;

import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.shared.PropDescription;

public interface IObjectCrud<T extends PropDescription> {

    List<T> getList(OObjectId object);

    T addElem(OObjectId object, T elem);

    void changeElem(OObjectId object, T elem);

    void deleteElem(OObjectId object, T elem);

    T findElem(OObjectId object, String name);

    T findElemById(OObjectId object, Long id);
}
