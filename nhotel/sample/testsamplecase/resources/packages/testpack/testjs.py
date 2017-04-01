import cutil,jsutil,con,toformat,miscutil

import datetime

# column {"list":[{"visible":true, "id":"id", "columnname":"storeKey"},{"visible":true, "id":"number0", "columnname":"Number 0"},{"visible":true, "id":"number1", "columnname":"Number 1"},{"visible":true, "id":"number2", "columnname":"Number 2"},{"visible":true, "id":"number3", "columnname":"Number 3"},{"visible":true, "id":"number4", "columnname":"Number 4"}]}

# data {"list":[{"id":1, "number0":123, "number1":333.7, "number2":null, "number3":null, "number4":null},{"id":2, "number0":78, "number1":4, "number2":null, "number3":1.987, "number4":3.4555}]}

# tdata {"list":[{"timef":"2017/01/14 20:45:14", "keyf":null},{"timef":"2017/01/15 20:45:14", "keyf":null},{"timef":"2017/01/16 20:45:14", "keyf":null},{"timef":"2017/01/17 20:45:14", "keyf":null},{"timef":"2017/01/18 20:45:14", "keyf":null},{"timef":"2017/01/19 20:45:14", "keyf":null},{"timef":"2017/01/20 20:45:14", "keyf":null},{"timef":"2017/01/21 20:45:14", "keyf":null},{"timef":"2017/01/22 20:45:14", "keyf":null},{"timef":"2017/01/23 20:45:14", "keyf":null},{"timef":"2017/01/24 20:45:14", "keyf":null},{"timef":"2017/01/25 20:45:14", "keyf":null},{"timef":"2017/01/26 20:45:14", "keyf":null},{"timef":"2017/01/27 20:45:14", "keyf":null},{"timef":"2017/01/28 20:45:14", "keyf":null},{"timef":"2017/01/29 20:45:14", "keyf":null},{"timef":"2017/01/30 20:45:14", "keyf":null},{"timef":"2017/01/31 20:45:14", "keyf":null},{"timef":"2017/02/01 20:45:14", "keyf":null},{"timef":"2017/02/02 20:45:14", "keyf":null},{"timef":"2017/02/03 20:45:14", "keyf":null},{"timef":"2017/02/04 20:45:14", "keyf":null},{"timef":"2017/02/05 20:45:14", "keyf":null},{"timef":"2017/02/06 20:45:14", "keyf":null},{"timef":"2017/02/07 20:45:14", "keyf":null},{"timef":"2017/02/08 20:45:14", "keyf":null},{"timef":"2017/02/09 20:45:14", "keyf":null},{"timef":"2017/02/10 20:45:14", "keyf":null},{"timef":"2017/02/11 20:45:14", "keyf":null},{"timef":"2017/02/12 20:45:14", "keyf":null},{"timef":"2017/02/13 20:45:14", "keyf":null},{"timef":"2017/02/14 20:45:14", "keyf":null},{"timef":"2017/02/15 20:45:14", "keyf":null},{"timef":"2017/02/16 20:45:14", "keyf":null},{"timef":"2017/02/17 20:45:14", "keyf":null},{"timef":"2017/02/18 20:45:14", "keyf":null},{"timef":"2017/02/19 20:45:14", "keyf":null},{"timef":"2017/02/20 20:45:14", "keyf":null},{"timef":"2017/02/21 20:45:14", "keyf":null},{"timef":"2017/02/22 20:45:14", "keyf":null},{"timef":"2017/02/23 20:45:14", "keyf":null},{"timef":"2017/02/24 20:45:14", "keyf":null},{"timef":"2017/02/25 20:45:14", "keyf":null},{"timef":"2017/02/26 20:45:14", "keyf":null},{"timef":"2017/02/27 20:45:14", "keyf":null},{"timef":"2017/02/28 20:45:14", "keyf":null},{"timef":"2017/03/01 20:45:14", "keyf":null},{"timef":"2017/03/02 20:45:14", "keyf":null},{"timef":"2017/03/03 20:45:14", "keyf":null},{"timef":"2017/03/04 20:45:14", "keyf":null},{"timef":"2017/03/05 20:45:14", "keyf":null},{"timef":"2017/03/06 20:45:14", "keyf":null},{"timef":"2017/03/07 20:45:14", "keyf":null},{"timef":"2017/03/08 20:45:14", "keyf":null},{"timef":"2017/03/09 20:45:14", "keyf":null},{"timef":"2017/03/10 20:45:14", "keyf":null},{"timef":"2017/03/11 20:45:14", "keyf":null},{"timef":"2017/03/12 20:45:14", "keyf":null},{"timef":"2017/03/13 20:45:14", "keyf":null},{"timef":"2017/03/14 20:45:14", "keyf":null},{"timef":"2017/03/15 20:45:14", "keyf":null},{"timef":"2017/03/16 20:45:14", "keyf":null},{"timef":"2017/03/17 20:45:14", "keyf":null},{"timef":"2017/03/18 20:45:14", "keyf":null},{"timef":"2017/03/19 20:45:14", "keyf":null},{"timef":"2017/03/20 20:45:14", "keyf":null},{"timef":"2017/03/21 20:45:14", "keyf":null},{"timef":"2017/03/22 20:45:14", "keyf":null},{"timef":"2017/03/23 20:45:14", "keyf":null},{"timef":"2017/03/24 20:45:14", "keyf":null},{"timef":"2017/03/25 20:45:14", "keyf":null},{"timef":"2017/03/26 21:45:14", "keyf":null},{"timef":"2017/03/27 21:45:14", "keyf":null},{"timef":"2017/03/28 21:45:14", "keyf":null},{"timef":"2017/03/29 21:45:14", "keyf":null},{"timef":"2017/03/30 21:45:14", "keyf":null},{"timef":"2017/03/31 21:45:14", "keyf":null},{"timef":"2017/04/01 20:45:14", "keyf":null},{"timef":"2017/04/02 20:45:14", "keyf":null},{"timef":"2017/04/03 20:45:14", "keyf":null},{"timef":"2017/04/04 20:45:14", "keyf":null},{"timef":"2017/04/05 20:45:14", "keyf":null},{"timef":"2017/04/06 20:45:14", "keyf":null},{"timef":"2017/04/07 20:45:14", "keyf":null},{"timef":"2017/04/08 20:45:14", "keyf":null},{"timef":"2017/04/09 20:45:14", "keyf":null},{"timef":"2017/04/10 20:45:14", "keyf":null},{"timef":"2017/04/11 20:45:14", "keyf":null},{"timef":"2017/04/12 20:45:14", "keyf":null},{"timef":"2017/04/13 20:45:14", "keyf":null},{"timef":"2017/04/14 20:45:14", "keyf":null},{"timef":"2017/04/15 20:45:14", "keyf":null},{"timef":"2017/04/16 20:45:14", "keyf":null},{"timef":"2017/04/17 20:45:14", "keyf":null},{"timef":"2017/04/18 20:45:14", "keyf":null},{"timef":"2017/04/19 20:45:14", "keyf":null},{"timef":"2017/04/20 20:45:14", "keyf":null},{"timef":"2017/04/21 20:45:14", "keyf":null},{"timef":"2017/04/22 20:45:14", "keyf":null},{"timef":"2017/04/23 20:45:14", "keyf":null}]}

