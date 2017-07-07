package org.migration.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.migration.extractor.ObjectExtractor;
import org.migration.properties.PropHolder;

public class Test6 extends TestHelper {
	
	@Test
	public void test1() throws FileNotFoundException, URISyntaxException {

		String fName = getfName("rsample12.ora");
		fName = fName + "," + getfName("rsample15.ora");
		ObjectExtractor o = new ObjectExtractor(fName);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("PROC.TEXT_PROC",i.getName());
		i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("TESTPROC",i.getName());
		i = o.extractNext();
		System.out.println(i.getName());
		assertEquals("TESTPROC1",i.getName());
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
		assertEquals("SCHEMA.XDATA",i.getName());
		i = o.extractNext();
		assertNotNull(i);
		System.out.println(i.getName());
		assertEquals("SCHEMA.XDATA77",i.getName());
		i = o.extractNext();
		assertNull(i);
	}


}
