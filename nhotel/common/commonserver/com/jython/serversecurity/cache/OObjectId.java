/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jython.serversecurity.cache;

import java.io.Serializable;

import com.jython.serversecurity.AppInstanceId;

public class OObjectId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String object;

    private Long id;

    private AppInstanceId instanceId;

    private String userName;

    public AppInstanceId getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(AppInstanceId instanceId) {
        this.instanceId = instanceId;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
