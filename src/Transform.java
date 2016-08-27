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
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.transform.jpk.JPK;

public class Transform {

	private static void P(String s) {
		System.out.println(s);
	}

	public static void main(String[] args) throws TransformerFactoryConfigurationError, Exception {

		if (args.length != 2) {
			P("Utworzenie plików do wysyłki (2016/08/21 r:0)");
			P("Plik InitUpload.xml powinien być następnie podpisany cyfrowo");
			P("");
			P("Wywołanie:");
			P("Transform <configuration file> <vatfile>");
			System.exit(4);
		}
		JPK.Prepare(args[0], args[1]);
	}

}
