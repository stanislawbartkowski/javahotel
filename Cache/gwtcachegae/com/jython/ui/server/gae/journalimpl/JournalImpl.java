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
package com.jython.ui.server.gae.journalimpl;

import javax.inject.Inject;

import com.googlecode.objectify.ObjectifyService;
import com.jython.ui.server.gae.crudimpl.CrudGaeAbstract;
import com.jython.ui.server.gae.security.entities.EJournal;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.journal.IJournal;
import com.jythonui.server.journal.JournalRecord;

public class JournalImpl extends CrudGaeAbstract<JournalRecord, EJournal> implements IJournal {

	static {
		ObjectifyService.register(EJournal.class);
	}

	@Inject
	public JournalImpl(ICrudObjectGenSym iGen) {
		super(EJournal.class, EJournal.class.getSimpleName(), iGen);
	}

	@Override
	protected JournalRecord constructProp(EObject ho, EJournal sou) {
		JournalRecord j = new JournalRecord();
		j.setJournalType(sou.getType());
		j.setJournalTypeSpec(sou.getTypespec());
		j.setJournalElem1(sou.getElem1());
		j.setJournalElem2(sou.getElem2());
		return j;
	}

	@Override
	protected EJournal constructE() {
		return new EJournal();
	}

	@Override
	protected void toE(EObject ho, EJournal dest, JournalRecord sou) {
		dest.setType(sou.getJournalType());
		dest.setTypespec(sou.getJournalTypeSpec());
		dest.setElem1(sou.getJournalElem1());
		dest.setElem2(sou.getJournalElem2());
	}

	@Override
	protected IDeleteElem beforeDelete(EObject ho, EJournal elem) {
		return null;
	}

}
