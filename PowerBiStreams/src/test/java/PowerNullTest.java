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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.com.powerbi.streams.PowerBI;

public class PowerNullTest extends AbstractPowerTest {

	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		String token = PowerBI.getAuthToken(OAUTH_USERNAME, OAUTH_PASSWORD, OAUTH_CLIENT_ID);
		System.out.println(token);
		if (token == null)
			return;
		Map<String, String> schema = new HashMap<String, String>();
		schema.put("id", PowerBI.INT64_TYPE);
		schema.put("name", PowerBI.STRING_TYPE);
		schema.put("d", PowerBI.DOUBLE_TYPE);
		String id = PowerBI.checkTableDataSet(token, testDataSet1, testTable1, schema);
		System.out.println(id);
		List<Map<String, PowerBI.TableValue>> rlist = new ArrayList<Map<String, PowerBI.TableValue>>();
		Map<String, PowerBI.TableValue> row = new HashMap<String, PowerBI.TableValue>();
		row.put("id", new PowerBI.TableValue(10));
		row.put("name", new PowerBI.TableValue("Next null"));
		row.put("d", new PowerBI.TableValue());
		rlist.add(row);
		row = new HashMap<String, PowerBI.TableValue>();
		row.put("id", new PowerBI.TableValue(15));
		row.put("name", new PowerBI.TableValue());
		row.put("d", new PowerBI.TableValue(234.56));
		rlist.add(row);
		row = new HashMap<String, PowerBI.TableValue>();
		row.put("id", new PowerBI.TableValue());
		row.put("name", new PowerBI.TableValue("Wow"));
		row.put("d", new PowerBI.TableValue(999.23));
		rlist.add(row);
		PowerBI.addTableRows(token, id, testTable1, rlist);
	}
}
