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
package com.javahotel.remoteinterfaces;

import java.util.List;

import javax.ejb.Remote;

import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;

@Remote
public interface IAuthentication {

	public void persistHotel(SessionT sessionId, HotelT hotel,
			String description, String database);

	public ReturnPersist testPersistHotel(SessionT sessionId, PersistType t,
			HotelP hotel);

	public ReturnPersist testPersistPerson(SessionT sessionId, PersistType t,
			PersonP person);

	public void removeHotel(SessionT sessionId, HotelT hotel);

	public List<HotelP> getHotelList(SessionT sessionId);

	public void clearAuthBase(SessionT sessionId);

	public void persistPersonHotel(SessionT sessionId, String person,
			HotelT hotel, List<String> roles);

	public List<String> getPersonHotelRoles(SessionT sessionId,
			String person, HotelT hotel);

	public void persistPerson(SessionT sessionId, String person,
			PasswordT password);

	public void removePerson(SessionT sessionId, String person);

	public List<PersonP> getPersonList(SessionT sessionId);
}
