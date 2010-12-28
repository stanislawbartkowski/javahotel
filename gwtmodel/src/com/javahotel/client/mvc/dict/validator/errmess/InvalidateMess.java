/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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

package com.javahotel.client.mvc.dict.validator.errmess;

import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class InvalidateMess {

	private final IField fie;
	private final boolean empty;
	private final String errmess;

	public InvalidateMess(final IField fie, final boolean e, final String err) {
		this.fie = fie;
		this.empty = e;
		this.errmess = err;
	}

	public InvalidateMess(final IField fie, final String err) {
		this.fie = fie;
		this.empty = false;
		this.errmess = err;
	}

	/**
	 * @return the fie
	 */
	public IField getFie() {
		return fie;
	}

	/**
	 * @return the empty
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * @return the errmess
	 */
	public String getErrmess() {
		return errmess;
	}

}
