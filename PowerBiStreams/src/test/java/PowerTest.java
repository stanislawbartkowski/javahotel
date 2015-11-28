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
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.com.powerbi.streams.PowerBI;
import org.com.powerbi.streams.PowerBI.DataSetElem;

public class PowerTest extends AbstractPowerTest {

	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		String token = PowerBI.getAuthToken(OAUTH_USERNAME, OAUTH_PASSWORD, OAUTH_CLIENT_ID);
		System.out.println(token);
		if (token == null)
			return;
		List<DataSetElem> li = PowerBI.getDataSets(token);
		for (DataSetElem e : li)
			System.out.println(e.getId() + " " + e.getName());
		String id = PowerBI.getDataSetId(token, testDataSet);
		System.out.println(id);
		if (id != null) {
			List<String> ls = PowerBI.getDataSetTables(token, id);
			for (String s : ls)
				System.out.println(s);
		} else {

			Map<String, String> schema = new HashMap<String, String>();
			schema.put("id", PowerBI.INT64_TYPE);
			schema.put("name", PowerBI.STRING_TYPE);
			PowerBI.createDataSet(token, testDataSet, testTable, schema);
			id = PowerBI.getDataSetId(token, testDataSet);
			System.out.println(id);
		}
		Random rand = new Random();
		int no = 0;
		while (true) {
			no++;
			System.out.println(no);
			List<Map<String, PowerBI.TableValue>> rlist = new ArrayList<Map<String, PowerBI.TableValue>>();
			for (int i = 0; i < 20; i++) {
				Map<String, PowerBI.TableValue> row = new HashMap<String, PowerBI.TableValue>();
				int ra = rand.nextInt(50);
				row.put("id", new PowerBI.TableValue(ra));
				row.put("name", new PowerBI.TableValue("Hello " + i));
				rlist.add(row);
			}
			PowerBI.addTableRows(token, id, testTable, rlist);
			System.out.println("wait ...");
			Thread.sleep(1000);
		}
	}

}
