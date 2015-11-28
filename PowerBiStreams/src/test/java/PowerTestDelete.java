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
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.com.powerbi.streams.PowerBI;

public class PowerTestDelete extends AbstractPowerTest {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");

		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");

		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");

		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");

		String token = PowerBI.getAuthToken(OAUTH_USERNAME, OAUTH_PASSWORD, OAUTH_CLIENT_ID);
		System.out.println(token);
		if (token == null)
			return;
		String id = PowerBI.getDataSetId(token, testDataSet);
		if (id == null)
			return;
		System.out.println(id);
		PowerBI.clearTable(token, id, testTable);

	}

}
