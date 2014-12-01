import cutil,con

def calculateVatBrutto(brutto,level):
    if level == None : return (brutto,None)
    print brutto,level
    v = level/( 100  + level)
    print v
    vat = con.mulDecimal(brutto,v)
    print vat
    return (con. minusDecimal(brutto,vat),vat) 

class CalcVat :
    
    def __init__(self):
        self.lvat = {}
        for v in cutil.getDict("vat") :
            l = v.getAttr("level")
            if l == "" : self.lvat[v.getName()] = None
            else : self.lvat[v.getName()] = float(l)
        self.bvat = {}
       
    def addVatLine(self,brutto,vat):
        abrutto = 0.0
        if self.bvat.has_key(vat) : abrutto = self.bvat[vat]
        self.bvat[vat] = con.addDecimal(abrutto,brutto)
        
    def calculateVat(self):
        lvat = []
        for v in self.bvat : 
            level = self.lvat[v]
            lvat.append(calculateVatBrutto(self.bvat[v],level))
        return lvat
            
            
              
        
         