FORMATS="{'list':[{'visible':true, 'id':'id', 'columnname':'Next num'},{'visible':true, 'id':'number0', 'columnname':'Number'},{'visible':true, 'id':'number1', 'columnname':'Number 1'},{'visible':true, 'id':'number2', 'columnname':'Number 2'},{'visible':true, 'id':'number3', 'columnname':'Number 3'},{'visible':true, 'id':'number4', 'columnname':'Number 4'}]}"
DATAS="{'list':[{'id':1, 'number0':123, 'number1':333.7, 'number2':null, 'number3':null, 'number4':null},{'id':2, 'number0':78, 'number1':4, 'number2':null, 'number3':1.987, 'number4':3.4555}]}"

CHECKFORMATS="{'list':[{'visible':false, 'id':'id', 'columnname':'Id'},{'visible':true, 'id':'check', 'columnname':'Select'},{'visible':true, 'id':'number', 'columnname':'Number'}]}"
CHECKDATAS="{'list':[{'id':1, 'check':false, 'number':0},{'id':2, 'check':false, 'number':1},{'id':3, 'check':false, 'number':2},{'id':4, 'check':false, 'number':3},{'id':5, 'check':false, 'number':4},{'id':6, 'check':false, 'number':5},{'id':7, 'check':false, 'number':6},{'id':8, 'check':false, 'number':7},{'id':9, 'check':false, 'number':8},{'id':10, 'check':false, 'number':9},{'id':11, 'check':false, 'number':10},{'id':12, 'check':false, 'number':11},{'id':13, 'check':false, 'number':12},{'id':14, 'check':false, 'number':13},{'id':15, 'check':false, 'number':14},{'id':16, 'check':false, 'number':15},{'id':17, 'check':false, 'number':16},{'id':18, 'check':false, 'number':17},{'id':19, 'check':false, 'number':18},{'id':20, 'check':false, 'number':19},{'id':21, 'check':false, 'number':20},{'id':22, 'check':false, 'number':21},{'id':23, 'check':false, 'number':22},{'id':24, 'check':false, 'number':23},{'id':25, 'check':false, 'number':24},{'id':26, 'check':false, 'number':25},{'id':27, 'check':false, 'number':26},{'id':28, 'check':false, 'number':27},{'id':29, 'check':false, 'number':28},{'id':30, 'check':false, 'number':29},{'id':31, 'check':false, 'number':30},{'id':32, 'check':false, 'number':31},{'id':33, 'check':false, 'number':32},{'id':34, 'check':false, 'number':33},{'id':35, 'check':false, 'number':34},{'id':36, 'check':false, 'number':35},{'id':37, 'check':false, 'number':36},{'id':38, 'check':false, 'number':37},{'id':39, 'check':false, 'number':38},{'id':40, 'check':false, 'number':39},{'id':41, 'check':false, 'number':40},{'id':42, 'check':false, 'number':41},{'id':43, 'check':false, 'number':42},{'id':44, 'check':false, 'number':43},{'id':45, 'check':false, 'number':44},{'id':46, 'check':false, 'number':45},{'id':47, 'check':false, 'number':46},{'id':48, 'check':false, 'number':47},{'id':49, 'check':false, 'number':48},{'id':50, 'check':false, 'number':49},{'id':51, 'check':false, 'number':50},{'id':52, 'check':false, 'number':51},{'id':53, 'check':false, 'number':52},{'id':54, 'check':false, 'number':53},{'id':55, 'check':false, 'number':54},{'id':56, 'check':false, 'number':55},{'id':57, 'check':false, 'number':56},{'id':58, 'check':false, 'number':57},{'id':59, 'check':false, 'number':58},{'id':60, 'check':false, 'number':59},{'id':61, 'check':false, 'number':60},{'id':62, 'check':false, 'number':61},{'id':63, 'check':false, 'number':62},{'id':64, 'check':false, 'number':63},{'id':65, 'check':false, 'number':64},{'id':66, 'check':false, 'number':65},{'id':67, 'check':false, 'number':66},{'id':68, 'check':false, 'number':67},{'id':69, 'check':false, 'number':68},{'id':70, 'check':false, 'number':69},{'id':71, 'check':false, 'number':70},{'id':72, 'check':false, 'number':71},{'id':73, 'check':false, 'number':72},{'id':74, 'check':false, 'number':73},{'id':75, 'check':false, 'number':74},{'id':76, 'check':false, 'number':75},{'id':77, 'check':false, 'number':76},{'id':78, 'check':false, 'number':77},{'id':79, 'check':false, 'number':78},{'id':80, 'check':false, 'number':79},{'id':81, 'check':false, 'number':80},{'id':82, 'check':false, 'number':81},{'id':83, 'check':false, 'number':82},{'id':84, 'check':false, 'number':83},{'id':85, 'check':false, 'number':84},{'id':86, 'check':false, 'number':85},{'id':87, 'check':false, 'number':86},{'id':88, 'check':false, 'number':87},{'id':89, 'check':false, 'number':88},{'id':90, 'check':false, 'number':89},{'id':91, 'check':false, 'number':90},{'id':92, 'check':false, 'number':91},{'id':93, 'check':false, 'number':92},{'id':94, 'check':false, 'number':93},{'id':95, 'check':false, 'number':94},{'id':96, 'check':false, 'number':95},{'id':97, 'check':false, 'number':96},{'id':98, 'check':false, 'number':97},{'id':99, 'check':false, 'number':98},{'id':100, 'check':false, 'number':99}]}"

