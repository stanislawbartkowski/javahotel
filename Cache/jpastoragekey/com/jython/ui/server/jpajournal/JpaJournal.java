/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jython.ui.server.jpajournal;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.jython.jpautil.crudimpl.AbstractJpaCrud;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.jpastoragekey.entity.EJournalEntry;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jythonui.server.journal.IJournal;
import com.jythonui.server.journal.JournalRecord;

public class JpaJournal extends AbstractJpaCrud<JournalRecord, EJournalEntry> implements IJournal {

	@Inject
	public JpaJournal(ITransactionContextFactory eFactory, IJpaObjectGenSymFactory iGenFactory) {
		super(new String[] { "findAllJournal", "findOneJournal" }, iGenFactory, EJournalEntry.class.getSimpleName(),
				eFactory, EJournalEntry.class);
	}

	@Override
	protected JournalRecord toT(EJournalEntry sou, EntityManager em, OObjectId hotel) {
		JournalRecord j = new JournalRecord();
		j.setJournalType(sou.getType());
		j.setJournalTypeSpec(sou.getTypespec());
		j.setJournalElem1(sou.getElem1());
		j.setJournalElem2(sou.getElem2());
		return j;
	}

	@Override
	protected EJournalEntry constructE(EntityManager em, OObjectId hotel) {
		return new EJournalEntry();
	}

	@Override
	protected void toE(EJournalEntry dest, JournalRecord sou, EntityManager em, OObjectId hotel) {
		dest.setType(sou.getJournalType());
		dest.setTypespec(sou.getJournalTypeSpec());
		dest.setElem1(sou.getJournalElem1());
		dest.setElem2(sou.getJournalElem2());
	}

	@Override
	protected void afterAddChange(EntityManager em, OObjectId hotel, JournalRecord prop, EJournalEntry elem,
			boolean add) {
	}

	@Override
	protected void beforedeleteElem(EntityManager em, OObjectId hotel, EJournalEntry elem) {
	}

}
