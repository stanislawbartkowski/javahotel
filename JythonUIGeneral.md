#This page describes general idea behind MVP JythonUI framework



# Introduction #

The purpose is to create convenient implementation of MVP (Model/View/Presenter) http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter framework.
  * View. XML files defining content of the page (window, dialog). It provides default layout (simplistic although functional) and allows also defining more customized view (HTML panel layout).
  * Model. Framework written in GWT/Java (client side) and Server/Java. Takes data from the Presenter and display it using XML Views. Also picks up events from the View, gets current data and raises proper method in the Presenter.
  * Presenter. Resides at the server side. Acts as an interface between background repository (for instance database) and UI. Provides action for View (user) events and implements all business logic. Provides also response to the user event (data and actions).

# Development #

Development requires defining XML file (user interface contents and buttons) and Presenter implementation written in Jython. Jython is interpreted language and does not require recompiling. This way way the development of View and Presenter can be done without typical J2EE/Java development. On the other hand Jython the advantage of Jython is easy access to Java libraries so all richness of Java is available for Jython development.
Framework acts as a "middleware" between View and Presenter. Creates user interface using View (XML files) and fills it with the date taken from the presenter. After user action (for instance clicking the button) sends all data to the Presenter and raises proper method at the server side.
Current version support Jython only. It is planned in the future to allow also client customization (View enahancement by means of JavaScript code) and additional language for the server side (Presenter). For instances: C Python (by means of JNI) and Java itself.

# Details #

## Dialogs definition in xml file ##

Dialog xml file defines active elements in the user interface : fields entered, buttons, display elements. Framework creates default layout. Future version will allow custom layout in shape of html panel. One dialog can navigate to other dialog.

XSD file defining a dialog is available here:
https://code.google.com/p/javahotel/source/browse/trunk/JythonUI/jythonuiserver/resources/xsd/dialogschema.xsd

The general structure is as follows:
```

<?xml version="1.0"?>
<dialog xmlns="http://www.jythongwt.dialogxsd.com">

<before/>

<parent>
   { parent name}
</parent>

<displayname>
   { dialog name, title }
</displayname>

<types>
  { xml file with types definiton }
</types>

<jython>
  { jython backing method }
</jython>

<leftmenu>
  { definition of left menu }
</leftmenu>

<actions>
  { definition of actions available }
</actions>

<buttons>
  { definition of buttons available }
</buttons>

<form>
  { form definition }
</form>

<validaterules>
  { validate rule for form }
</validaterules>

<list>
 { definition of list }
</list>

<checklist>
 { definition of check list }
</checklist>

</dialog>

```

| Element | Content | Description | Multiplicity | Details |
|:--------|:--------|:------------|:-------------|:--------|
| before | Empty| If exists than before action is raised | 0..1 |  |

## Buttons ##

```
<buttons>
  <button> .. </button>
  ....
</buttons>
```

buttons tag encloses list of buttons. At least one button should be defined.

Attributes or nested elements in the button tag

| Tag | Mandatory | Default | Content | Description |
|:----|:----------|:--------|:--------|:------------|
| id | yes |  | Button identifier | Every button should have distinct id |
| displayname | No | Empty name | Button title |
| validate | No | No validate | Empty | If exists then clicking the button raises form validation. If not exists than validation is not raised (in case of Resing button) |
| actiontype | No |  |  |
| actionparam | No |  |  |
| actionparam1 | No |  |  |
| actionparam2 | No |  |  |
| hidden | No | Not hidden | Empty | If exists then button is not visible and not clickable |
| readonly | No | Active | Empty | if exists then button is visible but not clickable |


## Actions ##

### Action list ###

| Action name | Parameter | Parameter 1 | Parameter 2 | Description |
|:------------|:----------|:------------|:------------|:------------|
| JUP\_DIALOG | Name of the XML file with the dialog  |  |  | Displays dialog as popup, modal dialog |
| JMAIN\_DIALOG | Name of the XML file with the dialog |  |  | Displays dialog in main window, removes previous main dialog |
| JOK\_MESSAGE | Window content | Window title |  | Displays simple OK message |
| JERROR\_MESSAGE | Window content | Window title |  | Displays simple OK message but window is decorated as "error" message |
| JYESNO\_MESSAGE | Window content | Window title | Action identifier. Action receives information is user action was yes/no. Boolean variable JYESANSWER holds true/false value | Displays Yes/No message windows |
| JCLOSE\_DIALOG |  |  |  | Opposite to JUP\_DIALOG. Closes modal dialog and returns to upper dialog |

