/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.systemt.aql;

import com.ibm.avatar.api.CompileAQL;
import com.ibm.avatar.api.CompileAQLParams;

public class CompileAQLModule {
	
	public static void compile(String[] moduleFiles,String outputDir,boolean multi) throws Exception {
				
		CompileAQLParams moduleCompilationParams = new CompileAQLParams();
		moduleCompilationParams.setInputModules(moduleFiles);
		moduleCompilationParams.setOutputURI(outputDir);
		moduleCompilationParams.setTokenizerConfig(Utils.getTokenizer(multi));
		CompileAQL.compile(moduleCompilationParams);
	}


}
