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
package com.gwtmodel.table;

import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.mm.MM;
import com.gwtmodel.table.smessage.IGetStandardMessage;

public class InvalidateMess {

	private final boolean errorsetalready;
	private final IVField fie;
	private final boolean empty;
	private final String errmess;
	private IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();

	public InvalidateMess(final IVField fie, final boolean e, final String err, boolean errorsetalready) {
		this.fie = fie;
		this.empty = e;
		this.errmess = iMess.getMessage(err);
		this.errorsetalready = errorsetalready;
	}

	public InvalidateMess(final IVField fie, final String err) {
		this(fie, false, err, false);
	}

	public InvalidateMess(final IVField fie) {
		this(fie, true, null, false);
	}

	/**
	 * @return the fie
	 */
	public IVField getFie() {
		return fie;
	}

	/**
	 * @return the empty
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * @return the errmess
	 */
	public String getErrmess() {
		if (empty) {
			return MM.getL().EmptyFieldMessage();
		}
		return errmess;
	}

	public boolean isErrorsetalready() {
		return errorsetalready;
	}
	
}
