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
package com.jythonui.server.mail;

import java.util.ArrayList;
import java.util.List;

import com.jythonui.server.ISharedConsts;
import com.jythonui.shared.PropDescription;

public class Note extends PropDescription {

	private static final long serialVersionUID = 1L;

	private boolean text;

	private List<NoteAttach> aList = new ArrayList<NoteAttach>();
	private List<String> recipientsList = new ArrayList<String>();

	public Note() {
		setAttr(ISharedConsts.PATTPROP, ISharedConsts.MAILNOTEPATT);
	}

	public String getFrom() {
		return getAttr(ISharedConsts.MAILFROM);
	}

	public void setFrom(String from) {
		setAttr(ISharedConsts.MAILFROM, from);
	}

	public String getContent() {
		return getAttr(ISharedConsts.MAILCONTENT);
	}

	public void setSendResult(String res) {
		setAttr(ISharedConsts.MAILSENTRESULT, res);
	}

	public String getSendResult() {
		return getAttr(ISharedConsts.MAILSENTRESULT);
	}

	public void setContent(String content) {
		setAttr(ISharedConsts.MAILCONTENT, content);
	}

	public List<NoteAttach> getaList() {
		return aList;
	}

	public boolean isText() {
		return text;
	}

	public void setText(boolean text) {
		this.text = text;
	}

	public List<String> getRecipientsList() {
		return recipientsList;
	}

	public void setRecipient(String recipient) {
		recipientsList.clear();
		recipientsList.add(recipient);
	}

	public Object getRecipient() {
		if (recipientsList.isEmpty())
			return null;
		return recipientsList.get(0);
	}

}
