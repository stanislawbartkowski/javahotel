/*
 *  Copyright 2016 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table;

import com.gwtmodel.table.common.IMessConsts;

/**
 * 
 * @author hotel
 */
public interface IConsts {

	int defaultDecimal = 2;
	int defaultPage = 20;
	String errorStyle = "dialog-empty-field";
	String numberStyle = "Number";
	String nowrapStyle = "no_wrap_cell_style";
	int actionImageWidth = 12;
	int actionImageHeight = 12;
	String DEFJSONROW = "row";
	String JSONROWLIST = "list";
	int CHUNK_SIZE = 100;
	String LOGOUTHTMLNAME = "logout";
	String UKNOWNHTMLNAME = "XXX";
	String EMPTYCLASSSTYLE = "dialog-empty-field";
	int MonthPanel = 12;
	String LABELPREFIX = "label-";
	String LABELFORPREFIX = "labelfor-";
	String IJSCALL = "JS.";
	String SPINNERSTYLE = "spinner-style";
	String EMAILSTYLE = "email-style";
	String JSTRUE = "true";
	String JSFALSE = "false";
	String JSNULL = "null";
	String JSUNDEFINED = "undefined";
	int headerImageWidth = 20;
	int headerImageHeight = 20;
	String EMPTYIM = "-";

