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
package com.javahotel.client.dispatcher;

import com.google.gwt.user.client.Command;
import com.gwtmodel.table.ICommand;
import com.javahotel.client.IResLocator;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public abstract class UICommand implements Command {

	protected final IResLocator rI;
	private final EnumDialog eDialog;

	protected UICommand(final IResLocator i, final EnumDialog e) {
		this.rI = i;
		this.eDialog = e;
	}

	private class ClickNext implements ICommand {

		private final EnumAction a;

		ClickNext(final EnumAction a) {
			this.a = a;
		}

		public void execute() {
			rI.getDi().dispatch(eDialog, a);
		}
	}

	public ICommand createCLick(final EnumAction a) {
		return new ClickNext(a);
	}
}
