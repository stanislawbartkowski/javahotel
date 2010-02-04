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
package com.javahotel.common.toobject;

import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class PhoneNumberP extends AbstractToILd {

	private LId id;
	private String phoneNumber;

	public enum F implements IField {

		id, phoneNumber
	};

	@Override
	public Class<?> getT(final IField f) {
		Class<?> cla = String.class;
		F fi = (F) f;
		switch (fi) {
		case id:
			cla = Long.class;
			break;
		}
		return cla;
	}

	@Override
	public IField[] getT() {
		return F.values();
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LId getId() {
		return id;
	}

	public void setId(LId id) {
		this.id = id;
	}

	@Override
	public Object getF(IField f) {
		F fi = (F) f;
		switch (fi) {
		case id:
			return id;
		case phoneNumber:
			return getPhoneNumber();
		}
		return null;
	}

	@Override
	public void setF(IField f, Object o) {
		F fi = (F) f;
		switch (fi) {
		case id:
			this.id = (LId) o;
			break;
		case phoneNumber:
			setPhoneNumber((String) o);
		}
	}
}
