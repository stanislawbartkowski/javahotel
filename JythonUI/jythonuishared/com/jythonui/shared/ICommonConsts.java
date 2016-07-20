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
package com.jythonui.shared;

/**
 * @author hotel
 * 
 *         Shared constants
 */
public interface ICommonConsts {

	String ID = "id";
	String DISPLAYNAME = "displayname";
	String UPMENU = "upmenu";
	String LEFTMENU = "leftmenu";
	String LEFTSTACK = "leftstack";
	String DIALOG = "dialog";
	String BUTTON = "button";
	String BUTTONHEADER = "header";
	String DATELINE = "dateline";
	String FORM = "form";
	String FIELD = "field";
	String IMPORT = "import";
	String METHOD = "method";
	String HTMLPANEL = "htmlpanel";
	String UIBINDER = "uibinder";
	String HTMLLEFTMENU = "htmlleftmenu";
	String HTMLTYPE = "html";
	String JSCODE = "jscode";
	String CSSCODE = "csscode";
	String BEFORE = "before";
	String LIST = "list";
	String TOOLBARTYPE = "toolbartype";
	String NOWRAPLIST = "nowrap";
	String COLUMN = "column";
	String COLUMNS = "columns";
	String LISTBUTTONSLIST = "buttons-addlist";
	String LISTBUTTONSVALIDATE = "buttons-validate";
	String LISTBUTTONSSELECTED = "buttons-selected";
	String LISTSELECTEDMESS = "buttons-selectedmess";
	String JSTATUSSET = "JSTATUS_SET_";
	String JSTATUSTEXT = "JSTATUS_TEXT_";
	String REMEMBER = "remember";
	String REMEMBERKEY = "rememberkey";
	String TYPE = "type";
	String STRINGTYPE = "string";
	String BOOLTYPE = "bool";
	String DECIMALTYPE = "decimal";
	String DATETYPE = "date";
	String DATETIMETYPE = "datetime";
	String UPLOADTYPE = "upload";
	String DOWNLOADTYPE = "download";
	String INTTYPE = "int";
	String LONGTYPE = "long";
	String SPINNERTYPE = "spinner";
	String SPINNERMIN = "spinnermin";
	int DEFAULTSPINNERMIN = 1;
	String SPINNERMAX = "spinnermax";
	int DEFAULTSPINNERMAX = 5;
	String EMAILTYPE = "email";
	String AFTERDOT = "afterdot";
	String DEFVALUE = "defvalue";
	String CELLTITLE = "celltitle";
	String COLNO = "colno";
	String FOOTER = "footer";
	String FOOTERTYPE = "footertype";
	String FOOTERALIGN = "footeralign";
	String FOOTERAFTERDOT = "footerafterdot";
	String IMAGECOLUMN = "imagecolumn";
	String IMAGELIST = "imagelist";
	String CURRENTPOS = "currentpos";
	String LABEL = "label";
	String ASXML = "asxml";
	String FORMPANEL = "formpanel";
	String AUTOHIDE = "autohide";
	String MODELESS = "modeless";
	String CLEARLEFT = "clearleft";
	String JSMODIFROW = "jsmodifrow";
	String CLEARCENTRE = "clearcentre";
	String NOPROPERTYCOLUMN = "nopropertycolumn";
	String LEFT = "left";
	String TOP = "top";
	String MAXLEFT = "maxleft";
	String MAXTOP = "maxtop";
	String JXMLCONTENT = "JXMLCONTENT";
	String JXMLSETCONTENT = "JXMLCONTENTSET";
	int DEFAULTCOLNO = 14;
	int DEFAULTROWNO = 20;
	int DEFPAGESIZE = 20;
	String TABPANEL = "tabpanel";
	String TABPANELELEM = "tabelem";
	String DISCLOSUREPANEL = "disclosurepanel";
	String DISCLOUSREPANELELEM = "disclosureelem";
	String DATELINEFORMS = "files";
	String DATELINEFORM = "filedef";
	String DATELINEDEFAFILE = "defafile";
	String DATALINEFILE = "file";
	String DATALINEFILEDEFAULT = "file";
	String DATELINEID = "lineid";
	String DATELINEDATEID = "dateid";
	String DATELINEDATEIDDEFAULT = "datecol";
	String DATELINEFORMDEFAULT = "form";
	String EDITCOL = "editable";
	String SIGNALCLOSE = "signalclose";
	int DEFAULTAFTERDOT = 2;
	String JLISTMAP = "JLIST_MAP";
	String JCHARTMAP = "JCHART_MAP";
	String JCHECKLISTMAP = "JCHECK_MAP";
	String JDATELINEMAP = "JDATELINE_MAP";
	String ELEMFORMAT = "elemformat";
	String NOTEMPTY = "notempty";
	String HIDDEN = "hidden";
	String READONLY = "readonly";
	String READONLYADD = "readonlyadd";
	String READONLYCHANGE = "readonlychange";
	String JLIST_NAME = "JLIST_NAME";
	String PARENT = "parent";
	String JERROR = "JERROR";
	String JMAINDIALOG = "JMAIN_DIALOG";
	String JSACTION = "jsaction";
	String ACTIONTYPE = "actiontype";
	String ACTIONPARAM = "actionparam";
	String ACTIONPARAM1 = "actionparam1";
	String ACTIONPARAM2 = "actionparam2";
	String ACTIONPARAM3 = "actionparam3";
	String JEDITLISTROWACTION = "editlistrowaction";
	String JCLICKIMAGEACTION = "clickimage";
	String BEFORECHANGETAB = "beforechangetab";
	String TABID = "tabid";
	String TABPANELID = "tabpanelid";
	String JEDITLISTROWNO = "JLIST_EDIT_ROWNO_";
	String JEDITLISTACTION = "JLIST_EDIT_ACTION_";
	String JEDITROWYESACTION = "JLIST_EDIT_ACTIONOK_";
	String JEDITROW_OK = "JEDIT_ROW_OK_";
	String BUTTONS = "buttons";
	String JUPDIALOG = "JUP_DIALOG";
	String JCLOSEDIALOG = "JCLOSE_DIALOG";
	String JCLOSEBUTTON = "JCLOSE_BUTTON";
	String ACTIONS = "actions";
	String ACTION = "action";
	String ACTIONID = "actionid";
	String JSUBMIT = "J_SUBMIT";
	String JURL_OPEN = "JURL_OPEN";
	String JOKMESSAGE = "JOK_MESSAGE";
	String JYESNOMESSAGE = "JYESNO_MESSAGE";
	String JYESANSWER = "JYESANSWER";
	String JERRORMESSAGE = "JERROR_MESSAGE";
	String JMESSAGE_TITLE = "JMESSAGE_TITLE";
	String JAFTERDIALOGACTION = "JAFTERDIALOG_ACTION";
	String CLOSEDIALOGIMAGE = "closeimage";
	String JCRUD_AFTERCONF = "JCRUD_AFTERCONF";
	String JLOGOUTACTION = "JLOGOUT_ACTION";
	String JEXECUTEACTION = "JEXECUTE_ACTION";
	String JFOOTER = "JFOOTER_";
	String JLISTEDITMODE = "MODE";
	String TYPES = "types";
	String COMBOID = "comboid";
	String IMAGENAME = "imagebutton";
	String TYPEDEFS = "typedefs";
	String TYPEDEF = "typedef";
	String CUSTOMTYPE = "custom:";
	String SIGNALCHANGE = "signalchange";
	String AFTERSUBMIT = "aftersubmit";
	String DISCLOSURECHANGE = "disclosurechange";
	String DISCLOSUREID = "disclosureid";
	String DISCLOSUREOPEN = "disclosureopen";
	String JSUBMITERES = "JSUBMITRES";
	String SIGNALBEFORE = "signalbefore";
	String JVALBEFORE = "JVALBEFORE";
	String SIGNALCHANGEFIELD = "changefield";
	String SIGNALAFTERFOCUS = "changeafterfocus";
	String SIGNALCOLUMNCHANGE = "columnchangeaction";
	String SIGNALAFTERROW = "aftereditrow";
	String SIGNALBEFOREROW = "beforerow";
	String HELPER = "helper";
	String LINESET = "_lineset";
	String STANDBUTT = "standbutt";
	String HELPERREFRESH = "helperrefresh";
	String COMBOTYPE = "combo";
	String TEXTAREA = "textarea";
	String RICHTEXT = "richtext";
	String FROM = "from";
	String VALIDATE = "validate";
	String JCOPY = "JCOPY_";
	String VALIDATERULES = "validaterules";
	String VALIDATEOP = "op";
	String VALIDATEID1 = "id1";
	String WIDTH = "width";
	String VISLINES = "vislines";
	String EDITCLASS = "editclass";
	String EDITCSS = "editcss";
	String COLUMNCLASS = "columnclass";
	String HEADERCLASS = "headerclass";
	String DATEPANELCLASSDEFAULT = "datepanel";
	String CLASSNAME = "cssclass";
	String PAGESIZE = "pagesize";
	String ALIGN = "align";
	String CHUNKED = "chunked";
	String PASSWORD = "password";
	String SUGGEST = "suggest";
	int DEFAULTSUGGESTSIZE = 100;
	String SUGGESTKEY = "suggestkey";
	String SUGGESTSIZE = "suggestsize";
	String HTMLID = "htmlid";
	String AUTHENTICATE = "Authenticate";
	String CLOSEOUT = "CloseOut";
	String YESAUTHENTICATE = "Y";
	String J_SECURITYTOKEN = "J_SECURITYTOKEN";
	String J_DIALOGNAME = "J_DIALOGNAME";
	String CHECKLIST = "checklist";
	String CHECKLISTLINES = "lines";
	String CHECKLISTCOLUMNS = "columns";
	String CHECKLINEVALUE = "val";
	String CHECKERRORROW = "line";
	String CHECKERRORCOL = "col";
	String CHECKERRORMESS = "errmess";
	String XMLROOT = "data";
	String POLYMER = "polymer";

