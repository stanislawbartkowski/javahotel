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
package com.javahotel.client.mvc.record.view.helper;

import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.model.RecordField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class RecordChangeMode {

	private RecordChangeMode() {
	}

	public static void changeMode(IRecordDef model, int actionMode) {

		for (RecordField re : model.getFields()) {
			boolean enable = true;
			switch (actionMode) {
			case IPersistAction.ENABLEDIALOGACTION:
				break;
			case IPersistAction.DISABLEDIALOGACTION:
				enable = false;
				break;
			case IPersistAction.ADDACION:
				break;
			case IPersistAction.DELACTION:
				enable = false;
				break;
			case IPersistAction.MODIFACTION:
				if (!re.isCanChange()) {
					enable = false;
				}
				break;
			}
			if (re.isAlwaysReadOnly()) {
				enable = false;
			}
			re.getELine().setReadOnly(!enable);
		}

	}
}
