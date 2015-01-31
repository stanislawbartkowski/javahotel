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
package com.gwtmodel.table;

/**
 * 
 * 
 * @author perseus
 */
public class MapEntryFactory {

    private MapEntryFactory() {
    }

    public static IMapEntry createEntry(String key, String val) {
        return new ListEntry(key, val);
    }

    private static class ListEntry implements IMapEntry {

        private final String key;
        private final String val;

        private ListEntry(String key, String val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return val;
        }
    }
}
