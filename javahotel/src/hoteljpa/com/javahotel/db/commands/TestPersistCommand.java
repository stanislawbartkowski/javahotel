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
package com.javahotel.db.commands;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

public class TestPersistCommand extends CommandAbstract {

	private final PersistType t;
	private final DictionaryP a;
	private final ReturnPersist ret;
	private final DictType da;

	public TestPersistCommand(final SessionT se, PersistType t, DictType da,
			DictionaryP a) {
		super(se, false, new HotelT(a.getHotel()));
		this.t = t;
		this.a = a;
		this.da = da;
		ret = new ReturnPersist();
	}

	@Override
	protected void command() {
		TestPersistObject.testPersist(iC, ret, t, a, da);
	}

	public ReturnPersist getRet() {
		return ret;
	}

}
