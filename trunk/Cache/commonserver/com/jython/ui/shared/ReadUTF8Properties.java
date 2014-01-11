/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

package com.jython.ui.shared;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class ReadUTF8Properties {

    private static String getFileContent(String name) throws IOException {
        // does not work in Google App Engine, use Guava goodies
        // return new String(Files.readAllBytes(Paths.get(name)));
        return Files.toString(new File(name), Charsets.UTF_8);
    }

    private static String toLatin1(String s) {
        StringBuilder b = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (c >= 256 && c < 1000)
                // 3 digits, add one leading 0
                b.append("\\u0").append(Integer.toHexString(c));
            else if (c >= 1000)
                // 4 digits
                b.append("\\u").append(Integer.toHexString(c));
            else
                b.append(c);
        }
        return b.toString();
    }

    public static Properties readProperties(String propName) throws IOException {

        Properties prop = new Properties();
        String p = toLatin1(getFileContent(propName));
        prop.load(new StringReader(p));
        return prop;
    }

}
