package com.sample.ejblocator;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sample.ejbcalc.IEJBName;
import com.sample.ejbcalc.IHelloCalc;

public class EJBSampleLocator {
    
    public static IHelloCalc construct() throws NamingException {
        InitialContext ctx = new InitialContext();
        Object remoteObj = new InitialContext().lookup(IEJBName.JNDIRemote);
        IHelloCalc inter = (IHelloCalc) remoteObj;
        return inter;
    }

}
