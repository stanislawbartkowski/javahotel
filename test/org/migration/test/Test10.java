package org.migration.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.migration.comparedb2.CUtil;
import org.migration.comparedb2.CompareObjects;
import org.migration.comparedb2.ObjectsNofFound;
import org.migration.extractor.ObjectExtractor;

public class Test10 extends TestHelper {

	private final static String url = "jdbc:db2://db211:50000/eod:retrieveMessagesFromServerOnGetMessage=true";
	private final static String user = "db2inst1";
	private final static String password = "db2inst1";
	
	private final String sName = "oracle.ddl";

	@Test
	public void test1() throws Exception {
		Set<ObjectExtractor.OBJECT> lType = new HashSet<ObjectExtractor.OBJECT>();
//		lType.add(ObjectExtractor.OBJECT.GLOBALTEMP);
//		lType.add(ObjectExtractor.OBJECT.SEQUENCE);
//		lType.add(ObjectExtractor.OBJECT.TYPE);
//		lType.add(ObjectExtractor.OBJECT.FUNCTION);
//		lType.add(ObjectExtractor.OBJECT.TRIGGER);
//		lType.add(ObjectExtractor.OBJECT.BODY);
		lType.add(ObjectExtractor.OBJECT.PACKAGE);
//		List<ObjectsNofFound> oList = CompareObjects.analyze(getFileName("rsample1.ora"), url, user, password, lType);
		List<ObjectsNofFound> oList = CompareObjects.analyze(sName, url, user, password, lType);
		CUtil.printList(oList);
	}

}
