import cutil,vat

def dialogaction(action,var):
    
    cutil.printVar("test vat",action,var)
    
    if action == "test1" :
        V = vat.CalcVat()
        V.addVatLine(100,"22%")
        li = V.calculateVat()
        for l in li : print l
        assert 1 == len(li)
        l = li[0]
        print l[0],l[1]
        assert 81.97 == l[0]
        assert 18.03 == l[1]
        var["OK"] = True

    if action == "test2" :
        V = vat.CalcVat()
        V.addVatLine(100,"22%")
        V.addVatLine(100,"23%")
        li = V.calculateVat()
        for l in li : print l
        assert 2 == len(li)
        assert 81.3 == li[0][0]
        assert 81.97 == li[1][0]
        var["OK"] = True

    if action == "test3" :
        V = vat.CalcVat()
        V.addVatLine(25,"7%")
        V.addVatLine(25,"7%")
        V.addVatLine(25,"7%")
        V.addVatLine(25,"7%")
        li = V.calculateVat()
        for l in li : print l
        l = li[0]
        print l[0],l[1]
        assert 93.46 == l[0]
        assert 6.54 == l[1]
        var["OK"] = True

    if action == "test4" :
        V = vat.CalcVat()
        V.addVatLine(100,"0%")
        V.addVatLine(100,"free")
        li = V.calculateVat()
        for l in li : print l
        assert 2 == len(li)
        assert 100.0 == li[0][0]
        assert 100.0 == li[1][0]
        assert None == li[0][1]
        assert 0.0 == li[1][1]
        var["OK"] = True
        
        