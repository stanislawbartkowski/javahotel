/*
 * Copyright 2015 stanislawbartkowski@gmail.com
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
package com.ibm.mystreams.server;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.mystreams.client.GreetingService;
import com.ibm.mystreams.server.restapi.RestApi;
import com.ibm.mystreams.shared.ConnectionData;
import com.ibm.mystreams.shared.RestRes;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	private final static Logger l = Logger.getLogger(GreetingServiceImpl.class
			.getName());

	@Override
	public RestRes callRest(ConnectionData data, String url) {
		RestRes res = new RestRes();
		try {
			String mess = RestApi.readString(data, url);
			res.setOk(true);
			res.setMess(mess);
		} catch (KeyManagementException | NoSuchAlgorithmException
				| IOException e) {
			l.log(Level.SEVERE, "Error while retrieving RESR response", e);
			e.printStackTrace();
			res.setOk(false);
			res.setMess(e.getLocalizedMessage());
		}
		return res;
	}
}
