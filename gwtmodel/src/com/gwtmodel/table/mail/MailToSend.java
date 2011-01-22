/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.mail;

import com.gwtmodel.table.ICustomObject;
import java.util.Map;

/**
 *
 * @author perseus
 */
public class MailToSend implements ICustomObject {

    private Map<String, String> box;
    private final String boxName;
    private final String header;
    private final String content;
    private final String to;
    private final String from;
    private final boolean text;

    public MailToSend(String boxName, Map<String, String> box, String header,
            String content, String to, String from, boolean text) {
        this.boxName = boxName;
        this.box = box;
        this.header = header;
        this.content = content;
        this.to = to;
        this.from = from;
        this.text = text;
    }

    /**
     * @return the box
     */
    public Map<String, String> getBox() {
        return box;
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return the text
     */
    public boolean isText() {
        return text;
    }

    /**
     * @return the boxName
     */
    public String getBoxName() {
        return boxName;
    }

    /**
     * @param box the box to set
     */
    public void setBox(Map<String, String> box) {
        this.box = box;
    }
}
