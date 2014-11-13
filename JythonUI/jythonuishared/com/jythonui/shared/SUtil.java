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
package com.jythonui.shared;

public class SUtil {

    private SUtil() {

    }

    private static String getDirectory(String fileName) {
        String[] s = fileName.split("/");
        String dir = "";
        for (int i = 0; i < s.length - 1; i++)
            dir = dir + s[i] + "/";
        return dir;
    }

    public static String getFileName(String parentDialogName, String pdialogName) {
        String dialogName = pdialogName;
        if (pdialogName.charAt(0) == ICommonConsts.RELCHAR) {
            if (parentDialogName == null)
                return null;
            String dirName = getDirectory(parentDialogName);
            dialogName = dirName + pdialogName.substring(1);
        }
        return dialogName;
    }

}
