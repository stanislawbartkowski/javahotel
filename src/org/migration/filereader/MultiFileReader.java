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

package org.migration.filereader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.migration.properties.PropHolder;

class MultiFileReader implements IFileReader {

	private final String[] fileS;
	private int currentf = -1;
	private BufferedReader b = null;

	MultiFileReader(String fList) {
		fileS = fList.split(",");
	}

	@Override
	public void close() throws Exception {
		if (b != null)
			b.close();
	}

	@Override
	public String readLine() throws IOException {
		while (true) {
			if (b == null) {
				currentf++;
				if (currentf >= fileS.length)
					return null;
				b = new BufferedReader(new FileReader(fileS[currentf]));
				// return artificial end of file marker
				if (currentf > 0)
					return (String) PropHolder.getProp().get(PropHolder.INPUTSTATTERM);
			}
			String l = b.readLine();
			if (l != null)
				return l;
			b.close();
			b = null;
			// next file
		}
	}

}
