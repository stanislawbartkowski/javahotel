/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

public class EntityConstr {

	private final String komname;
	private final String symname;
	private final String viewname;

	EntityConstr(String p1, String p2,String p3) {
		this.komname = p1;
		this.symname = p2;
		this.viewname = p3;
	}

	public String getViewname() {
		return viewname;
	}

	public String getKomname() {
		return komname;
	}

	public String getSymname() {
		return symname;
	}

}