### Action in Jython code ###

```
  var[{Action name}] = True
  var['actionparam'] = {param value}
  var['actionparam1'] = {param1 value}  
  var['actionparam2'] = {param2 value}
```

Example:
```
  var['JOK_MESSSAGE'] = True
  var['actionparam'] = 'Hello message'
  var['actionparam1'] = 'Hello title'
```

### Action in XML definition ###

```xml

<button id={id} displayname={display name}  actiontype={action} actionparam={value of param}  actionparam1={value of param1} actionparam2={value of param2} />
```

Example: Display new main windows after clicking button "list"

```xml

<button id="list" displayname="List" actiontype="JMAIN_DIALOG" actionparam="list.xml"/>
```

Jython equivalence:
```
  var['JMAIN_DIALOG'] = True
  var['actionparam'] = 'list.xml'
```

### Sample related to JYESNO\_MESSAGE ###

XML file:
```xml

<dialog>
...
<buttons>
<button id="clearPersons" displayname="Clear all" actiontype="JYESNO_MESSAGE" actionparam="Do you really want to remove all persons now ?" actionparam1="Warning before removing" actionparam2="clearpersons" />


Unknown end tag for &lt;/buttons&gt;


...
<dialog>
```

Jython code:
```python


def dialogaction(action,var) :

if action == "clearpersons" :
yes = var['JYESANSWER']
if yes : do_seomethin_if_yes_answer
else : do_somenthing_if_no_answer

```

## CRUD actions ##

### List of actions ###

| Action name | Description |
|:------------|:------------|
| before | General action, should set list of items |
| crud\_readlist | Call after every crud action, should retrieve list of items, similar to before. But 'before' is called only once at the beginning. |
| crud\_add | Called when user clicked "Accept" in the "Add item" dialog. |
| crud\_remove | Called when user clicked "Accept" in the "Remove item" window |
| crud\_change | Called when user clicked "Accept" in the "Modif item" window |

### Before and crud\_readlist ###
"Before" is called once at the beginning to populate list for the first time. "crud\_readlist" is called any time when list requires refreshing after adding, removing or changing an item.

Example
```python


def __create_list(op, var) :
seq = op.getAllPersons()
list = []

for s in seq :
elem = {}
elem["key"] = s.id
elem["pnumber"] = s.getPersonNumb()
elem["pname"] = s.getPersonName()
list.append(elem)

map={}
map["list"] = list
var["JLIST_MAP"] = map

def dialogaction(action,var) :

op = ServiceInjector.constructPersonOp()

if action == "before" or action == "crud_readlist" :
__create_list(op,var)
return
```

### crud\_add, crud\_change and crud\_remove ###

These action are called after user clicked "Accept" in appropriate window. These action should validate data and/or perform any background logic related to the action.
There are the following options available:
  1. Invalid data. Data entered are invalid and action should return error message.
  1. Data is valid but additional confirmation is required before completing the task
  1. Data is valid and not confirmation is required. Action should be performed and list refreshing is necessary.
  1. User answered "Yes" after conformation. Action should be performed and list refreshing is necessary.

### Invalid data ###
Action should not set "JCLOSE\_DIALOG" variable. Instead appropriate "JERROR\_field" variable should be set with proper message text to display.
Example, assuming that pnumber field is invalid.
```python


if action == "crud_add" :
if duplicated :
var["JERROR_pnumber"] = "Duplicated number, person number        should be unique"
return
```

### Confirmation is required ###
If additional confirmation is required before completing the action the two variables should be set.
  1. JYESNO\_MSSSAGE : should contain content of the yes/no window
  1. JMESSAGE\_TITLE : the title of the yes/no window

