/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jython.ui.server.jpastoragekey.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jython.serversecurity.jpa.entities.EObjectDict;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel", "name" }) )
@NamedQueries({ @NamedQuery(name = "findAllJournal", query = "SELECT x FROM EJournalEntry x WHERE x.hotel = ?1 ORDER BY x.id DESC"),
		@NamedQuery(name = "findOneJournal", query = "SELECT x FROM EJournalEntry x WHERE x.hotel = ?1 AND x.name = ?2"), 
        @NamedQuery(name = "removeAllJournals", query = "DELETE FROM EJournalEntry x WHERE x.hotel = ?1") 
})
public class EJournalEntry extends EObjectDict {

	@Column(nullable = false)
	private String type;
	private String typespec;
	private String elem1;
	private String elem2;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypespec() {
		return typespec;
	}

	public void setTypespec(String typespec) {
		this.typespec = typespec;
	}

	public String getElem1() {
		return elem1;
	}

	public void setElem1(String elem1) {
		this.elem1 = elem1;
	}

	public String getElem2() {
		return elem2;
	}

	public void setElem2(String elem2) {
		this.elem2 = elem2;
	}

}
