'''
Created on 27 lis 2014

@author: sbartkowski
'''

def _nL(f):
    f.write ('\n')

def _printL(title,f, li):
    _nL(f)
    f.write('-- ' + title + ' --')
    _nL(f)
    _nL(f)    
    for elem in li :
        f.write(elem)
        _nL(f)        

def createListOfObjects(outname, T):
    f = open(outname,"w")
    _printL("Tables",f,T.T)
    _printL("Views",f,T.V)
    _printL("Functions",f,T.F)
    _printL("Procedures",f,T.P)
    f.close()