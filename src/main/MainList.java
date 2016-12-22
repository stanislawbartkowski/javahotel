/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package main;

import org.migration.tasks.ProcList;

public class MainList {

	private static void e(String s) {
		System.out.println(s);
	}

	private final static String LISTOF = "--listof";

	private static void drawhelp() {
		e("Parameters: ");
		e("MainList <input file name> /--listof/");
		e("    input file containing Oracle code");
		e("    " + LISTOF + " (optional), list of objects");
		System.exit(4);
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 1 && args.length != 2)
			drawhelp();
		if (args.length == 2)
			if (!LISTOF.equals(args[1]))
				drawhelp();
		ProcList.stat(args[0], args.length == 2);

	}

}
