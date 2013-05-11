import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import com.sample.ejbcalc.IHelloCalc;
import com.sample.ejblocator.EJBSampleLocator;


public class TestEJB {
    
    private static IHelloCalc i; 

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        i = EJBSampleLocator.construct();
    }

    @Test
    public void test1() {
        assertEquals("Hello, I'm your Calculator",i.getHello());
    }

    @Test
    public void test4() {
        assertEquals(4,i.add(2, 2));
        assertFalse(i.add(2, 2) == 5);
        assertEquals(2,i.add(2, 0));
        assertEquals(2,i.add(0, 2));
        assertEquals(-1,i.add(2, -3));
    }

}