	String APP_FILENAME = "app.properties";

	String JDATELINEQUERYID = "JDATELINE_QUERYID";
	String JDATELINEQUERYFROM = "JDATELINE_FROM";
	String JDATELINEQEURYTO = "JDATELINE_TO";
	String JDATELINEQUERYLINEID = "JDATELINE_ID";
	String JDATELINEQUERYLIST = "JDATELINE_QUERYLIST";
	String JDATELINELINEDEF = "linedef";
	String JDATELINEVALUES = "values";
	String JDATELINESPAN = "colspan";
	String JDATELINEHINT = "hint";

	String JDATELINELINEID = "JDATELINE_LINE";
	String JDATELINEDATEID = "JDATELINE_DATE";
	String JDATELINE_CELLACTION = "datelineaction";

	String JDATEACTIONGETVALUES = "datelinevalues";

	int CHART_DEFAULTWIDTH = 400;
	int CHART_DEFAULTHEIGHT = 240;
	String CHARTHEIGHT = "height";
	String CHARTPIENOT3D = "notpie3d";
	String CHARTTYPE = "charttype";
	String CHARTLIST = "chart";

	String SECURITYTOKEN = "SECURITY_TOKEN";

	String LOGINPAGE = "LoginPages";
	String SHIROREALM = "Shirorealm";

	String GWT_LOCALE = "GWT_LOCALE";

	String UPLOADFILEERROR = "UPLOAD_FILE_ERROR";

	String BLOBUPLOAD_REALM = "BLOBUPLOAD_REALM";
	String BLOBUPLOAD_KEY = "BLOBUPLOAD_GEN_KEY";
	String BLOBDOWNLOADPARAM = "param";

	String DOWNLOADSERVLET = "downLoadHandler";
	String UPLOADSERVLET = "upLoadHandler";

	String CRUD_ADD = "crud_add";
	String CRUD_CHANGE = "crud_change";

	char RELCHAR = '?';

	String DROPMENUID = "dropmenuid";
	String DROPDOWNCONTENT = "dropdown-content";

}
