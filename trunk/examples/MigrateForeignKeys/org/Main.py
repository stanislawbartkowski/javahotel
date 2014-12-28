'''
Created on 25 lis 2014

@author: sbartkowski
'''

FILE1 = ''
FILE2= ''
FILE3 = ''
FILE4 = ''

FILE = ""
FILET = ""
FILET2 = ""

TA = ''

DIR = ''

FF = ''
FF1 = ''
FF2 = ''

INF="resource/script_tables.sql"


from org.readfiles import readfiles
from org.atomizer import atomizer
from org.tokenizer import tokenizer
from org.foreign import foreign
from org.publish import foreignDB2
from org.publish import listOfO
from org.tablecontainer import tablecontainer

def test1():

    print 'Hello'
#    R = readfiles.ReadFiles(FILE1, FILE2)
#    R = readfiles.ReadFiles(FF)
    R = readfiles.ReadFiles(INF) 
#    R.setutf16le()
    R.setutf16()
    l = R.nextLine()
    while l != None :
        print l
        l = R.nextLine()

def test2() :
    R = readfiles.ReadFiles(INF)
    R.setutf16()
    A = atomizer.Atomizer(R)
    a = A.nextAtom()
    while a != None :
        print a
        a = A.nextAtom()

def test3():    
    R = readfiles.ReadFiles(INF)
    A = atomizer.Atomizer(R)
    T = tokenizer.TOKENIZER(A)
    a = T.nextToken()
    while a != None :
        (o,w) = a
        print o,w
        a = T.nextToken()

def test4():    
    R = readfiles.ReadFiles(INF)
    A = atomizer.Atomizer(R)
    T = tokenizer.TOKENIZER(A)
    F = foreign.ForeignSearcher(T)
    a = F.nextForeign()
    f = open("output/foreign_keys.db2","w")
    while a != None :
#        print a
        s =  foreignDB2.foreignDB2(a)
        print s
        print ""
        print ""
        f.write(s)
        f.write("\n")
        f.write("\n")

        a = F.nextForeign()
    f.close()

def test5():    
    R = readfiles.ReadFiles(TA)
    A = atomizer.Atomizer(R)
    T = tokenizer.TOKENIZER(A)
    F = foreign.ObjectSearcher(T)
    a = F.nextTable()
    while a != None :
        print a.schema,a.name
        a = F.nextTable()

def test6() :
    C = tablecontainer.TABLECONTAINER(DIR)
    C.createContainer()
    
def test7() :
    C = tablecontainer.TABLECONTAINER(DIR)
    C.createContainer()
    R = readfiles.ReadFiles(FILE1,FILE2,FILE3,FILE4)
#    R = readfiles.ReadFiles(FILE1)
    A = atomizer.Atomizer(R)
    T = tokenizer.TOKENIZER(A)
    F = foreign.ForeignSearcher(T)
    a = F.nextForeign()
    f = open("foreign_keys.db2","w")    
    while a != None :
        if C.isTable(a.ATA) : 
            s = foreignDB2.foreignDB2(a)
            print s
            print ""
            print ""
            f.write(s)
            f.write("\n")
            f.write("\n")
        a = F.nextForeign()
    f.close()

def test8():    
#    R = readfiles.ReadFiles(FF)
#    R = readfiles.ReadFiles(FF1)
    R = readfiles.ReadFiles(FF2)
    R.setutf16le()
    C = tablecontainer.TABLECONTAINER(R)
    C.createContainer()
    listOfO.createListOfObjects('/tmp/l.txt',C)
    
                        
if __name__ == '__main__':
#    test8()
#    test7()
#    test6()
#    test5()
    test4()
#    test3()
#    test2()
#    test1()

