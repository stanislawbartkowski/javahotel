'''
Created on 25 lis 2014

@author: sbartkowski
'''

import sets
from os import listdir
from os.path import isfile, join

from org.readfiles import readfiles
from org.atomizer import atomizer
from org.tokenizer import tokenizer
from org.foreign import foreign

def _toKey(F):
    if F.schema == None : return F.name.upper()
    return (F.schema + "." + F.name).upper()

class TABLECONTAINER(object):
    '''
    classdocs
    '''

    def __init__(self, R):
        '''
        Constructor
        '''
        self.R = R
        self.S = sets.Set()
        self.T = []
        self.V = []
        self.P = []
        self.F = []
        
    def createContainer(self):
#        if type(self.dir) == str :  R = readfiles.ReadFiles(self.dir)
#        else :
#            li = [join(self.dir,f) for f in listdir(self.dir) if isfile(join(self.dir,f)) ]
#            R = readfiles.ReadFiles(*li)
        A = atomizer.Atomizer(self.R)
        T = tokenizer.TOKENIZER(A)
        F = foreign.ObjectSearcher(T)
        a = F.nextObject()
        CMAP = { tokenizer.TABLE : self.T, tokenizer.FUNCTION : self.F, tokenizer.PROCEDURE : self.P, tokenizer.VIEW : self.V }
#        print a
        while a != None :
#            print a.schema,a.name
            (oType,name) = a
#            print oType,name
            if oType == tokenizer.TABLE : self.S.add(_toKey(name))
            if CMAP.has_key(oType) : CMAP[oType].append(_toKey(name))
            a = F.nextObject()
            
    def isTable(self,F):
        return _toKey(F) in self.S
            


         
         
        
        