'''
Created on 25 lis 2014

@author: sbartkowski
'''

class Atomizer(object):
    '''
    classdocs
    '''


    def __init__(self, R):
        '''
        Constructor
        '''
        self.R = R
        self.tokens = None
        
    def __addElem(self,w):
        if w == None or w == '-': return
        if self.tokens == None : self.tokens = [w]
        else : self.tokens.append(w)
        
    def __createAtomList(self,l):
        i = 0
        st = None
        prevc = None
        while i < len(l) :
            c = l[i]
            if c == '-' and prevc == '-' : break
            prevc = c
            i = i + 1
            if c == ';' :
                self.__addElem(st)
                self.__addElem(c)
                st = None
                continue                
            if c.isspace() or c == ',' or c == '@' or c == '(' or c == ') ':
                self.__addElem(st)
                st = None  
                continue
            if st == None : st = c
            else : st = st + c
        self.__addElem(st)
        
    def nextAtom(self):
        while self.tokens == None :
            l = self.R.nextLine()
            if l == None : return None
            self.__createAtomList(l)
            self.inde = 0
        w = self.tokens[self.inde]
        self.inde = self.inde + 1
        if self.inde >= len(self.tokens) : self.tokens = None
        return w
        