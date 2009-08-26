/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.common.toobject;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class StringP extends AbstractTo {

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public enum F implements IField {

        name
    }

    @Override
    public Class getT(final IField f) {
        Class cla = String.class;
        return cla;
    }

    @Override
    public IField[] getT() {
        return F.values();
    }
    private String name;

    @Override
    public Object getF(IField f) {
        return getName();
    }

    @Override
    public void setF(IField f, Object o) {
        setName((String) o);
    }
}
