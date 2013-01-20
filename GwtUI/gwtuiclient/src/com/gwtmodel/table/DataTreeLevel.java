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
package com.gwtmodel.table;

/**
 * @author hotel
 * 
 */
public class DataTreeLevel {

    private DataTreeLevel() {

    }

    public static boolean isLeaf(int level) {
        return level < IDataListType.LEAFBOUND;
    }

    public static int getLevel(int level) {
        if (!isLeaf(level)) {
            return level - IDataListType.LEAFBOUND;
        }
        return level;
    }

    public static int toLeaf(int treelevel) {
        assert treelevel < IDataListType.LEAFBOUND;
        return treelevel;
    }

    public static int toNode(int treelevel) {
        return treelevel + IDataListType.LEAFBOUND;
    }

}