	String GWT = "gwtwidget";
	String POLYMER = "polymerwidget";
	String ATTRSIZE = "size";
	String ATTRPIXELSIZE = "pixelSize";
	String ATTRHEIGHT = "height";
	String ATTRSTYLENAME = "styleName";
	String ATTRSTYLEPRIMARYNAME = "stylePrimaryName";
	String ATTRTITLE = "title";
	String ATTRVISIBLE = "visible";
	String ATTRWIDTH = "width";
	String ATTRTEXT = "text";
	String ATTRHTML = "html";
	String ATTRHTMLC = "HTML";
	String ATTREENABLED = "enabled";
	String ATTRATRIBUTES = "attributes";
	String ATTRBOOLEANATTRIBUTE = "booleanAttribute";
	String ATTRDISABLED = "disabled";
	String ATTRID = "id";
	String ATTRNAME = "name";
	String ATTRNOINK = "noink";
	String ATTRSTYLE = "style";
	String ATTRFOCUSED = "focused";
	String ATTRPOINTERDOWN = "pointerDown";
	String ATTRPRESSED = "pressed";
	String ATTRTOGGLES = "toggles";
	String ATTRICON = "icon";
	String ATTRSRC = "src";
	String ATTRTHEME = "theme";
	String ATTRADDSTYLENAMES = "addStyleNames";
	String ATTRTABINDEX = "tabindex";
	String ATTRACTIVE = "active";
	String ATTRRAISED = "raised";
	String ATTRELEVATION = "elevation";
	String ATTRELEVATIONFLOAT = "elevationFloat";
	String ATTRCLASS = "class";
	String ATTRATTOP = "atTop";
	String ATTRMODE = "mode";
	String ATTRSHADOW = "shadow";
	String ATTRTALLCLASS = "tallClass";
	String ATTRBOTTOMJUSTIFY = "bottomJustify";
	String ATTRJUSTIFY = "justify";
	String ATTRMIDDLEJUSTIFY = "middleJustify";
	String ATTRURL = "url";
	String ATTRALTTEXT = "altText";
	String ATTRDEFAULTSELECTED = "defaultSelected";
	String ATTRDISABLEEDGESWIPE = "disableEdgeSwipe";
	String ATTRDRAGGING = "dragging";
	String ATTRDRAWERTOGGLEATTRIBUTE = "drawerToggleAttribute";
	String ATTRDRAWEWIDTH = "drawerWidth";
	String ATTREDGESWUPESENSITIVITY = "edgeSwipeSensitivity";
	String ATTREDGESWUPESENSITIVITYPIXELS = "edgeSwipeSensitivityPixels";
	String ATTRFORCENARROW = "forceNarrow";
	String ATTRHASTRANSFORM = "hasTransform";
	String ATTRHADWILLCHANGE = "hasWillChange";
	String ATTRNARROW = "narrow";
	String ATTRPEEKING = "peeking";
	String ATTRRESPONSICEWIDTH = "responsiveWidth";
	String ATTRRIGHTDRAWER = "rightDrawer";
	String ATTRSELECTED = "selected";
	String ATTRCHECKED = "checked";
	String ATTRINVALID = "invalid";
	String ATTRVALIDATOR = "validator";
	String ATTRVALIDATORTYPE = "validatorType";
	String ATTRVALUE = "value";
	String ATTRANIMATIONCONFIG = "animationConfig";
	String ATTRAUTOFITONATTACH = "autoFitOnAttach";
	String ATTRCANCELED = "canceled";
	String ATTRETRYANIMATION = "entryAnimation";
	String ATTREXITANIMATION = "exitAnimation";
	String ATTRFITINFO = "fitInto";
	String ATTRMODAL = "modal";
	String ATTRNOAUTOFOCUS = "noAutoFocus";
	String ATTRNOCANCELONESCKEY = "noCancelOnEscKey";
	String ATTRNOCANCELONOUTSIDECLICK = "noCancelOnOutsideClick";
	String ATTROPENED = "opened";
	String ATTRSIZINGTARGET = "sizingTarget";
	String ATTRWITHBACKDROP = "withBackdrop";
	String ATTRDIALOGELEMENT = "dialogElement";
	String ATTRALWAYSFLOATLABEL = "alwaysFloatLabel";
	String ATTRARIAACTIVEATTRIBUTE = "ariaActiveAttribute";
	String ATTRHORIZONTALALIGN = "horizontalAlign";
	String ATTRKEYBINDINGS = "keyBindgings";
	String ATTRKEYEVENTTARGET = "keyEventTarget";
	String ATTRLABEL = "label";
	String ATTRNOANIMATION = "noAnimations";
	String ATTRNOLABELFLOAT = "noLabelFloat";
	String ATTRPLACEHOLDER = "placeholder";
	String ATTRRECEIVEDFOCUSFROMKEYBOARD = "receivedFocusFromKeyboard";
	String ATTRREQUIRED = "required";
	String ATTRSELECTEDITEMS = "selectedItems";
	String ATTRSELECTEDITEM = "selectedItem";
	String ATTRSELECTITEMLABEL = "selectedItemLabel";
	String ATTRSTOPKEYBORADFROMPROPAGATION = "stopKeyboardEventPropagation";
	String ATTRVERTICALALIGN = "verticalAlign";
	String ATTRACTIVATEEVENT = "activateEvent";
	String ATTRFORITEMTITLE = "attrForItemTitle";
	String ATTRATTRFORSELECTED = "attrForSelected";
	String ATTRFOCUSEDITEM = "focusedItem";
	String ATTRITEMS = "items";
	String ATTRMULTI = "multi";
	String ATTRSELECTEDATTRIBUTE = "selectedAttribute";
	String ATTRSELECTEDVALUES = "selectedValues";
	String ATTRALIGNBOTTOM = "alignBottom";
	String ATTRDISABLEDRAG = "disableDrag";
	String ATTRHIDESCROLLBUTTONS = "hideScrollButtons";
	String ATTRNOBAR = "noBar";
	String ATTRNOSLIDE = "noSlide";
	String ATTRNOCINK = "noInk";
	String ATTRMINI = "mini";
	String ATTRARIALABEL = "ariaLabel";
	String ATTRACCEPT = "accept";
	String ATTRALLOWEDPATTERN = "allowedPattern";
	String ATTRAUTOCAPITALIZE = "autocapitalize";
	String ATTRAUTOCOMPLETE = "autocomplete";
	String ATTRAUTOCORRECT = "autocorrect";
	String ATTRAUTOFOCUS = "autofocus";
	String ATTRAUTOSAVE = "autosave";
	String ATTRAUTOVALIDATE = "autoValidate";
	String ATTRCHARCOUNTER = "charCounter";
	String ATTRERRORMESSAGE = "errorMessage";
	String ATTRINPUTMODE = "inputMode";
	String ATTRLIST = "list";
	String ATTRMAX = "max";
	String ATTRMAXS = "maxS";
	String ATTRMAXLENGTH = "maxlength";
	String ATTRMAXLENGTHS = "maxlengthS";
	String ATTRMIN = "min";
	String ATTRMINS = "minS";
	String ATTRMINLENGTH = "minlength";
	String ATTRMINLENGTHS = "minlengthS";
	String ATTRPATTERN = "pattern";
	String ATTRPREVENTINVALIDINPUT = "preventInvalidInput";
	String ATTRREADONLY = "readOnly";
	String ATTRRESULS = "results";
	String ATTRRESULSS = "resultsS";
	String ATTRSIZES = "sizeS";
	String ATTRTYPE = "type";
	String ATTRMAXROWS = "maxRows";
	String ATTRMAXROWSS = "maxRowsS";
	String ATTRMULTIPLE = "multiple";
	String ATTRROWS = "rows";
	String ATTRROWSS = "rowsS";
	String ATTRANIMATED = "animated";
	String ATTRRATIO = "ratio";
	String ATTRRATIOS = "ratioS";
	String ATTRSECONDARYPROGESS = "secondaryProgress";
	String ATTRSECONDARYPROGESSS = "secondaryProgressS";
	String ATTRSECONDARYRATIO = "secondaryRatio";
	String ATTRSECONDARYRATIOS = "secondaryRatioS";
	String ATTRSTEP = "step";
	String ATTRSTEPS = "stepS";
	String ATTRVALUES = "valueS";
	String ATTRINDETERMINATE = "indeterminate";
	String ATTRALLOWEMPTYSELECTION = "allowEmptySelection";
	String ATTRSELECTABLE = "selectable";
	String ATTRSELECTEDCLASS = "selectedClass";
	String ATTRANIMATING = "animating";
	String ATTRCENTER = "center";
	String ATTRHOLDDOWN = "holdDown";
	String ATTRINITIALOPACITY = "initialOpacity";
	String ATTRINITIALOPACITYS = "initialOpacityS";
	String ATTROPACITYDECAYVELOCITY = "opacityDecayVelocity";
	String ATTROPACITYDECAYVELOCITYS = "opacityDecayVelocityS";
	String ATTRRECENTERS = "recenters";
	String ATTRRIPPLES = "ripples";
	String ATTRALT = "alt";
	String ATTRDRAGGINF = "dragging";
	String ATTREDITABLE = "editable";
	String ATTREXPAND = "expand";
	String ATTRIMMEDIATEVALUE = "immediateValue";
	String ATTRIMMEDIATEVALUES = "immediateValueS";
	String ATTRMARKERS = "markers";
	String ATTRMAXMARKERS = "maxMarkers";
	String ATTRMAXMARKERSS = "maxMarkersS";
	String ATTRPIN = "pin";
	String ATTRSECONDARYPROGRESS = "secondaryProgress";
	String ATTRSECONDARYPROGRESSS = "secondaryProgressS";
	String ATTRSNAPS="snaps";
	String ATTRTRANSITING="transiting";
	String ATTRSCROLLABLE="scrollable";
	String ATTRAUTOSELECT="autoselect";
	String ATTRLINK="link";

	String RESBEG = "{" + IMessConsts.STANDCH;
	String RESEND = "}";
};