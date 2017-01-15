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
package com.gwtmodel.table.binder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
			.asList(new A(IAttrName.ATTRATRIBUTES), new A(IAttrName.ATTRBOOLEANATTRIBUTE), new A(IAttrName.ATTRID),
					new A(IAttrName.ATTRNAME), new A(IAttrName.ATTRNOINK, TT.BOOLEAN), new A(IAttrName.ATTRSTYLE),
					new A(IAttrName.ATTRTABINDEX, TT.INT), new A(IAttrName.ATTRCLASS), new A(IAttrName.ATTRARIALABEL))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> basebuttonMap = Arrays
			.asList(new A(IAttrName.ATTRFOCUSED, TT.BOOLEAN), new A(IAttrName.ATTRPOINTERDOWN, TT.BOOLEAN),
					new A(IAttrName.ATTRINVALID, TT.BOOLEAN), new A(IAttrName.ATTRPRESSED, TT.BOOLEAN),
					new A(IAttrName.ATTRTOGGLES, TT.BOOLEAN))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> baseenterMap = Arrays
			.asList(new A(IAttrName.ATTRERRORMESSAGE), new A(IAttrName.ATTRDISABLED, TT.BOOLEAN),
					new A(IAttrName.ATTRINVALID, TT.BOOLEAN), new A(IAttrName.ATTRVALIDATOR),
					new A(IAttrName.ATTRVALIDATORTYPE), new A(IAttrName.ATTRVALUE))
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
			new A(IAttrName.ATTRDISABLED, TT.BOOLEAN), new A(IAttrName.ATTRINVALID, TT.BOOLEAN),
			new A(IAttrName.ATTRKEYBINDINGS), new A(IAttrName.ATTRKEYEVENTTARGET),

			new A(IAttrName.ATTRRECEIVEDFOCUSFROMKEYBOARD, TT.BOOLEAN), new A(IAttrName.ATTRREQUIRED, TT.BOOLEAN),
			new A(IAttrName.ATTRSTOPKEYBORADFROMPROPAGATION, TT.BOOLEAN), new A(IAttrName.ATTRVALIDATOR),
			new A(IAttrName.ATTRVALIDATORTYPE), new A(IAttrName.ATTRVALUE)).stream()
			.collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> ironajaxMap = Arrays
			.asList(new A(IAttrName.ATTRACTIVEREQUESTS), new A(IAttrName.ATTRAUTO, TT.BOOLEAN),
					new A(IAttrName.ATTRBODY), new A(IAttrName.ATTRBUBBLES, TT.BOOLEAN),
					new A(IAttrName.ATTRDEBOUNCEDURATION), new A(IAttrName.ATTRCONTENTTYPE),
					new A(IAttrName.ATTRHANDLEAS), new A(IAttrName.ATTRHEADERS), new A(IAttrName.ATTRJSONPREFIX),
					new A(IAttrName.ATTRLASTERROR), new A(IAttrName.ATTRLASTREQUEST), new A(IAttrName.ATTRLASTRESPONSE),
					new A(IAttrName.ATTRLOADING, TT.BOOLEAN), new A(IAttrName.ATTRMETHOD), new A(IAttrName.ATTRPARAMS),
					new A(IAttrName.ATTRSYNC, TT.BOOLEAN), new A(IAttrName.ATTRTIMEOUT), new A(IAttrName.ATTRURL),
					new A(IAttrName.ATTRVERBOSE, TT.BOOLEAN), new A(IAttrName.ATTRWITHCREDENTIALS, TT.BOOLEAN))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> ironcollapseMap = Arrays
			.asList(new A(IAttrName.ATTROPENED, TT.BOOLEAN), new A(IAttrName.ATTRHORIZONTAL, TT.BOOLEAN),
					new A(IAttrName.ATTRNOANIMATION, TT.BOOLEAN))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> ironimageMap = Arrays
			.asList(new A(IAttrName.ATTRFADE, TT.BOOLEAN), new A(IAttrName.ATTRALT, TT.BOOLEAN),
					new A(IAttrName.ATTRERROR, TT.BOOLEAN), new A(IAttrName.ATTRHEIGHT),
					new A(IAttrName.ATTRLOADED, TT.BOOLEAN), new A(IAttrName.ATTRLOADING, TT.BOOLEAN),
					new A(IAttrName.ATTRPLACEHOLDER), new A(IAttrName.ATTRPOSITION),
					new A(IAttrName.ATTRPRELOAD, TT.BOOLEAN), new A(IAttrName.ATTRPREVENTLOAD, TT.BOOLEAN),
					new A(IAttrName.ATTRSIZING), new A(IAttrName.ATTRSRC), new A(IAttrName.ATTRWIDTH))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> ironlistMap = Arrays
			.asList(new A(IAttrName.ATTRAS), new A(IAttrName.ATTRFIRSTVISIBLEINDEX), new A(IAttrName.ATTRGRID),
					new A(IAttrName.ATTRINDEXAS), new A(IAttrName.ATTRITEMS), new A(IAttrName.ATTRKEYBINDINGS),
					new A(IAttrName.ATTRKEYEVENTTARGET), new A(IAttrName.ATTRLASTVISIBLEINDEX),
					new A(IAttrName.ATTRMAXPHYSICALCOUNT), new A(IAttrName.ATTRMULTISELECTION, TT.BOOLEAN),
					new A(IAttrName.ATTRSCROLLTARGET), new A(IAttrName.ATTRSELECTEDAS),
					new A(IAttrName.ATTRSELECTEDITEM), new A(IAttrName.ATTRSELECTEDITEMS),
					new A(IAttrName.ATTRSELECTIONENABLED, TT.BOOLEAN),
					new A(IAttrName.ATTRSTOPKEYBORADFROMPROPAGATION, TT.BOOLEAN))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> paperdropdownmenuMap = Arrays.asList(new A(IAttrName.ATTRACTIVE, TT.BOOLEAN),
			new A(IAttrName.ATTRALLOWOUTSIDESCROLL, TT.BOOLEAN), new A(IAttrName.ATTRALWAYSFLOATLABEL, TT.BOOLEAN),
			new A(IAttrName.ATTRARIAACTIVEATTRIBUTE), new A(IAttrName.ATTRDYNAMICALLIGN, TT.BOOLEAN),
			new A(IAttrName.ATTRHORIZONTALALIGN), new A(IAttrName.ATTRKEYBINDINGS), new A(IAttrName.ATTRKEYEVENTTARGET),
			new A(IAttrName.ATTRLABEL), new A(IAttrName.ATTRNAME), new A(IAttrName.ATTRNOANIMATION, TT.BOOLEAN),
			new A(IAttrName.ATTRNOLABELFLOAT, TT.BOOLEAN), new A(IAttrName.ATTROPENED, TT.BOOLEAN),
			new A(IAttrName.ATTRPLACEHOLDER), new A(IAttrName.ATTRRECEIVEDFOCUSFROMKEYBOARD, TT.BOOLEAN),
			new A(IAttrName.ATTRREQUIRED, TT.BOOLEAN), new A(IAttrName.ATTRSELECTEDITEM),
			new A(IAttrName.ATTRSELECTITEMLABEL), new A(IAttrName.ATTRSTOPKEYBORADFROMPROPAGATION, TT.BOOLEAN),
			new A(IAttrName.ATTRVERTICALALIGN)).stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> ironselectorMap = Arrays.asList(new A(IAttrName.ATTRACTIVATEEVENT),
			new A(IAttrName.ATTRATTRFORSELECTED), new A(IAttrName.ATTRFALLBACKSELECTION), new A(IAttrName.ATTRITEMS),
			new A(IAttrName.ATTRMULTI, TT.BOOLEAN), new A(IAttrName.ATTRSELECTABLE), new A(IAttrName.ATTRSELECTED),
			new A(IAttrName.ATTRSELECTEDATTRIBUTE), new A(IAttrName.ATTRSELECTEDCLASS),
			new A(IAttrName.ATTRSELECTEDITEM), new A(IAttrName.ATTRSELECTEDITEMS), new A(IAttrName.ATTRSELECTEDVALUES))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> vaadingridMap = Arrays
			.asList(new A(IAttrName.ATTRCOLUMNS), new A(IAttrName.ATTRCOLUMNREORDERINGALLOWED, TT.BOOLEAN),
					new A(IAttrName.ATTRDISABLED, TT.BOOLEAN), new A(IAttrName.ATTRDETAILEDEVENTS, TT.BOOLEAN),
					new A(IAttrName.ATTRFOOTER), new A(IAttrName.ATTRFROZENCOLUMN), new A(IAttrName.ATTRHEADER),
					new A(IAttrName.ATTRITEMS), new A(IAttrName.ATTRSELECTION), new A(IAttrName.ATTRSIZE),
					new A(IAttrName.ATTRSORTORDER), new A(IAttrName.ATTRVISIBLEROWS))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> vaadincomboboxMap = Arrays.asList(new A(IAttrName.ATTRALLOWCUSTOMVALUE, TT.BOOLEAN),
			new A(IAttrName.ATTRALLOWEDPATTERN), new A(IAttrName.ATTRALWAYSFLOATLABEL, TT.BOOLEAN),
			new A(IAttrName.ATTRAUTOFOCUS, TT.BOOLEAN), new A(IAttrName.ATTRHASVALUE, TT.BOOLEAN),
			new A(IAttrName.ATTRINPUTELEMENT), new A(IAttrName.ATTRINPUTMODE, TT.BOOLEAN), new A(IAttrName.ATTRITEMS),
			new A(IAttrName.ATTRITEMLABELPATH), new A(IAttrName.ATTRLABEL), new A(IAttrName.ATTRNAME),
			new A(IAttrName.ATTRNOLABELFLOAT, TT.BOOLEAN), new A(IAttrName.ATTROPENED, TT.BOOLEAN),
			new A(IAttrName.ATTRPLACEHOLDER), new A(IAttrName.ATTRREADONLY, TT.BOOLEAN),
			new A(IAttrName.ATTRREQUIRED, TT.BOOLEAN), new A(IAttrName.ATTRSELECTEDITEM), new A(IAttrName.ATTRSIZE))
			.stream().collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> vaadindatepickerMap = Arrays.asList(new A(IAttrName.ATTRAUTOVALIDATE, TT.BOOLEAN),
			new A(IAttrName.ATTRHASVALUE, TT.BOOLEAN), new A(IAttrName.ATTRI18N), new A(IAttrName.ATTRINITIALPOSITION),
			new A(IAttrName.ATTRKEYBINDINGS), new A(IAttrName.ATTRKEYEVENTTARGET),

			new A(IAttrName.ATTRLABEL), new A(IAttrName.ATTRNAME), new A(IAttrName.ATTROPENED, TT.BOOLEAN),
			new A(IAttrName.ATTRREADONLY), new A(IAttrName.ATTRREQUIRED, TT.BOOLEAN),
			new A(IAttrName.ATTRSTOPKEYBORADFROMPROPAGATION, TT.BOOLEAN)).stream()
			.collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> vaadinuploadMap = Arrays.asList(new A(IAttrName.ATTRACCEPT),
			new A(IAttrName.ATTRFILES), new A(IAttrName.ATTRHEADERS), new A(IAttrName.ATTRI18N),
			new A(IAttrName.ATTRMAXFILES), new A(IAttrName.ATTRMAXFILESIZE), new A(IAttrName.ATTRMETHOD),
			new A(IAttrName.ATTRNODROP, TT.BOOLEAN), new A(IAttrName.ATTRTARGET), new A(IAttrName.ATTRTIMEOUT)).stream()
			.collect(Collectors.toMap(A::getName, A::getType));

	private static Map<String, TT> vaadinsplitlayoutMap = Arrays.asList(new A(IAttrName.ATTRVERTICAL)).stream()
			.collect(Collectors.toMap(A::getName, A::getType));

	// VaadinSplitLayout

	private final static Map<WidgetTypes, Map<String, TT>> wMap = new HashMap<WidgetTypes, Map<String, TT>>();

	private final static Set<WidgetTypes> buttSet = Arrays
			.asList(WidgetTypes.PaperDropdownMenu, WidgetTypes.PaperToggleButton).stream().collect(Collectors.toSet());

	private final static Set<WidgetTypes> editSet = Arrays
			.asList(WidgetTypes.PaperDropdownMenu, WidgetTypes.VaadinComboBox, WidgetTypes.VaadinDatePicker).stream()
			.collect(Collectors.toSet());

	// VaadinSplitLayout

	static {
		wMap.put(WidgetTypes.PaperToast, paperToastMap);
		wMap.put(WidgetTypes.PaperToggleButton, papertogglebuttontMap);
		wMap.put(WidgetTypes.IronAjax, ironajaxMap);
		wMap.put(WidgetTypes.IronCollapse, ironcollapseMap);
		wMap.put(WidgetTypes.IronImage, ironimageMap);
		wMap.put(WidgetTypes.IronList, ironlistMap);
		wMap.put(WidgetTypes.PaperDropdownMenu, paperdropdownmenuMap);
		wMap.put(WidgetTypes.IronSelector, ironselectorMap);
		wMap.put(WidgetTypes.VaadinGrid, vaadingridMap);
		wMap.put(WidgetTypes.VaadinComboBox, vaadincomboboxMap);
		wMap.put(WidgetTypes.VaadinDatePicker, vaadindatepickerMap);
		wMap.put(WidgetTypes.VaadinUpload, vaadinuploadMap);
		wMap.put(WidgetTypes.VaadinSplitLayout, vaadinsplitlayoutMap);
	}

	private static TT getPolymerA(WidgetTypes w, String attr) {
		if (oMap.containsKey(attr))
			return oMap.get(attr);
		if (polymerMap.containsKey(attr))
			return polymerMap.get(attr);
		// w exists in wMap
		if (buttSet.contains(w) && basebuttonMap.containsKey(attr))
			return basebuttonMap.get(attr);
		if (editSet.contains(w) && baseenterMap.containsKey(attr))
			return baseenterMap.get(attr);
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
