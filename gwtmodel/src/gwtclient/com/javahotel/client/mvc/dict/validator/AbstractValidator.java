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
package com.javahotel.client.mvc.dict.validator;

import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.validator.IErrorMessageContext;
import com.javahotel.client.mvc.validator.IRecordValidator;

abstract class AbstractValidator implements IRecordValidator {
	
	protected final IResLocator rI;
	protected IErrorMessageContext iCo;
	
	AbstractValidator(final IResLocator rI) {
		this.rI = rI;
		iCo = null;
	}
	
	public boolean isEmpty(RecordModel a) {
		return false;
	}

	public void setErrContext(IErrorMessageContext co) {
		iCo = co;
	}


}
