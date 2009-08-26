/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import java.util.Collection;

import com.javahotel.client.CommonUtil;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.common.toobject.PersonP;

class PersonValidator extends AbstractValidator {

	private final DictData da;

	PersonValidator(IResLocator rI, DictData da) {
		super(rI);
		this.da = da;
	}

	public void validateS(final int action, final RecordModel a,
			final ISignalValidate sig) {
		Collection<InvalidateMess> errMess = ValidUtil.validateEmpty(da,
				action, a);
		if (errMess != null) {
			ValidUtil.callSig(errMess, sig, iCo);
			return;
		}
		LoginRecord pe = (LoginRecord) a.getA();
		String p1 = pe.getPassword();
		String p2 = pe.getConfpassword();
		if (!p1.equals(p2)) {
			errMess = ValidUtil.createErr(LoginRecord.F.password, rI
					.getLabels().PasswordDifferent());
			if (errMess != null) {
				ValidUtil.callSig(errMess, sig, iCo);
				return;
			}

		}
		PersonP p = new PersonP();
		p.setName(pe.getLogin());
		GWTGetService.getService().testPersistPerson(
				CommonUtil.getPType(action),
				p,
				new ValidUtil.ValidCallBack(rI, sig, iCo,
						pe));
	}
}
