
'''
ALTER TABLE "AUD"."REFCOMPONENTSERVICE" ADD CONSTRAINT "FK_REFCOMPONENTSERVICE_REFCOMPONENT" FOREIGN KEY
    ("COMPONENTID")
    REFERENCES "AUD"."REFCOMPONENT"
    ("COMPONENTID")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
@
'''

def _toA(s) :
    return '"' + s + '"'

def toS(T) :
    if T.schema == None : return T.name
    return T.schema + "." + T.name

def _toLS(l):
    out = None 
    for k in l :
        if out == None : out = "(" + k
        else : out = out + ' , ' + k
    return out + ")"     

def foreignDB2(F):
    outL = "ALTER TABLE " + toS(F.ATA) + " ADD CONSTRAINT " + _toA(F.cname) + " FOREIGN KEY" + '\n'
    outL = outL + "   " +  _toLS(F.keylist) + '\n'
    outL = outL + "   " + "REFERENCES " + toS(F.REFTA) + '\n'
    outL = outL + "   " + _toLS(F.reflist) + '\n'
    return outL + '@'