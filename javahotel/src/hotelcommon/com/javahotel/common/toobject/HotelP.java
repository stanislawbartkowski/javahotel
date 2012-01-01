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
package com.javahotel.common.toobject;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class HotelP extends AbstractTo {

    public enum F implements IField {

        id, name, description, database
    }

    @Override
    public IField[] getT() {
        return F.values();
    }
    private Long id;
    private String name;
    private String description;
    private String database;

    @Override
    public Class<?> getT(IField f) {
        F fi = (F) f;
        Class<?> cla = String.class;
        switch (fi) {
            case id: cla = Long.class; break;
        }
        return cla;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(final String database) {
        this.database = database;
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
            case id:
                return getId();
            case name:
                return getName();
            case description:
                return getDescription();
            case database:
                return getDatabase();
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
                setName((String) o);
                break;
            case description:
                setDescription((String) o);
                break;
            case database:
                setDatabase((String) o);
                break;
        }
    }
}