Example:
```python

if action == "crud_add" and not var["JCRUD_AFTERCONF"] :
var["JYESNO_MESSAGE"] = "Are you ready to add new elem ?"
var["JMESSAGE_TITLE"] = "Ask for something"
return
```

### User answered "yes" after confirmation ###
Action is called and "JCRUD\_AFTERCONF" variable is set to True. Action should performed necessary logic and set "JCLOSE\_DIALOG" at the end.

```python

if action == "crud_add" and var["JCRUD_AFTERCONF"] :
.... do adding
var["JCLOSE_DIALOG"] = True
return
```

### Data is valid and no confirmation is required ###
As above but no JCRUD\_AFTERCONF checking is required. If conformation is not used then "JCRUD\_AFTERCONF" variable is always set to False. Action should set "JCLOSE\_DIALOG" variable to True.

## Move data from Jython to GUI ##

### Fields values to form ###

```python

var['{name of the field}'] = {value}
var["JCOPY_{name of the field}] = True
```

The first line set the new value and the second causes that this value is moved to the GUI form. Important: 'var' map is used to transport data from GUI to Jython and vice versa. As default the 'JCOPY_'+{name} is not set to True. One has to explicitly point which field should be refreshed after returning 'var' map to GUI.
The following value types are supported:_| Type | Example |
|:-----|:--------|
| String | var['name'] = 'Abraham Lincol' |
| Boolean | var['ok'] = True |
| int value | var['position'] = 567 |
| long value | var['id'] = 345k54321122L |
| date | import datetime; var['start\_from'] =  datetime.date(2001,11,5); |
| datetime | import datetime; var['modif\_time'] = datetime.datetime(2017,01,13,20,45,14); |

### Lists to form ###
It is possible to move more then one list to the form.
```python

map={}
map[{list name in the form}] = {sequence}
var["JLIST_MAP"] = map
```

'JLIST\_MAP' contains map of lists. Key in the list is the name of the list in GUI form and the value is the sequence having list content.
Examples:
```python

list = []

for i in range(100) :
elem = {}
elem["key"] = i
elem["pnumber"] = str(i)
list.append(elem)

map={}
map["list"] = list
var["JLIST_MAP"] = map
```

## Custom (reusable) types with helper and enums ##

### XML file with the definition ###

```
<?xml version="1.0"?>
<typedefs xmlns="http://www.jythongwt.typedefxsd.com">

<!-- common jython definition -->
     <jython>
       <import>from {package} import {module}</import>
       <method>{module}.{method}({0},{1})
      </jython>    

<!-- more the one typedef -->
  <typedef id="{type identifier}" type="combo" comboid="{id}" displayname = "{name}">
  

<!-- jython definition, overwrites common -->

     <jython>
        <method>{module}.{method}({0},{1})</method>
      </jython>    
 
   </typedef>

  <typedef id="{type identifier}" type="helper" comboid="{id}" displayname = "{name}">
  

<!-- jython definition, overwrites common -->

     <jython>
        <method>{module}.{method}({0},{1})</method>
      </jython>    

      <columns>
        <column id="{id1}" displayname="display name 1" />
        <column id="{id2}" displayname="display name 2" />
        .............
      </columns>  
 
   </typedef>

</typedefs>
```

Example:
```
<?xml version="1.0"?>
<typedefs xmlns="http://www.jythongwt.typedefxsd.com">

     <jython>
       <import>from testpack import packenum</import>
      </jython>    

  <typedef id="tenum" type="combo" comboid="id" displayname = "name">
  
     <jython>
        <method>packenum.dialogaction({0},{1})</method>
      </jython>    
 
   </typedef>

  <typedef id="tehelper" type="helper" comboid="id" displayname = "List of ids">
  
     <jython>
        <method>packenum.helperaction({0},{1})</method>
      </jython>
      
      <columns>
        <column id="id" displayname="Id" />
        <column id="name" displayname="Name" />
      </columns>  
 
   </typedef>
  
</typedefs>

```

'enum' define standard combo field, 'helper' define list with the values to select. After clicking on the helper image a list is displayed allowing choosing the value. The 'columns' tag defines list of columns visible. 'comboid' attribute define the 'id' of the column which is copied to the form field. The type name is 'tehelper'

