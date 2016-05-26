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

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public class Empty {

	private Empty() {
	}

	private static class EmptyDataType implements IDataType {

		@Override
		public boolean eq(IDataType dType) {
			// return true;
			return (dType instanceof EmptyDataType);
		}
	}

	private static class EmptyDataType1 implements IDataType {

		@Override
		public boolean eq(IDataType dType) {
			// return true;
			return (dType instanceof EmptyDataType1);
		}
	}

	private static class EmptyFieldType extends AbstractVField {

		EmptyFieldType() {
			super(IConsts.UKNOWNHTMLNAME, FieldDataType.constructString());
		}

		@Override
		public boolean eq(IVField o) {
			// return true;
			return (o instanceof EmptyFieldType);
		}

	}

	private static class EmptyDecimalFieldType extends AbstractVField {

		EmptyDecimalFieldType() {
			super(IConsts.UKNOWNHTMLNAME, FieldDataType.constructBigDecimal());
		}

		@Override
		public boolean eq(IVField o) {
			return (o instanceof EmptyDecimalFieldType);
		}

	}

	private static IDataType eType;
	private static IDataType eType1;
	private static IVField eField;
	private static IVField deField;

	static {
		eType = new EmptyDataType();
		eType1 = new EmptyDataType1();
		eField = new EmptyFieldType();
		deField = new EmptyDecimalFieldType();
	}

	public static IVField getFieldType() {
		return eField;
	}

	public static IDataType getDataType() {
		return eType;
	}

	public static IDataType getDataType1() {
		return eType1;
	}

	public static IVField getDecimalType() {
		return deField;
	}
}
