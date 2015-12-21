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
package com.jythonui.server.journal;

import com.jythonui.server.ISharedConsts;
import com.jythonui.shared.PropDescription;

public class JournalRecord extends PropDescription {

	private static final long serialVersionUID = 1L;

	public JournalRecord() {
		setAttr(ISharedConsts.PATTPROP, ISharedConsts.JOURNALPATT);

	}

	public String getJournalType() {
		return getAttr(ISharedConsts.JOURNALTYPE);
	}

	public void setJournalType(String type) {
		setAttr(ISharedConsts.JOURNALTYPE, type);
	}

	public String getJournalTypeSpec() {
		return getAttr(ISharedConsts.JOURNALTYPESPEC);
	}

	public void setJournalTypeSpec(String typespec) {
		setAttr(ISharedConsts.JOURNALTYPESPEC, typespec);
	}

	public String getJournalElem1() {
		return getAttr(ISharedConsts.JOURNALELEM1);
	}

	public void setJournalElem1(String elem) {
		setAttr(ISharedConsts.JOURNALELEM1, elem);
	}

	public String getJournalElem2() {
		return getAttr(ISharedConsts.JOURNALELEM2);
	}

	public void setJournalElem2(String elem) {
		setAttr(ISharedConsts.JOURNALELEM2, elem);
	}

}
