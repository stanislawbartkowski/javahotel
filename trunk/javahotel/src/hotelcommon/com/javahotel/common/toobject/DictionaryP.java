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
package com.javahotel.common.toobject;

import com.javahotel.types.IDictionary;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DictionaryP extends AbstractTo implements IDictionary {
	
	private LId id;
	private String name;
	private String description;
	private String hotel;

	@Override
	public Class<?> getT(final IField f) {
		Class<?> cla = String.class;
		if (! (f instanceof F)) {
		    return null;
		}
		F fi = (F) f;
//		try {
//			fi = (F) f;
//		} catch (java.lang.ClassCastException e) {
//			return null;
//		} catch (Exception e) {
//		    return null;
///		}
		switch (fi) {
		case id:
			cla = LId.class;
			break;
		}
		return cla;
	}

	public enum F implements IField {

		id, name, description, hotel
	}

	@Override
	public IField[] getT() {
		return F.values();
	}

	public LId getId() {
		return id;
	}

	public void setId(LId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getHotel() {
		return hotel;
	}

	public void setHotel(final String hotel) {
		this.hotel = hotel;
	}

	@Override
	public Object getF(IField f) {
		F fi = (F) f;

		switch (fi) {
		case name:
			return getName();
		case id:
			return getId();
		case description:
			return getDescription();
		case hotel:
			return getHotel();
		}
		return null;
	}

	@Override
	public void setF(IField f, Object o) {
		F fi = (F) f;

		switch (fi) {
		case name:
			setName((String) o);
			break;
		case id:
			setId((LId) o);
			break;
		case description:
			setDescription((String) o);
			break;
		case hotel:
			setHotel((String) o);
			break;
		}
	}
}
