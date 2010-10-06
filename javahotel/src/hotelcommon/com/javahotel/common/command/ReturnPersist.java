/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.common.command;

import java.io.Serializable;
import java.util.List;

import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ReturnPersist implements Serializable {

    private LId id;
    private String idName;
    private List<ResDayObjectStateP> resState;
    private String errMessage;
    private String viewName;
    private int numberOf;

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public LId getId() {
        return id;
    }

    public void setId(LId id) {
        this.id = id;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public List<ResDayObjectStateP> getResState() {
        return resState;
    }

    public void setResState(List<ResDayObjectStateP> resState) {
        this.resState = resState;
    }

    public void setErrorMessage(final String s) {
        errMessage = s;
    }

    public String getErrorMessage() {
        return errMessage;
    }

    /**
     * @return the numberOf
     */
    public int getNumberOf() {
        return numberOf;
    }

    /**
     * @param numberOf the numberOf to set
     */
    public void setNumberOf(int numberOf) {
        this.numberOf = numberOf;
    }
}
