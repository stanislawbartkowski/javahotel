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
package com.javahotel.dbres.entityconstr;

import java.util.HashMap;
import java.util.Map;

class EntityConstrData implements IEntityConstr {

	private EntityConstr defa;

	private Map<String, EntityConstr> ma;

	EntityConstrData() {
		this.defa = null;
		ma = new HashMap<String, EntityConstr>();
	}

	void setDefa(EntityConstr defa) {
		this.defa = defa;
	}

	void addConstr(String classname, EntityConstr d) {
		ma.put(classname, d);
	}

	public EntityConstr getEntityC(String classname) {
		EntityConstr d = ma.get(classname);
		if (d == null) {
			return null;
		}
		String komname = d.getKomname();
		String symname = d.getSymname();
		String viewname = d.getViewname();
		if ((komname == null) && (defa != null)) {
			komname = defa.getKomname();
		}
		if ((symname == null) && (defa != null)) {
			symname = defa.getSymname();
		}
		if ((viewname == null) && (defa != null)) {
			viewname = defa.getViewname();
		}
		return new EntityConstr(komname, symname, viewname);
	}

}