Explanation:
First 'typedef' defines 'combo' type. The type name is 'teenum'. Jython code providing values is called as:
```python

from testpack import packenum
packenum.dialogaction(action,var)
```
The second defines selection helper. The type name is 'tehelper'. After clicking on the helper image a list with 'id' and 'name' columns. The 'columns' tag defines list of columns visible. 'comboid' attribute define the 'id' of the column which is copied to the form field. The type name is 'tehelper'
```python

from testpack import packenum
packenum.helperaction(action,var)
```

### Usage ###
```
<dialog xmlns="http://www.jythongwt.dialogxsd.com">

<!-- XML file name with typed definition used -->
 <types>{file name}</types>
 
 <form>
<!-- type name should be preceded with 'custom:' text -->
   <field id="combof" type="custom:{type id}" signalchange="" />
   
 </form>  

</dialog>

```

In the XML dialog file using the custom type a 'types' tag should a file name with custom types definition. In the 'form' definition a field should be preceded with 'custom:' name.

Example:
```
<code language="xml">
<dialog xmlns="http://www.jythongwt.dialogxsd.com">

<displayname>Combo Dialog</displayname>

 <before/>

 <types>typesenum.xml</types>
 
  <jython>
   <import>from testpack import packenum</import>
   <method>packenum.dialogaction({0},{1})</method>
 </jython>  
 
 <buttons>
  <button id="testcombo" displayname="Test" />
</buttons>
 
 
 <form>
   <field id="combof" type="custom:tenum" signalchange="" />
   
   <field id="comboenum" type="custom:tehelper" />
   
   <field id="outcombof" readonly="" />
 </form>  


</dialog>
</code>
```

## Helper assigned to one dialog ##

### XML dialog file ###

It is possible to add helper to a field in the dialog without defining it as separate custom type. It is enabled by 'helper' attribute. The value of this attribute is a file name of the helper dialog. Dialog is displayed as modal windows. In order to rewrite the new value to the field the help dialog should be closed having var'JCOPY_{field}' set to True and var[{fiel id}] having new value._

```
<?xml version="1.0"?>
<dialog xmlns="http://www.jythongwt.dialogxsd.com">

.......
 
 <form>
<!-- The 'helper' attribute enables helper for this field -->
   <field id={field id} displayname={display} helper={helper dialog} />
/>
 </form>  

....
</dialog>
```

Example:
The dialog below has two fields with helpers. One helper is defined by 'helper.xml' file and the second by 'helplist.xml".


```
<?xml version="1.0"?>
<dialog xmlns="http://www.jythongwt.dialogxsd.com">

<displayname>Combo Dialog</displayname>

 <before/>
 
  <jython>
   <import>from testpack import helperpa</import>
   <method>helperpa.dialogaction({0},{1})</method>
 </jython>  
 
 <buttons>
  <button id="testcombo" displayname="Test" />
</buttons>
 
 
 <form>
   <field id="helpid" displayname="Help" helper="helper.xml" />
   <field id="helplist" displayname="Help list" helper="helperlist.xml" />
 </form>  


</dialog>
```

### Helper XML dialog file ###
The helper dialog is exactly like any other dialog. The only difference is in the case of "positive" exit meaning that new value should be set to the helper field.
```python

if action == "copy" :
var[{field with helper}] = {new value}
var['JCOPY_'{field id with helper}] = True
var["JCLOSE_DIALOG"] = True
```

Example:
helper.xml file
```
<?xml version="1.0"?>
<dialog xmlns="http://www.jythongwt.dialogxsd.com">
<displayname>Person</displayname>

  <jython>
   <import>from testpack import helperpa</import>
   <method>helperpa.dialogaction({0},{1})</method>
 </jython>  

<buttons>
  <button id="copy" displayname="Copy" />
</buttons>

<form>
   <field id="fieldid" />
 </form>  
 

</dialog>
```

Jython code:
```python

def dialogaction(action,var) :

print "helperpa",action
for k in var.keys() :
print k, var[k]


if action == "copy" :
var['helpid'] = var['fieldid']
var['JCOPY_helpid'] = True
var["JCLOSE_DIALOG"] = True

```

