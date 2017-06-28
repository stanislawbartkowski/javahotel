/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
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

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.migration.comparedb2.CUtil;
import org.migration.comparedb2.CompareObjects;
import org.migration.comparedb2.ObjectsNofFound;
import org.migration.extractor.ObjectExtractor;

public class MainCompare {

	private static void e(String s) {
		System.out.println(s);
	}

	private static void drawhelp() {
		e("Parameters");
		e(" <input file> <url> <user> <password> <list of object> or ALL");
		e(" List of objects : " + CUtil.possibleO());
		System.exit(4);
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 5)
			drawhelp();
		Set<ObjectExtractor.OBJECT> oList = CUtil.parseList(args[4]);
		List<ObjectsNofFound> cList = CompareObjects.analyze(args[0], args[1], args[2], args[3], oList);
		CUtil.printList(cList);
	}

}
