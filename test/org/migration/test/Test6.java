package org.migration.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.migration.extractor.ObjectExtractor;
import org.migration.fix.FixObject;
import org.migration.fix.impl.RemoveEnable;
import org.migration.properties.PropHolder;
import org.migration.tasks.ExtractObjects;

public class Test6 extends TestHelper {

	@Test
	public void test1() throws FileNotFoundException, URISyntaxException {

		String fName = getfName("rsample12.ora");
		fName = fName + "," + getfName("rsample15.ora");
		ObjectExtractor o = new ObjectExtractor(fName);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("PROC.TEXT_PROC", i.getName());
		i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("TESTPROC", i.getName());
		i = o.extractNext();
		System.out.println(i.getName());
		assertEquals("TESTPROC1", i.getName());
	}

	@Test
	public void test2() throws FileNotFoundException, URISyntaxException {
		PropHolder.getProp().put(PropHolder.INPUTSTATTERM, "@");
		PropHolder.getProp().setProperty(PropHolder.SCHEMASONLY, "SCHEMA");
		BufferedReader r = openFile("rsample16.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("SCHEMA.XDATA", i.getName());
		i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("SCHEMA.XDATA77", i.getName());
		i = o.extractNext();
		assertNull(i);
	}

	@Test
	public void test3() throws Exception {
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new RemoveEnable());
		ExtractObjects.extractObjects(getFileName("rsample17.ora"), getOutputDir());
		verifyFile("table/sche_table.db2",12);
		for (String l : lastL) {
			System.out.println(l);
			assertFalse(l.contains("ENABLE"));
		}
	}

	@Test
	public void test4() throws Exception {
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new RemoveEnable());
		ExtractObjects.extractObjects(getFileName("rsample18.ora"), getOutputDir());
		verifyFile("table/sche_table.db2",13);
		for (String l : lastL) {
			System.out.println(l);
			assertFalse(l.contains("ENABLE"));
			assertFalse(l.contains("SUPPLEMENTAL"));
		}
	}

}