## Validation ##
The validation can be performed in Jython backup code. But some simple and most common validation can be checked immediately at the server side.
  * Field is not empty.
  * Simple relationship between two field: less then, less or equal etc. For instance: test the beginning date is not greater then the ending date.
Automatic client side validation is triggered in two scenarios:
  * In dialog being the part of CRUD list, after clicking the "Accept" button.
  * In others dialog windows only if button ha"validate" attribute. This way the validation is not performed when the user clicks "Cancel" button.
### Validation "not empty" ###
It is enabled by "notempty" attribute. Example:
```
<dialog>
 
<form>
  <field id={id} ... notempty="" />

...
</dialog>
```
This client checks if a field is not empty and displays error message if it is not.
### Relationships between fields ###
This validation allows perform some tests testing relationship between two fields. In order to perform the validation both fields should be nonempty. Otherwise the comparison is not executed and assumed to be "valid".
The validation is performed at the client side. If it fails then server action is not raised.
```
<dialog>

<forms>
...
<forms>

<validaterules>
    <validate id={id1} op={op} id1={id2} displayname={Error text if not valid} />
.....
</validaterules>

</dialog>
```

The following comparison operators are recognized.
| Operator | Meaning |
|:---------|:--------|
| eq | Both fields should be equal |
| le | The first field should be less or equal the second |
| lt | The first field should be less then the second |
| gt | The first field should be greater the the second |
| ge | The first field should be greater or equal the second |

Example. If both fields are not empty then the first date cannot be later then the second.
```
<dialog>
...
 <form>
...
   <field id="begintime" displayname="Begin time" type="datetime" />
   <field id="endtime" displayname="End time" type="datetime" />
..   
 </form>  
 
  <validaterules> 
   <validate id="begintime" op="le" id1="endtime" displayname="Begin time cannot be later then the end time !" />
 </validaterules>

```

## Reading list in pages (chunks) ##

As a default all list is transported to the client side and cached there. But it is not possible for very big list (although it is difficult to say what "big" means here).
From user point of view the only difference between list moved to the browser in one piece and list moved in chunks is that there is a delay between while moving from one page to the next page.
Unfortunately, handling the chunked lists in the Jython support code is more complicated. Not only reading list in pages should be supported but also sorting and filtering.

### XML definition ###
'chunked' attribute defines chunked list.

Example:
```
 <list id="list" displayname="List of chunks" chunked="" elemformat="elemchunk.xml">
```

### Jython handling of chunked list ###

