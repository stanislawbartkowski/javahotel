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
package com.gwtmodel.table.mailcommon;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author perseus
 */
public class CMailToSend implements Serializable {

    private Map<String, String> box;
    private String boxName;
    private String header;
    private String content;
    private String to;
    private String from;
    private boolean text;

    public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setText(boolean text) {
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
