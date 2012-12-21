/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
import java.util.Map;
import javax.ejb.Remote;

@Remote
public interface ISecurity {

	public void logoutSession(SessionT sessionId);

	public SessionT loginSession(String sessionId, String name,
			PasswordT password);

	public SessionT loginadminSession(String sessionId, String name,
			PasswordT password);

	boolean isValidSession(SessionT sessionId);

	boolean isAdminSession(SessionT sessionId);

	void setDatabaseDefinition(SessionT sessionT, Map<String, String> prop);

	void setNewProperties(SessionT sessionT, Map<String, String> prop);

	List<HotelT> getListHotels(SessionT sessionT);

	List<String> getListRoles(SessionT sessionT, HotelT ho);
}
