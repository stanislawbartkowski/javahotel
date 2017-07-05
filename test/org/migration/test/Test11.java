package org.migration.test;

import org.junit.Test;
import org.migration.tasks.ProcList;

public class Test11 extends TestHelper {
	
	private static String IFILE="/home/sbartkowski/Dokumenty/db2/xxxx/company/DBanalysisIVAP01D.package.txt"; 
	
	@Test
	public void test1() throws Exception {
		ProcList.stat(IFILE, true);

	}

}
