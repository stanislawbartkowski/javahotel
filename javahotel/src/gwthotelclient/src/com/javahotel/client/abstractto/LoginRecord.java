/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

package com.javahotel.client.abstractto;

import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class LoginRecord extends AbstractTo {

    private String login;
    private String password;

    private String confpassword;
    private String hotel;

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the hotel
     */
    public String getHotel() {
        return hotel;
    }

    /**
     * @return the confpassword
     */
    public String getConfpassword() {
        return confpassword;
    }

    /**
     * @param login
     *            the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    public enum F implements IField {

        login, password, hotel, confpassword
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
        case login:
            return getLogin();
        case password:
            return getPassword();
        case hotel:
            return getHotel();
        case confpassword:
            return getConfpassword();
        }
        return null;
    }

    @Override
    public Class getT(IField f) {
        return String.class;
    }

    @Override
    public void setF(IField f, Object o) {
        F fi = (F) f;
        switch (fi) {
        case login:
            setLogin((String) o);
            break;
        case password:
            password = (String) o;
            break;
        case hotel:
            hotel = (String) o;
            break;
        case confpassword:
            confpassword = (String) o;
            break;
        }
    }

    @Override
    public IField[] getT() {
        return F.values();
    }

}