def dialogaction(action,var):
    
    cutil.printVar('js',action,var)
    
    if action == 'test1' :
        s = "{'list':[{'visible':true, 'id':'id', 'columnname':'Next num'},{'visible':true, 'id':'number0', 'columnname':'Number'},{'visible':true, 'id':'number1', 'columnname':'Number 1'},{'visible':true, 'id':'number2', 'columnname':'Number 2'},{'visible':true, 'id':'number3', 'columnname':'Number 3'},{'visible':true, 'id':'number4', 'columnname':'Number 4'}]}"
        a = jsutil.toList(s,'list')
        print len(a)
        assert len(a) == 6
        for e in a :
            print e["visible"],e["id"],e["columnname"]
            assert e["visible"]
        var["OK"] = True
        
    if action == 'test2' :
        s = "{'list':[{'id':1, 'number0':123, 'number1':333.7, 'number2':null, 'number3':null, 'number4':null},{'id':2, 'number0':78, 'number1':4, 'number2':null, 'number3':1.987, 'number4':3.4555}]}"
        a = jsutil.toList(s,'list')
        print a
        print len(a)
        assert 2 == len(a)
        assert a[0][u'number3'] == None
        assert a[0][u'number1'] == 333.7
        var["OK"] = True
        
    if action == 'test3' :
        dlist = miscutil.toListMap("test107.xml","lista")
        assert dlist != None
        (dtype,afterdot) = miscutil.getColumnDescr(dlist,"id")
        print dtype,afterdot
        assert "long" == dtype
        assert 0 == afterdot
        (dtype,afterdot) = miscutil.getColumnDescr(dlist,"mon")
        print dtype,afterdot
        assert "decimal" == dtype
        assert 3 == afterdot
        (dtype,afterdot) = miscutil.getColumnDescr(dlist,"bol")
        print dtype,afterdot
        assert "boolean" == dtype
        assert 0 == afterdot
        (dtype,afterdot) = miscutil.getColumnDescr(dlist,"dat")
        print dtype,afterdot
        assert "date" == dtype
        assert 0 == afterdot
        var["OK"] = True
        
    if action == 'test4' :
        s = "{'list':[{'id':1, 'number0':123, 'number1':333.7, 'number2':null, 'number3':null, 'number4':null},{'id':2, 'number0':78, 'number1':4, 'number2':null, 'number3':1.987, 'number4':3.4555}]}"
        a = jsutil.toList(s,'list',"test107.xml","list1")
        print a
        assert 2 == len(a)
        print type(a[0]['id'])
        assert (type(a[0]['id']) == int)
        var["OK"] = True
        
    if action == "test5" :
        d = "2017/01/14"
        t = "2017/01/14 20:45:14"
        da = con.StoDate(d)
        print da 
        assert 2017 == da.year
        assert 1 == da.month
        assert 14 == da.day
        ta = con.StoDate(t,True)
        print ta
        assert 20 == ta.hour
        assert 45 == ta.minute
        assert 14 == ta.second
        var["OK"] = True
        
    if action == "test6" :
        s = "{'list':[{'timef':'2017/01/14 20:45:14', 'keyf':null},{'timef':'2017/01/15 20:45:14', 'keyf':null},{'timef':'2017/01/16 20:45:14', 'keyf':null},{'timef':'2017/01/17 20:45:14', 'keyf':null},{'timef':'2017/01/18 20:45:14', 'keyf':null},{'timef':'2017/01/19 20:45:14', 'keyf':null},{'timef':'2017/01/20 20:45:14', 'keyf':null},{'timef':'2017/01/21 20:45:14', 'keyf':null},{'timef':'2017/01/22 20:45:14', 'keyf':null},{'timef':'2017/01/23 20:45:14', 'keyf':null},{'timef':'2017/01/24 20:45:14', 'keyf':null},{'timef':'2017/01/25 20:45:14', 'keyf':null},{'timef':'2017/01/26 20:45:14', 'keyf':null},{'timef':'2017/01/27 20:45:14', 'keyf':null},{'timef':'2017/01/28 20:45:14', 'keyf':null},{'timef':'2017/01/29 20:45:14', 'keyf':null},{'timef':'2017/01/30 20:45:14', 'keyf':null},{'timef':'2017/01/31 20:45:14', 'keyf':null},{'timef':'2017/02/01 20:45:14', 'keyf':null},{'timef':'2017/02/02 20:45:14', 'keyf':null},{'timef':'2017/02/03 20:45:14', 'keyf':null},{'timef':'2017/02/04 20:45:14', 'keyf':null},{'timef':'2017/02/05 20:45:14', 'keyf':null},{'timef':'2017/02/06 20:45:14', 'keyf':null},{'timef':'2017/02/07 20:45:14', 'keyf':null},{'timef':'2017/02/08 20:45:14', 'keyf':null},{'timef':'2017/02/09 20:45:14', 'keyf':null},{'timef':'2017/02/10 20:45:14', 'keyf':null},{'timef':'2017/02/11 20:45:14', 'keyf':null},{'timef':'2017/02/12 20:45:14', 'keyf':null},{'timef':'2017/02/13 20:45:14', 'keyf':null},{'timef':'2017/02/14 20:45:14', 'keyf':null},{'timef':'2017/02/15 20:45:14', 'keyf':null},{'timef':'2017/02/16 20:45:14', 'keyf':null},{'timef':'2017/02/17 20:45:14', 'keyf':null},{'timef':'2017/02/18 20:45:14', 'keyf':null},{'timef':'2017/02/19 20:45:14', 'keyf':null},{'timef':'2017/02/20 20:45:14', 'keyf':null},{'timef':'2017/02/21 20:45:14', 'keyf':null},{'timef':'2017/02/22 20:45:14', 'keyf':null},{'timef':'2017/02/23 20:45:14', 'keyf':null},{'timef':'2017/02/24 20:45:14', 'keyf':null},{'timef':'2017/02/25 20:45:14', 'keyf':null},{'timef':'2017/02/26 20:45:14', 'keyf':null},{'timef':'2017/02/27 20:45:14', 'keyf':null},{'timef':'2017/02/28 20:45:14', 'keyf':null},{'timef':'2017/03/01 20:45:14', 'keyf':null},{'timef':'2017/03/02 20:45:14', 'keyf':null},{'timef':'2017/03/03 20:45:14', 'keyf':null},{'timef':'2017/03/04 20:45:14', 'keyf':null},{'timef':'2017/03/05 20:45:14', 'keyf':null},{'timef':'2017/03/06 20:45:14', 'keyf':null},{'timef':'2017/03/07 20:45:14', 'keyf':null},{'timef':'2017/03/08 20:45:14', 'keyf':null},{'timef':'2017/03/09 20:45:14', 'keyf':null},{'timef':'2017/03/10 20:45:14', 'keyf':null},{'timef':'2017/03/11 20:45:14', 'keyf':null},{'timef':'2017/03/12 20:45:14', 'keyf':null},{'timef':'2017/03/13 20:45:14', 'keyf':null},{'timef':'2017/03/14 20:45:14', 'keyf':null},{'timef':'2017/03/15 20:45:14', 'keyf':null},{'timef':'2017/03/16 20:45:14', 'keyf':null},{'timef':'2017/03/17 20:45:14', 'keyf':null},{'timef':'2017/03/18 20:45:14', 'keyf':null},{'timef':'2017/03/19 20:45:14', 'keyf':null},{'timef':'2017/03/20 20:45:14', 'keyf':null},{'timef':'2017/03/21 20:45:14', 'keyf':null},{'timef':'2017/03/22 20:45:14', 'keyf':null},{'timef':'2017/03/23 20:45:14', 'keyf':null},{'timef':'2017/03/24 20:45:14', 'keyf':null},{'timef':'2017/03/25 20:45:14', 'keyf':null},{'timef':'2017/03/26 21:45:14', 'keyf':null},{'timef':'2017/03/27 21:45:14', 'keyf':null},{'timef':'2017/03/28 21:45:14', 'keyf':null},{'timef':'2017/03/29 21:45:14', 'keyf':null},{'timef':'2017/03/30 21:45:14', 'keyf':null},{'timef':'2017/03/31 21:45:14', 'keyf':null},{'timef':'2017/04/01 20:45:14', 'keyf':null},{'timef':'2017/04/02 20:45:14', 'keyf':null},{'timef':'2017/04/03 20:45:14', 'keyf':null},{'timef':'2017/04/04 20:45:14', 'keyf':null},{'timef':'2017/04/05 20:45:14', 'keyf':null},{'timef':'2017/04/06 20:45:14', 'keyf':null},{'timef':'2017/04/07 20:45:14', 'keyf':null},{'timef':'2017/04/08 20:45:14', 'keyf':null},{'timef':'2017/04/09 20:45:14', 'keyf':null},{'timef':'2017/04/10 20:45:14', 'keyf':null},{'timef':'2017/04/11 20:45:14', 'keyf':null},{'timef':'2017/04/12 20:45:14', 'keyf':null},{'timef':'2017/04/13 20:45:14', 'keyf':null},{'timef':'2017/04/14 20:45:14', 'keyf':null},{'timef':'2017/04/15 20:45:14', 'keyf':null},{'timef':'2017/04/16 20:45:14', 'keyf':null},{'timef':'2017/04/17 20:45:14', 'keyf':null},{'timef':'2017/04/18 20:45:14', 'keyf':null},{'timef':'2017/04/19 20:45:14', 'keyf':null},{'timef':'2017/04/20 20:45:14', 'keyf':null},{'timef':'2017/04/21 20:45:14', 'keyf':null},{'timef':'2017/04/22 20:45:14', 'keyf':null},{'timef':'2017/04/23 20:45:14', 'keyf':null}]}"
        a = jsutil.toList(s,'list',"test107.xml","list2")
        print len(a)
        assert 100 == len(a)
        for e in a :
