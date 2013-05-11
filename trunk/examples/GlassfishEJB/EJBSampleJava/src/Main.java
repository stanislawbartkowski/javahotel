import javax.naming.NamingException;

import com.sample.ejbcalc.IHelloCalc;
import com.sample.ejblocator.EJBSampleLocator;


public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            IHelloCalc i = EJBSampleLocator.construct();
            System.out.println(i.getHello());
            System.out.println(i.add(2, 2));
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
