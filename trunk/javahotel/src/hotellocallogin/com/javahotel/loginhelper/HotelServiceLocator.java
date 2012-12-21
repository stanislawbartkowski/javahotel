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
package com.javahotel.loginhelper;

import com.javahotel.db.authentication.impl.AuthenticationImpl;
import com.javahotel.db.hotelbase.impl.HotelData;
import com.javahotel.db.hotelbase.impl.HotelOp;
import com.javahotel.db.hotelbase.impl.HotelTest;
import com.javahotel.db.report.impl.ListCommand;
import com.javahotel.db.security.impl.SecurityImpl;
import com.javahotel.db.start.IStart;
import com.javahotel.db.start.StartBean;
import com.javahotel.remoteinterfaces.HotelServerType;
import com.javahotel.remoteinterfaces.IAuthentication;
import com.javahotel.remoteinterfaces.IHotelData;
import com.javahotel.remoteinterfaces.IHotelOp;
import com.javahotel.remoteinterfaces.IHotelTest;
import com.javahotel.remoteinterfaces.IList;
import com.javahotel.remoteinterfaces.ISecurity;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class HotelServiceLocator {

	public HotelServiceLocator(final HotelServerType t,
			final String serverHost, final String port) {
	}

	public HotelServiceLocator(final HotelServerType t) {
	}

	public ISecurity getSecurityBean() {
		SecurityImpl i = new SecurityImpl();
		IStart iS = new StartBean();
		i.setIStart(iS);
		i.init();
		return i;
	}

	public IAuthentication getAuthenticationBean() {
		return new AuthenticationImpl();
	}

	public IList getListBean() {
		return new ListCommand();
	}

	public IHotelTest getHotelTestBean() {
		return new HotelTest();
	}

	public IHotelData getHotelBean() {
		return new HotelData();
	}

	public IHotelOp getHotelOpBean() {
		return new HotelOp();
	}
}
