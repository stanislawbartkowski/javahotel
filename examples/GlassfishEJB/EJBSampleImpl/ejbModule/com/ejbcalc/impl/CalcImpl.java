package com.ejbcalc.impl;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.sample.ejbcalc.IEJBName;
import com.sample.ejbcalc.IHelloCalc;

/**
 * Session Bean implementation class CalcImpl
 */
@Stateless
@EJB(name = IEJBName.JNDIRemote, beanInterface = IHelloCalc.class)
@Remote
public class CalcImpl implements IHelloCalc {

    @Override
    public String getHello() {
        return "Hello, I'm your Calculator";
    }

    @Override
    public int add(int elem1, int elem2) {
        return elem1 + elem2;
    }

}
