'''
Created on 25 lis 2014

@author: sbartkowski
'''

ALTER=0
SEMICOLON=1
TABLE=2
ADD=3
CONSTRAINT=4
FOREIGN=5
KEY = 6
REFERENCES = 7
BEGCOMMENT=8
ENDCOMMENT=9
CREATE=10
VIEW=11
FUNCTION=12
PROCEDURE=13
GO=14

_MAP = { 'ALTER' : ALTER, ';' : SEMICOLON, '@' : SEMICOLON, 'TABLE' : TABLE, 'ADD' : ADD, "CONSTRAINT" : CONSTRAINT, 'FOREIGN' : FOREIGN, 'KEY' : KEY, \
         'REFERENCES' : REFERENCES,'/*' : BEGCOMMENT, '*/' : ENDCOMMENT, 'CREATE'  : CREATE, 'VIEW' : VIEW ,\
         'FUNCTION' : FUNCTION, 'PROCEDURE' : PROCEDURE, "GO" : GO } 

#
#    ADD CONSTRAINT [FK_ObjAction_ObjChannel] FOREIGN KEY ([ChannelID]) REFERENCES [aud].[ObjChannel] ([ChannelID]);
 

class TOKENIZER:
    '''
    classdocs
    '''


    def __init__(self, A):
        '''
        Constructor
        '''
        self.A = A
        
    def nextToken(self):
        incomment = False
        while True :
            w = self.A.nextAtom()
            if w == None : return None
            a = None
            keyw = w.upper()
            if _MAP.has_key(keyw) : a = _MAP[keyw]
            if incomment :
                if a == ENDCOMMENT : incomment = False
                continue
            if a == BEGCOMMENT : incomment = True
            else :
                if a == GO : a = SEMICOLON
                return (a,w)  
            
          
        
        
        
        
        
        