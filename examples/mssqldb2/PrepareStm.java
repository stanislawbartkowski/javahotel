/*
 * Copyright 2014 stanislawbartkowski@gmail.com
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepareStm {

	/**
	 *
	 * @author sbartkowski
	 *
	 *  Simple class for debug purpose
	 */
	private static class MyDump {

		private PrintStream out;

		void init() throws FileNotFoundException {
			File f = new File("/tmp/mydump");
			out = new PrintStream(f);
		}

		void P(String s) {
			out.println(s);
			out.flush();
		}
	}

	// DB2 signature
/*	
	CREATE OR REPLACE PROCEDURE SQL_PREPARESTM(
			  OUT PREQUERY VARCHAR(100),
			  OUT PREVALS PARVALS,
			  IN INS VARCHAR(100),
			  IN ARGNAMES PARVALS,
			  IN ARGVALS PARVALS 
			)
			FENCED
			NO EXTERNAL ACTION
			PROGRAM TYPE SUB
			EXTERNAL NAME 'PREPARE:PrepareStm.PrepareS' 
			PARAMETER STYLE java 
			LANGUAGE java
			;
 */
	public static void PrepareS(String[] outS, Array[] outA, String stmtS,
			Array argNames, Array argVals) throws FileNotFoundException,
			SQLException {

		// debug dump
		MyDump p = new MyDump();
		p.init();
		p.P("PrepareS start");

		// prepare map names->value for fast retrieval
		String[] parnames = (String[]) argNames.getArray();
		String[] parvals = (String[]) argVals.getArray();
		Map<String, String> args = new HashMap<String, String>();
		for (int i = 0; i < parnames.length; i++)
			args.put(parnames[i], parvals[i]);

		// StringBuffer - more effective here then String
		StringBuffer buf = new StringBuffer(stmtS);
		List<String> outv = new ArrayList<String>();
		// Replace all @name with ? and prepare value list
		while (true) {
			// start of next parameter marker
			int pos = buf.indexOf("@");
			if (pos == -1)
				break;
			// find the end
			int endpos = pos+1;
			// marker name from @ to the end of query or non digit character
			for (; endpos < buf.length(); endpos++)
				if (!Character.isLetterOrDigit(buf.charAt(endpos))) break;
			p.P(buf.toString() + ": " + pos + " e=" + endpos);
			String markername = buf.substring(pos, endpos);
			// marker name identified
			buf.delete(pos, endpos).insert(pos, '?');
			// marker name removed from query
			// look up for marker name associated value
			if (!args.containsKey(markername))
				throw new SQLException(markername
						+ ": cannot be found in input parameter list");
			outv.add(args.get(markername));
		} // while

		// take connection only to create an instance of Array data structure
		Connection con = DriverManager.getConnection("jdbc:default:connection");
		String[] vals = outv.toArray(new String[outv.size()]);

		// finalize, make assign output value
		outA[0] = con.createArrayOf("VARCHAR", vals);
		outS[0] = buf.toString();
		p.P("Prepare stop");
	}

}
