/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

    String CELL_COLUMN_WEEKEND = "weekend-cell-column";
    String HEADER_WEEKEND = "header-date-weekend";
    String CELL_COLUMN_TODAY = "today-cell-column";
    String HEADER_TODAY = "header_date_today";

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
    String HTMLTYPE = "html";
    String JSCODE = "jscode";
    String CSSCODE = "csscode";
    String BEFORE = "before";
    String LIST = "list";
    String COLUMN = "column";
    String COLUMNS = "columns";
    String LISTBUTTONSLIST = "buttons-addlist";
    String LISTBUTTONSVALIDATE = "buttons-validate";
    String JROWCOPY = "JROWCOPY_";
    String JCOPY = "JCOPY_";
    String JSTATUSSET = "JSTATUS_SET_";
    String JSTATUSTEXT = "JSTATUS_TEXT_";
    String JCOOKIE = "JCOOKIE_";
    String JCOOKIESET = "JCOOKIESET_";
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
    String AFTERDOT = "afterdot";
    String DEFVALUE = "defvalue";
    String COLNO = "colno";
    String FOOTER = "footer";
    String FOOTERTYPE = "footertype";
    String FOOTERALIGN = "footeralign";
    String FOOTERAFTERDOT = "footerafterdot";
    String IMAGECOLUMN = "imagecolumn";
    String IMAGELIST = "imagelist";
    String LABEL = "label";
    // String IJSCALL = "JS.";
    String ASXML = "asxml";
    String FORMPANEL = "formpanel";
    String AUTOHIDE = "autohide";
    String MODELESS = "modeless";
    String CLEARLEFT = "clearleft";
    String JSMODIFROW = "jsmodifrow";
    String CLEARCENTRE = "clearcentre";
    String JXMLCONTENT = "JXMLCONTENT";
    String JXMLSETCONTENT = "JXMLCONTENTSET";
    int DEFAULTCOLNO = 14;
    int DEFAULTROWNO = 20;
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
    int DEFAULTAFTERDOT = 2;
    String JLISTMAP = "JLIST_MAP";
    String JCHECKLISTMAP = "JCHECK_MAP";
    String JDATELINEMAP = "JDATELINE_MAP";
    String ELEMFORMAT = "elemformat";
    String NOTEMPTY = "notempty";
    String HIDDEN = "hidden";
    String READONLY = "readonly";
    String ENABLE = "enable";
    String READONLYADD = "readonlyadd";
    String READONLYCHANGE = "readonlychange";
    String CRUD_ADD = "crud_add";
    String CRUD_REMOVE = "crud_remove";
    String CRUD_CHANGE = "crud_change";
    String CRUD_SHOW = "crud_show";
    String CRUD_READLIST = "crud_readlist";
    String JLIST_NAME = "JLIST_NAME";
    String PARENT = "parent";
    String JERROR = "JERROR";
    String JMAINDIALOG = "JMAIN_DIALOG";
    String ACTIONTYPE = "actiontype";
    String ACTIONPARAM = "actionparam";
    String ACTIONPARAM1 = "actionparam1";
    String ACTIONPARAM2 = "actionparam2";
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
    String JOKMESSAGE = "JOK_MESSAGE";
    String JYESNOMESSAGE = "JYESNO_MESSAGE";
    String JYESANSWER = "JYESANSWER";
    String JERRORMESSAGE = "JERROR_MESSAGE";
    String JMESSAGE_TITLE = "JMESSAGE_TITLE";
    String JAFTERDIALOGACTION = "JAFTERDIALOG_ACTION";
    String JBUTTONRES = "JUPDIALOG_BUTTON";
    String CLOSEDIALOGIMAGE = "closeimage";
    String JBUTTONDIALOGRES = "JUPDIALOG_RES";
    String JBUTTONDIALOGSTART = "JUPDIALOG_START";
    String JCRUD_AFTERCONF = "JCRUD_AFTERCONF";
    String JLOGOUTACTION = "JLOGOUT_ACTION";
    String JFOOTER = "JFOOTER_";
    String JFOOTERCOPY = "JFOOTER_COPY_";
    String JLISTEDIT = "JLIST_EDIT_";
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
    String JCHANGESIGNALBEFORE = "JLIST_EDIT_BEFORE";
    String JVALBEFORE = "JVALBEFORE";
    String SIGNALCHANGEFIELD = "changefield";
    String SIGNALAFTERFOCUS = "changeafterfocus";
    String SIGNALCOLUMNCHANGE = "columnchangeaction";
    String SIGNALAFTERROW = "aftereditrow";
    String SIGNALBEFOREROW = "beforerow";
    String HELPER = "helper";
    String LINESET = "_lineset";
    String STANDBUTT = "standbutt";
    String BUTT_TOOLS = "TOOLS";
    String BUTT_ADD = "ADD";
    String BUTT_MODIF = "MODIF";
    String BUTT_REMOVE = "REMOVE";
    String BUTT_SHOW = "SHOW";
    String BUTT_FIND = "FIND";
    String BUTT_FILTER = "FILTER";
    String JCRUD_DIALOG = "JCRUD_DIALOG";
    String HELPERREFRESH = "helperrefresh";
    String COMBOTYPE = "combo";
    String TEXTAREA = "textarea";
    String RICHTEXT = "richtext";
    String FROM = "from";
    String VALIDATE = "validate";
    String VALIDATERULES = "validaterules";
    String VALIDATEOP = "op";
    String VALIDATEID1 = "id1";
    String WIDTH = "width";
    String EDITCLASS = "editclass";
    String EDITCSS = "editcss";
    String COLUMNCLASS = "columnclass";
    String HEADERCLASS = "headerclass";
    String DATEPANELCLASSDEFAULT = "datepanel";
    String CLASSNAME = "cssclass";
    String PAGESIZE = "pagesize";
    String ALIGN = "align";
    String ALIGNL = "L";
    String ALIGNR = "R";
    String ALIGNC = "C";
    String CHUNKED = "chunked";
    String JLISTSIZE = "JLIST_SIZE";
    String JLIST_READCHUNK = "readchunk";
    String JLIST_READCHUNKSTART = "JLIST_FROM";
    String JLIST_READCHUNKLENGTH = "JLIST_LENGTH";
    String JLIST_SORTLIST = "JLIST_SORTLIST";
    String JLIST_SORTASC = "JLIST_SORTASC";
    String JLIST_GETSIZE = "listgetsize";
    String JSEARCH_FROM = "JSEARCH_FROM_";
    String JSEARCH_TO = "JSEARCH_TO_";
    String JSEARCH_EQ = "JSEARCH_EQ_";
    String JFILTR_SEARCH = "JSEARCH_FILTR";
    String JSEARCH_SET = "JSEARCH_SET_";
    String JSEARCH_LIST_SET = "JSEARCH_LIST_SET_";
    String PASSWORD = "password";
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

    String APP_TITLE = "Title";
    String APP_PRODUCTNAME = "ProductName";
    String APP_OWNERNAME = "OwnerName";
    String APP_VERSION = "Version";
    String APP_FILENAME = "app.properties";
    String APP_PRODUCTIMAGE = "ProductImage";

    String JDATELINEQUERYID = "JDATELINE_QUERYID";
    String JDATELINEQUERYFROM = "JDATELINE_FROM";
    String JDATELINEQEURYTO = "JDATELINE_TO";
    String JDATELINEQUERYLINEID = "JDATELINE_ID";
    String JDATELINEQUERYLIST = "JDATELINE_QUERYLIST";
    String JDATELINELINEDEF = "linedef";
    String JDATELINEVALUES = "values";
    String JDATELINESPAN = "colspan";
    String JDATELINE_GOTODATE = "JDATELINE_GOTO_";

    String JDATELINELINEID = "JDATELINE_LINE";
    String JDATELINEDATEID = "JDATELINE_DATE";
    String JDATELINE_CELLACTION = "datelineaction";

    String JDATEACTIONGETVALUES = "datelinevalues";

    char PERMSIGN = '$';

    String SECURITYTOKEN = "SECURITY_TOKEN";

    String LOGINPAGE = "LoginPages";
    String LOGINDELIMITER = ",";
    String STARTPAGEQUERY = "start";
    String SHIROREALM = "Shirorealm";
    String STARTPAGES = "StartPages";
    String STARTPAGE = "Start";

    String JSETATTRCHECK = "JSETATTR_CHECKLIST_";
    String JVALATTRCHECK = "JVALATTR_CHECKLIST_";
    String JSETATTRBUTTON = "JSETATTR_BUTTON_";
    String JSETATTRFIELD = "JSETATTR_FIELD_";

    String JREFRESHDATELINE = "JREFRESH_DATELINE_";

    String GWT_LOCALE = "GWT_LOCALE";

    String UPLOADFILEERROR = "UPLOAD_FILE_ERROR";

    String BLOBUPLOAD_REALM = "BLOBUPLOAD_REALM";
    String BLOBUPLOAD_KEY = "BLOBUPLOAD_GEN_KEY";
    String BLOBDOWNLOADPARAM = "param";

    String DOWNLOADSERVLET = "downLoadHandler";
    String UPLOADSERVLET = "upLoadHandler";
}
