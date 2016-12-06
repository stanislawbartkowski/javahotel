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
package com.gwtmodel.table.binder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.gwtmodel.table.common.ConvertTT;
import com.gwtmodel.table.common.TT;

public class BinderWidgetAttributes {

	private BinderWidgetAttributes() {
	}

	private static class A {
		final String name;
		final TT type;

		A(String name, TT type) {
			this.name = name;
			this.type = type;
		}

		A(String name) {
			this(name, TT.STRING);
		}

		public String getName() {
			return name;
		}

		public TT getType() {
			return type;
		}

	}

	private static Map<String, TT> oMap = Arrays
			.asList(new A(IAttrName.ATTRADDSTYLENAMES), new A(IAttrName.FIELDID), new A(IAttrName.ATTRSIZE),
					new A(IAttrName.ATTRPIXELSIZE), new A(IAttrName.ATTRHEIGHT), new A(IAttrName.ATTRSTYLENAME),
					new A(IAttrName.ATTRSTYLEPRIMARYNAME), new A(IAttrName.ATTRTITLE),
					new A(IAttrName.ATTRVISIBLE, TT.BOOLEAN), new A(IAttrName.ATTRWIDTH))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> polymerMap = Arrays
			.asList(new A(IAttrName.ATTRATRIBUTES), new A(IAttrName.ATTRBOOLEANATTRIBUTE),
					new A(IAttrName.ATTRDISABLED, TT.BOOLEAN), new A(IAttrName.ATTRID), new A(IAttrName.ATTRNAME),
					new A(IAttrName.ATTRNOINK, TT.BOOLEAN), new A(IAttrName.ATTRSTYLE),
					new A(IAttrName.ATTRTABINDEX, TT.INT), new A(IAttrName.ATTRCLASS), new A(IAttrName.ATTRARIALABEL))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> paperToastMap = Arrays
			.asList(new A(IAttrName.ATTRSETALWAYSONTOP, TT.BOOLEAN), new A(IAttrName.ATTRAUTOFITONATTACH, TT.BOOLEAN),
					new A(IAttrName.ATTRBACKDROPELEMENT), new A(IAttrName.ATTRCANCELED),
					new A(IAttrName.ATTRCLOSINGREASON), new A(IAttrName.ATTRDURATION, TT.BIGDECIMAL),
					new A(IAttrName.ATTRDYNAMICALLIGN, TT.BOOLEAN), new A(IAttrName.ATTRFITINFO),
					new A(IAttrName.ATTRHORIZONTALALIGN), new A(IAttrName.ATTRHORIZONTALOFFSET, TT.BIGDECIMAL),
					new A(IAttrName.ATTRNOAUTOFOCUS, TT.BOOLEAN), new A(IAttrName.ATTRNOCANCELONESCKEY, TT.BOOLEAN),
					new A(IAttrName.ATTRNOCANCELONOUTSIDECLICK, TT.BOOLEAN), new A(IAttrName.ATTRNOOVERLAP, TT.BOOLEAN),
					new A(IAttrName.ATTROPENED, TT.BOOLEAN), new A(IAttrName.ATTRPOSITIONTARGET),
					new A(IAttrName.ATTRRESTOREFOCUSONCLOSE, TT.BOOLEAN), new A(IAttrName.ATTRSIZINGTARGET),
					new A(IAttrName.ATTRTEXT), new A(IAttrName.ATTRVERTICALALIGN),
					new A(IAttrName.ATTRVETICALOFFSET, TT.BIGDECIMAL), new A(IAttrName.ATTRWITHBACKDROP, TT.BOOLEAN))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> papertogglebuttontMap = Arrays.asList(new A(IAttrName.ATTRACTIVE, TT.BOOLEAN),
			new A(IAttrName.ATTRARIAACTIVEATTRIBUTE), new A(IAttrName.ATTRCHECKED, TT.BOOLEAN),
			new A(IAttrName.ATTRFOCUSED, TT.BOOLEAN), new A(IAttrName.ATTRINVALID, TT.BOOLEAN),
			new A(IAttrName.ATTRKEYBINDINGS), new A(IAttrName.ATTRKEYEVENTTARGET),
			new A(IAttrName.ATTRPOINTERDOWN, TT.BOOLEAN), new A(IAttrName.ATTRPRESSED, TT.BOOLEAN),
			new A(IAttrName.ATTRRECEIVEDFOCUSFROMKEYBOARD, TT.BOOLEAN), new A(IAttrName.ATTRREQUIRED, TT.BOOLEAN),
			new A(IAttrName.ATTRSTOPKEYBORADFROMPROPAGATION, TT.BOOLEAN), new A(IAttrName.ATTRTOGGLES, TT.BOOLEAN),
			new A(IAttrName.ATTRVALIDATOR), new A(IAttrName.ATTRVALIDATORTYPE), new A(IAttrName.ATTRVALUE)).stream()
			.collect(Collectors.toMap(A::getName, A::getType));

	private final static Map<WidgetTypes, Map<String, TT>> wMap = new HashMap<WidgetTypes, Map<String, TT>>();

	static {
		wMap.put(WidgetTypes.PaperToast, paperToastMap);
		wMap.put(WidgetTypes.PaperToggleButton, papertogglebuttontMap);
	}

	private static TT getPolymerA(WidgetTypes w, String attr) {
		if (oMap.containsKey(attr))
			return oMap.get(attr);
		if (polymerMap.containsKey(attr))
			return polymerMap.get(attr);
		// w exists in wMap
		return wMap.get(w).get(attr);
	}

	public interface IWidgetAttribute {

		String errval();

		String getString();

		boolean getBool();
	}

	public static boolean isBinderWidgetAttr(WidgetTypes w) {
		return wMap.containsKey(w);
	}

	public static IWidgetAttribute getWidgetAttribute(WidgetTypes w, String attr, String val) {
		TT t = getPolymerA(w, attr);
		if (t == null)
			return null;
		if (t == TT.BOOLEAN) {
			if (!IAttrName.BOOLEANFALSE.equals(val) && !IAttrName.BOOLEANTRUE.equals(val))
				return new IWidgetAttribute() {

					@Override
					public String errval() {
						return "Only " + IAttrName.BOOLEANFALSE + " or " + IAttrName.BOOLEANTRUE
								+ " allowed as bool value. " + val + " found";
					}

					@Override
					public String getString() {
						return null;
					}

					@Override
					public boolean getBool() {
						return false;
					}

				};
		}
		final Object o = ConvertTT.toO(t, val);

		if (o == null)
			return new IWidgetAttribute() {

				@Override
				public String errval() {
					return "Attribute type " + t.name() + " Incorrect value found";
				}

				@Override
				public String getString() {
					return null;
				}

				@Override
				public boolean getBool() {
					return false;
				}

			};

		return new IWidgetAttribute() {

			@Override
			public String errval() {
				return null;
			}

			@Override
			public String getString() {
				if (t != TT.BOOLEAN)
					return val;
				return null;
			}

			@Override
			public boolean getBool() {
				if (t == TT.BOOLEAN)
					return (Boolean) (o);
				return false;
			}

		};

	}

}
