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
package com.javahotel.security.login;

import com.javahotel.remoteinterfaces.HotelT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class PersonHotelRules {

    private static final char C = '*';

    private PersonHotelRules() {
    }

    public static class HR {

        private final String hotel;
        private final String role;

        public HR(final String p) {
            String[] r = p.split("\\" + C);
            if (r == null) {
                hotel = null;
                role = null;
                return;
            }
            if (r.length != 2) {
                hotel = null;
                role = null;
                return;
            }
            hotel = r[0];
            role = r[1];
        }

        boolean eqHotel(final String h) {
            if (getHotel() == null) {
                return false;
            }
            return getHotel().equals(h);
        }

        public String getHotel() {
            return hotel;
        }

        public String getRole() {
            return role;
        }

        public boolean isNull() {
            return hotel == null;
        }
    }

    public static String onHotel(final String ro, final HotelT ho) {
        HR h = new HR(ro);
        if (h.eqHotel(ho.getName())) {
            return h.getRole();
        }
        return null;
    }

    public static String createRol(final HotelT ho, final String role) {
        return ho.getName() + C + role;
    }
}
