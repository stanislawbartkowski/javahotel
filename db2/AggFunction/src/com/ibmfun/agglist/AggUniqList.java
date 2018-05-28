/*
 * Copyright 2018 stanislawbartkowski@gmail.com  
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
package com.ibmfun.agglist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class AggUniqList {
	
    private static String ECHOFILE="debug_output";
    private static boolean DEBUG = true;
    
    private static File getTempFile() {
    	return new File(System.getProperty("java.io.tmpdir"),ECHOFILE);
    }
    
	
    private static void initDebug() throws IOException {
    	if (!DEBUG) return;
    	File f = getTempFile();
    	f.delete();
    	f.createNewFile();
    }
    
	// testing
	// for debugging only, now empty
	private static void echo(String mess) throws IOException {
		if (!DEBUG) return;
    	File f = getTempFile();
    	try (BufferedWriter w = new BufferedWriter(new FileWriter(f,true))) {
    		w.write(mess); w.newLine();
    		w.flush();
    		w.close();
    	}
	}

	// delimiter
	private static final String D = "" + (char)28;
	private static final char FF = ',';

	public static void listagg_initialize(String s[]) throws IOException {
//		initDebug();
		echo("Initalize");
		s[0] = null;
	}

	public static void listagg_accumulate(String in, String s[]) throws IOException {
//		echo("Accumulate");
		if (in == null)
			return; // ignore nulls
		if (s[0] == null) {
			// first run
			s[0] = in;
			return;
		}
		// break down existing list
		String list[] = s[0].split(D);
		// look for duplicate
		for (String st : list)
			if (st.equals(in))
				return; // duplicate
		// add to the end of list
		s[0] += D + in;
	}

	public static void listagg_merge(String in, String s[]) throws IOException {
		echo("Merge");
		s[0] = in;
	}

	public static String listagg_finalize(String in) throws IOException {
		echo("Finalize, sort");
		// TODO: if not data return NULL or empty string ?
		if (in == null) return "";
		String list[] = in.split(D);
		List<String> sorted  = new ArrayList<String>();
		for (String s : list) sorted.add(s);
		Collections.sort(sorted);
		StringBuffer result = new StringBuffer();
		for (String s : sorted) {
			if (result.length() != 0) result.append(FF);
			result.append(s);
		}	
		return result.toString();
	}

}
