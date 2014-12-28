'''
Created on 25 lis 2014

@author: sbartkowski
'''

import io

class ReadFiles(object):
    '''
    classdocs
    '''


    def __init__(self, *files):
        '''
        Constructor
        '''
        self.files = files
        self.fileinde = 0
        self.lines = None
        self.lineinde = 0
        self.encoding = None
        self.setutf16()
        
#        file = io.open('data.txt','r', encoding='utf-16-le')
    
    def setEncoding(self,encoding = None):
        self.encoding = encoding
        
    def setutf16le(self):
        self.setEncoding('utf-16-le')
        
    def setutf16(self):
        self.setEncoding('utf-16')

    def _open(self):
        fname = self.files[self.fileinde]
        if self.encoding == None : return open(fname,"r")
        else : return io.open(fname,'r', encoding=self.encoding)

        
    def nextLine(self):
        if self.lines == None :
            
            while True :
                
                if self.fileinde >= len(self.files) : return None
                f = self._open()
                self.fileinde = self.fileinde + 1
                # throws exception if does not exist
                self.lines = f.readlines()
                f.close()
                if len(self.lines) > 0 :
                    self.lineinde = 0
                    break
        
        l = self.lines[self.lineinde].strip()
        self.lineinde = self.lineinde + 1
        if self.lineinde >= len(self.lines) : self.lines = None
        return l    
                
            
                
             
            
        