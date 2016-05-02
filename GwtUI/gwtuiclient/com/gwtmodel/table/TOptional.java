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
package com.gwtmodel.table;

public class TOptional<T> {

	final private T obj;

	private TOptional(T obj) {
		this.obj = obj;
	}

	public T orNull() {
		return obj;
	}

	public boolean isPresent() {
		return obj != null;
	}

	public T get() {
		assert obj != null;
		return obj;
	}

	public static <T> TOptional<T> of(T obj) {
		assert obj != null;
		return new TOptional<T>(obj);
	}

	public static <T> TOptional<T> fromNullable(T obj) {
		return new TOptional<T>(obj);
	}

}
