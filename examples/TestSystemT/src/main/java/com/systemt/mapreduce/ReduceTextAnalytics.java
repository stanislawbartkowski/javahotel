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
package com.systemt.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReduceTextAnalytics extends Reducer<Text, Text, Text, Text> {

	@Override
	public void setup(Context context) {
	}

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String content = null;
		for (Text tt : values) {
			if (content == null)
				content = tt.toString();
			else
				content += System.lineSeparator() + tt.toString();
		}
		context.write(key, new Text(content));
	}
}
