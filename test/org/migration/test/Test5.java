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
import org.migration.properties.PropHolder;
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

	@Test
	public void test3() throws FileNotFoundException, URISyntaxException {
		PropHolder.getProp().put(PropHolder.INPUTSTATTERM, "@");
		BufferedReader r = openFile("rsample14.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("SCHEMA.XDATA", i.getName());
		assertEquals(OBJECT.PACKAGE, i.getType());
		i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("SCHEMA.XDATA", i.getName());
		assertEquals(OBJECT.BODY, i.getType());
	}

	@Test
	public void test4() throws Exception {
		PropHolder.getProp().put(PropHolder.INPUTSTATTERM, "@");
		FixObject.register(ObjectExtractor.OBJECT.BODY, new RemoveEditionable());
		FixObject.register(ObjectExtractor.OBJECT.PACKAGE, new RemoveEditionable());
		ExtractObjects.extractObjects(getFileName("rsample14.ora"), getOutputDir());
		verifyFile("package/schema_xdata.db2", 5);
		for (String s : lastL) {
			System.out.println(s);
			assertFalse(s.contains("EDITIONABLE"));
		}
		verifyFile("body/schema_xdata.db2", 8);
		for (String s : lastL) {
			System.out.println(s);
			assertFalse(s.contains("EDITIONABLE"));
		}
	}

	@Test
	public void test5() throws FileNotFoundException, URISyntaxException {
		PropHolder.getProp().put(PropHolder.INPUTSTATTERM, "/");
		BufferedReader r = openFile("rsample15.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		System.out.println(i.getName() + " " + i.getType());
		assertEquals("TESTPROC", i.getName());
		assertEquals(OBJECT.PROCEDURE, i.getType());
		i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("TESTPROC1", i.getName());
		assertEquals(OBJECT.PROCEDURE, i.getType());		
	}

}
