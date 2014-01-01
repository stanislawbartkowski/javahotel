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
package com.gwtmodel.table.mail;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.mailcommon.CMailToSend;
import java.util.Map;

/**
 * 
 * @author perseus
 */
@SuppressWarnings("serial")
public class MailToSend extends CMailToSend implements ICustomObject {

	public MailToSend(String boxName, Map<String, String> box, String header,
			String content, String to, String from, boolean text) {
		setBox(box);
		setBoxName(boxName);
		setHeader(header);
		setContent(content);
		setTo(to);
		setFrom(from);
		setText(text);
	}

	public CMailToSend construct() {
		CMailToSend cma = new CMailToSend();
		cma.setBox(getBox());
		cma.setBoxName(getBoxName());
		cma.setContent(getContent());
		cma.setFrom(getFrom());
		cma.setHeader(getHeader());
		cma.setText(isText());
		cma.setTo(getTo());
		return cma;
	}
}
