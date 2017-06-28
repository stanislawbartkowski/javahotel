package org.migration.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.migration.extractor.ObjectExtractor;
import org.migration.extractor.ObjectExtractor.OBJECT;
import org.migration.fix.FixObject;
import org.migration.fix.impl.RemoveEditionable;
import org.migration.tasks.ExtractObjects;

public class Test5 extends TestHelper {

	@Test
	public void test1() throws FileNotFoundException, URISyntaxException {

		BufferedReader r = openFile("rsample13.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("SCHEDATA.CIC_GETSCHE_F", i.getName());
		assertEquals(OBJECT.FUNCTION, i.getType());
		i = o.extractNext();
		assertNotNull(i);
	}

	@Test
	public void test2() throws Exception {
		FixObject.register(ObjectExtractor.OBJECT.FUNCTION, new RemoveEditionable());
		ExtractObjects.extractObjects(getFileName("rsample13.ora"), getOutputDir());
		verifyFile("function/schedata_cic_getobkjectid_f.db2", 30);		
		for (String s : lastL) {
			System.out.println(s);
			assertFalse(s.contains("EDITIONABLE"));
		}
	}

}
