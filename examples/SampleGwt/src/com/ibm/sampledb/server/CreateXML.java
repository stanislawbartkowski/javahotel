/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sampledb.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class CreateXML {

    private final String rootTag;
    private final String elemTag;
    private BufferedWriter out;
    private String fileName;

    CreateXML(String rootTag, String elemTag) {
        this.rootTag = rootTag;
        this.elemTag = elemTag;
    }

    private void drawTag(String na, boolean beg, boolean addNl)
            throws IOException {
        if (beg) {
            out.write('<');
        } else {
            out.write("</");
        }
        out.write(na + ">");
        if (addNl) {
            out.write('\n');
        }
    }

    String getFileName() {
        return fileName;
    }

    void startXML() throws IOException {
        File temp = File.createTempFile("print", null);
        temp.deleteOnExit();
        fileName = temp.getAbsolutePath();
        out = new BufferedWriter(new FileWriter(temp));
        drawTag(rootTag, true, true);
    }

    void startElem() throws IOException {
        drawTag(elemTag, true, false);
    }

    void drawElem(String tag, String val) throws IOException {
        drawTag(tag, true, false);
        out.write(val);
        drawTag(tag, false, false);
    }

    void endElem() throws IOException {
        drawTag(elemTag, false, true);
    }

    void endXML() throws IOException {
        drawTag(rootTag, false, true);
        out.close();
    }

}
