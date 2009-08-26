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
public class PersonP extends AbstractTo {

    private Long id;
    private String name;

    public enum F implements IField {

        id, name;
    }

    @Override
    public Class<?> getT(final IField f) {
        Class<?> cla = String.class;
        F fi = (F) f;
        switch (fi) {
            case id:
                cla = Long.class;
                break;
        }
        return cla;
    }

    @Override
    public IField[] getT() {
        return F.values();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
            case id:
                return getId();
            case name:
                return getName();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        F fi = (F) f;
        switch (fi) {
            case id:
                setId((Long) o);
                break;
            case name:
                setName((String) name);
                break;
        }
    }
}
