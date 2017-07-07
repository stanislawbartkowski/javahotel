package org.migration.test;

import org.junit.Test;
import org.migration.tasks.ProcList;

public class Test12 extends TestHelper {
	
	
	private static String IFILE="/home/sbartkowski/Dokumenty/db2/xxxxxxxxxxxxxxxxxxx"; 

	@Test
	public void test1() throws Exception {
		
		ProcList.listofSchemas(IFILE);
		
	}

}
