'''
Created on 25 lis 2014

@author: sbartkowski
'''

from org.tokenizer import tokenizer 

class TNAME :
    
    def __init__(self,schema,name):
        self.schema = schema
        self.name = name

def _removeB(s):
    return s.replace('[','').replace(']','').replace('"','')

def _toTNAME(s):
    cc = s.split('.')
    schema = None
    name = _removeB(cc[0])
    if len(cc) > 1 :
        schema = name
        name = _removeB(cc[1])
    return TNAME(schema,name)

def _toList(s):
    s = s.replace('(','').replace(')','')
    ss = s.split(',')
    tlist = []
    for k in ss : tlist.append(_removeB(k))
    return tlist
        
class FOREIGN :
    
    def __init__(self,ATA,cname,keylist,REFTA,reflist):
        self.ATA = ATA
        self.cname = cname
        self.keylist = keylist
        self.REFTA = REFTA
        self.reflist = reflist
                

class ForeignSearcher :
    '''
    classdocs
    '''
    


    def __init__(self, T):
        '''
        Constructor
        '''
        self.T = T
        
        
    def nextForeign(self):
        state = 0 
        # 0 : nothing
        # 1 : ALTER found
        # 2 : + TABLE found
        # 3 : + ADD found
        # 4 : + CONSTRAINT found
        # 5 : + FOREIGN
        # 6 : + KEY
        # 7 : + REFERENCES 
        # 8 : + REFERENCES + table name
        while True :
            if state == 0 :
                alteredtable = None
                constraintname = None
                foreignkeys = None
                referencetable = None                
            to = self.T.nextToken()
            if to == None : return None
            (t,w) = to
            if t == tokenizer.ALTER and state == 0 : 
                state = 1
                continue
            if t == tokenizer.TABLE and state == 1 :
                state = 2
                continue
            if t == tokenizer.ADD and state == 2 :
                state = 3
                continue
            if t == tokenizer.CONSTRAINT and state == 3 :
                state = 4
                continue
            if t == tokenizer.FOREIGN and (state == 4 or state == 3):
                state = 5 
                continue
            if t == tokenizer.KEY and state == 5 :
                state = 6
                continue
            if t == tokenizer.REFERENCES and state == 6 :
                state = 7
                continue
            if t == tokenizer.SEMICOLON : 
                state = 0
                continue
            
            if t == None  :
                if state == 2 : 
                    if alteredtable == None : alteredtable  = _toTNAME(w)
                elif state == 4 : constraintname = _removeB(w)
                elif state == 6 : foreignkeys = _toList(w)
                elif state == 7 : 
                    if referencetable == None : referencetable = _toTNAME(w)
                    state = 8
                elif state == 8 :
                    referencelist = _toList(w)
                    if constraintname == None : constraintname = "FK_" + alteredtable.name + "_" + referencetable.name
                    return FOREIGN(alteredtable,constraintname,foreignkeys,referencetable,referencelist)

class ObjectSearcher :
    
    def __init__(self, T):
        '''
        Constructor
        '''
        self.T = T
                    
    def nextObject(self) :
        state = 0
        # state = 1 : CREATE
        # state = 2 : + TABLE or VIEW or FUNCTION or PROCEDURE
        oType = None
        while True :
            to = self.T.nextToken()
            if to == None : return None
            (t,w) = to            
            if t == tokenizer.CREATE and state == 0 : 
                state = 1
                continue
            if (t == tokenizer.TABLE or t == tokenizer.VIEW or t == tokenizer.FUNCTION or t == tokenizer.PROCEDURE) and state == 1 :
                state = 2
                oType = t
                continue
            if t == None and state == 2 :
                return (oType,_toTNAME(w))        