| Action | Description |
|:-------|:------------|
| before | Raised before displaying anything. 'list' property values should contain number of all elements in the list (instead of the list content |
| listgetsize | Similar like 'before' but it is raised any time when number of displayed elements could be changed. E.g after changing the order and filter |
| readchunk | Should provide the part of the list 'from' - 'number of lines . Should take into account also filtering and sorting |

Map variables:
| Jython map variable | Type | Description |
|:--------------------|:-----|:------------|
| JLIST\_FROM | Integer | The row number of the first line (starting from 0) |
| JLIST\_LENGTH | Integer | The number of lines to be retrieved |
| JLIST\_SORTLIST | Dependent on the column | If None then list is not sorted. If not None then the column name being sorted |
| JLIST\_SORTASC | Boolean | Is meaningful only if JLIST\_SORTLINE is not None. True means that the sort order is ascending (the top line is the smallest). False means that the sort order is descending (the top line is the greatest) |
| JSEARCH\_SET{field\_id} | Boolean | If True then filter is applied to this field |
| JSEARCH\_FROM{field\_id} | Dependent on the field type | Is meaningful only if JSEARCH\_SET{field\_id} is True. The value of 'from' field in the filter definition |
| JSEARCH\_TO{field\_id} |  Dependent on the field type | Is meaningful only ifJSEARCH\_SET{field\_id} is True. The value of 'to' field in the filter definition |
| JSEARCH\_EQ{field\_id} | Boolean | Is meaningful only if JSEARCH\_SET{field\_id} is True. The value of 'equal' field in the filter definition |


Sample:
http://code.google.com/p/javahotel/source/browse/trunk/Sample/WebApp/samplewebsrc/resources/packages/testpack/listchunk.py

## Check list (grid) ##

### Information ###
Allows to create two dimensional matrix with lines and columns and check/uncheck elements. Matrix definition (lines and columns) is created dynamically.

### XML definition ###
```
 <checklist id="<id>" displayname="<title>">
 </checklist> 
```

"id" should be unique.  It is possible to have more then one check list in the whole dialog. A dialog containing "check list" element should execute "before" action to get the definition of lines and columns for check matrix

Example:
```
 <checklist id="check" displayname="Permissions">
 </checklist> 
```

### Jython handling ###

Values related to check list are stored in var["JCHECK\_MAP"]. Then all values related to particular check list are stored in var["JCHECK\_MAP"][<check list id>]

Jython code is raised in three different context
  * Defining of lines and columns
  * Setting values to the elements
  * Reading the current setting

#### Defining the lines and columns ####

Definition of lines and column should be provided in "before" action. var["JCHECK\_MAP"][<check list id>]["lines"] contains lines (rows) definition and var["JCHECK\_MAP"][<check list id>]["columns"] contains columns definition.
Definition of lines and columns is a list (sequence) of maps:

| Key | Decription |
|:----|:-----------|
| id | Lines/columns identifier. Used for setting and getting values |
| displayname | Name/text displayed on the screen |

Example
```python


checkmap = {}

seq = []
m = {}
m["id"] = "hotel1"
m["displayname"] = "Hotel One"
seq.append(m)
m = {}
m["id"] = "hotel2"
m["displayname"] = "Hotel Two"
seq.append(m)
checkmap["lines"] = seq

li = [["man","Manager"],["acc","Accountant"],["adm","Administrator"],["rec","Receptionist"]]

seq = []
for l in li :
m = {}
m["id"] = l[0]
m["displayname"] = l[1]
seq.append(m)
checkmap["columns"] = seq

map = {}
map["check"] = checkmap
var["JCHECK_MAP"] = map

```

### Setting values to the check list elements ###

Setting values can be done in "before" action (together with lines and column definition) or in any other action. All values related to a single line are stored in sequence var["JCHECK\_MAP"][<check list id>][<line id>]. An element in a sequence is a map
| Key | Description |
|:----|:------------|
| "id" | Column id |
| "val" | Boolean value. True means that element is checked and False that element is unchecked |

Values for every line is stored as a sequence of of values for every column. For instance:
Lines: "hotel1", "hotel2"
Columns: "man", "acc", "adm", "rec"

Key value: var["JCHECK\_MAP"]["check"]["hotel1"] contains sequence of values: {{"id" : "man","val" : true },{"id" : "acc","val" : false }}
It defines that element ("hotel1","man") is checked and element("hotel1","acc") is unchecked.

Important: values for element not included are regarded as having value "false" (unchecked).

### Getting values ###

If dialog definition contains checklist clause that every Jython action contains list of values for this check list. The values are available in data structure described above. The same data structure is used for setting and getting values.

For instance: var["JCHECK\_MAP"]["check"]["hotel1"] contains list of values for line "hotel1".

Important: Unlike other elements values for check list are set automatically without specifying anything like "JCOPY_". In order to avoid values setting one shoud remove var["JCHECK\_MAP"] from 'var' variable._

Examples:
https://code.google.com/p/javahotel/source/browse/trunk/Sample/WebApp/samplewebsrc/resources/packages/testpack/checklist.py


# Security #

## app.properties ##
File app.properties contains general information about an application, including security specification. The following properties are available.


| Property | Value | Example |
|:---------|:------|:--------|
| Title | Title of the browser window | Example of Jython UI |
| ProductName | Product name, information displayed in the status bar | Jython UI demo |
| OnwerName | Owner name, information display in the status bar | Demo |
|Version | Version, information displayed in the upper left corner of the status bar | Version 1.0 Date: 16/04/2013 |
| Authenticated | Y/N (default N). If Y then non-authorized access is forbidden. All access requires authentication token |  |
| Login | List of starting pages (, as deliminator) requiring authentication | start.xml |
| Shirorealm | Name of the shiro realm used for authentication | classpath:resources/shiro/shiro.ini |

Sample of app.properties.

https://code.google.com/p/javahotel/source/browse/trunk/Sample/WebApp/samplewebsrc/app.properties This example does not put strict security access. Entrance via page authenticate.xml requires authentication (login name/password).

https://code.google.com/p/javahotel/source/browse/trunk/examples/DB2TaskScheduler/src/app.properties  No security at all.

Additional info.
  * Authenticate=Y put strictly restricted access. Any access requires being authenticated.
  * Login. Defines list of start pages forcing authentication. If Autenticate=N and the page is not on Login list then this page will not be available at all

## Shiro realm ##
As authentication and authorization engine Shiro is used. More information about shiro : http://shiro.apache.org/configuration.html.
Only standalone (not Web) API is used. The following Shiro methods are used.
| Action | JavaDoc | Method used |
|:-------|:--------|:------------|
| Realm configuration | http://shiro.apache.org/static/1.2.1/apidocs/org/apache/shiro/config/IniSecurityManagerFactory.html | IniSecurityManagerFactory(String iniResourcePath) |
| Authentication (login) | http://shiro.apache.org/static/1.2.1/apidocs/org/apache/shiro/subject/Subject.html | Subject.login(AuthenticationToken token) |
| Authorization (roles) |  | Subject.hasRole(String roleIdentifier) |
| Authorization (permission) |  | Subject.isPermitted(String permission) |

## Authorization, Jexl and permission string ##

After the user is authenticated it is possible to restrict his/her access to some resources. The access can be limited by using user name, role (group of users) and permission. The 'Jexl' boolean expression (evaluated to True/False) is used to validate access.
Jexl - http://commons.apache.org/proper/commons-jexl/.
The following functions can be used for creating access expression.
| Method name | Description | Example |
|:------------|:------------|:--------|
| sec.u("string") | Returns True if current user name is the same as the parameter | sec.u('darkhelmet') |
| sec.r("string") | Returns True if current user has the role defined by the parameter | sec.r('mightyknight') |
| sec.p("string") | Returns True if current user has the permission define by the parameter | sec.p('lightsaber:weild') |

Using methods described above it is possible to create an Jexl boolean expression which evaluates to True/False of current user.

Example
  * Give access to the user 'darkhelmet' only. sec.u('darkhelmet')
  * Give access to the user 'darkhelmet' and all belonging to 'schwartz' role. sec.u('darkhelmet') and sec.r('schwartz')

## Authorization, static way ##
Hides or makes read only access to some fields in dialog forms, columns in the list or buttons.
Access expression can be attached to two attributes: 'hidden' and 'readonly'. The format is as follows:
  * hidden="" or readonly="" (empty value). Evaluates to 'True' for all users.
  * hidden="$<Jexl expression>" or readonly="$<Jexl expression>". Is True only if Jexl access expression evaluates to True for current user.

Example:
Editable only for 'darkhelmet' user. For 'schwartz' role the field is readonly. Hidden for the rest of the world.
**hidden="$!sec.u('darkhelmet') and !sec.r('schwartz')"** readonly="$!sec.u(darkhelmet') and sec.r('schwartz')"

For 'darkhelmet' user neither hidden nor readonly evaluates to True. So the field is visible and editable.
For 'schwartz' role 'hidden' evaluates to False. 'readonly' evaluates to True. So the field is visible but not editable.
For outside world 'hidden' evaluates to True. So the field is not visible ('readonly' attribute is not important in this case).

## Authorization, programmatic way ##
It is also possible to verify authorization from 'jython' code.

Example:

```python

from guice import ServiceInjector

def dialogaction(action,var) :
iSec = ServiceInjector.constructSecurity()
token = var["SECURITY_TOKEN"]
ok = iSec.isAuthorized(token,"sec.u('darkhelmet')")
var["OK"] = ok
```

  * ServiceInjector.constructSecurity() returns handler for security class.
  * var["SECURITY\_TOKEN"] returns security token for current user. This token is used as a parameter to security method.
  * isAuthorized(token,"string") is used to validate access for current user. This method returns True/False for authorization string being the parameter.