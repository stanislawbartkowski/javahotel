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
package org.migration.comparedb2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.migration.extractor.ObjectExtractor;

public class CUtil {

	private CUtil() {

	}

	private static Set<ObjectExtractor.OBJECT> listO = new HashSet<ObjectExtractor.OBJECT>();

	static {
		listO.add(ObjectExtractor.OBJECT.TABLE);
		listO.add(ObjectExtractor.OBJECT.GLOBALTEMP);
		listO.add(ObjectExtractor.OBJECT.VIEW);
		listO.add(ObjectExtractor.OBJECT.SEQUENCE);
		listO.add(ObjectExtractor.OBJECT.TYPE);
		listO.add(ObjectExtractor.OBJECT.PACKAGE);
		listO.add(ObjectExtractor.OBJECT.PROCEDURE);
		listO.add(ObjectExtractor.OBJECT.FUNCTION);
		listO.add(ObjectExtractor.OBJECT.TRIGGER);
		listO.add(ObjectExtractor.OBJECT.BODY);
	}

	public static String ALL = "ALL";

	public static String possibleO() {
		StringBuffer bu = new StringBuffer();
		listO.forEach(l -> {
			if (bu.length() != 0)
				bu.append(',');
			bu.append(l);
		});
		return bu.toString() + " or " + ALL;
	}

	public static Set<ObjectExtractor.OBJECT> parseList(String l) {

		if (l.equals("ALL"))
			return listO;

		Set<ObjectExtractor.OBJECT> oSet = new HashSet<ObjectExtractor.OBJECT>();
		String[] t = l.split(",");

		for (String o : t) {
			ObjectExtractor.OBJECT oType = ObjectExtractor.OBJECT.valueOf(o);
			if (!(listO.contains(oType)))
				throw new RuntimeException(o + " is not allowed. Only " + possibleO() + " are allowed");
			oSet.add(oType);
		} // for

		return oSet;
	}

	public static void printList(List<ObjectsNofFound> oList) {
		oList.forEach(li -> {
			System.out.println();
			System.out.println(li.objectType() + " " + li.list().size());
			li.list().forEach(System.out::println);
		});
	}

}
