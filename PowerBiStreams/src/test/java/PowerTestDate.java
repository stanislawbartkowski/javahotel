
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.com.powerbi.streams.PowerBI;

public class PowerTestDate extends AbstractPowerTest {

	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		String token = PowerBI.getAuthToken(OAUTH_USERNAME, OAUTH_PASSWORD, OAUTH_CLIENT_ID);
		System.out.println(token);
		if (token == null)
			return;
		Map<String, String> schema = new HashMap<String, String>();
		schema.put("name", PowerBI.STRING_TYPE);
		schema.put("date", PowerBI.DATETIME_TYPE);
		schema.put("d", PowerBI.DOUBLE_TYPE);
		String id = PowerBI.checkTableDataSet(token, testDataSet2, testTable2, schema);
		System.out.println(id);
		int i = 0;
		long l = new Date().getTime();
		Random rand = new Random();
		while (true) {
			System.out.println(i++);
			List<Map<String, PowerBI.TableValue>> rlist = new ArrayList<Map<String, PowerBI.TableValue>>();
			Map<String, PowerBI.TableValue> row = new HashMap<String, PowerBI.TableValue>();
			row.put("date", new PowerBI.TableValue(new Date(l), null));
			l += 1000;
			row.put("name", new PowerBI.TableValue("Point " + i));
			row.put("d", new PowerBI.TableValue(rand.nextDouble() * 100));
			rlist.add(row);
			PowerBI.addTableRows(token, id, testTable2, rlist);
			Thread.sleep(1000);
		}
	}

}