#            print e
            assert type(e['timef']) == datetime.datetime
        var["OK"] = True
        
    if action == "test7" :
        formatS = FORMATS
        dataS = DATAS
        xml = toformat.toFormat("xml",dataS,formatS,"test107.xml","list1")
        print xml
        assert xml != None
        var["OK"] = True
        
    if action == "test8" :
        formatS = FORMATS
        dataS = DATAS
        xml = toformat.toFormat("csv",dataS,formatS,"test107.xml","list1")
        print xml
        assert xml != None
        var["OK"] = True
        
    if action == "test9" :
        formatS = FORMATS
        dataS = DATAS
        xml = toformat.toFormat("html",dataS,formatS,"test107.xml","list1")
        print xml
        assert xml != None
        var_file = open("/tmp/test.html","w")
        var_file.write(xml)
        var_file.close()
        var["OK"] = True

    if action == "test10" :
        formatS = FORMATS
        dataS = DATAS
        pdf = toformat.toFormat("pdf",dataS,formatS,"test107.xml","list1")
        print pdf
        assert pdf != None
        var_file = open("/tmp/test.pdf","wb")
        var_file.write(pdf)
        var_file.close()
        var["OK"] = True

    if action == "test11" :
        formatS = CHECKFORMATS
        dataS = CHECKDATAS
        pdf = toformat.toFormat("pdf",dataS,formatS,"test107.xml","list3")
        print pdf
        assert pdf != None
        var_file = open("/tmp/test.pdf","wb")
        var_file.write(pdf)
        var_file.close()
        csv = toformat.toFormat("csv",dataS,formatS,"test107.xml","list3")
        print csv
        xml = toformat.toFormat("xml",dataS,formatS,"test107.xml","list3")
        print xml
        var["OK"] = True
