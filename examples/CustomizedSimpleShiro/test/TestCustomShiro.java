import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class TestCustomShiro {

    private void testShiro(String realM) {
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(
                realM);
        org.apache.shiro.mgt.SecurityManager securityManager = factory
                .getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("guest",
                "guest1");
        try {
          currentUser.login(token);
          fail("This password should not work !");
        } catch (Exception e) {
            System.out.println("Failure expected, try again with the valid password");
        }
        token = new UsernamePasswordToken("guest",
                "guest");
        currentUser.login(token);
        System.out.println("Hello guest, you are welcome ...");
        assertTrue("Guest role expected", currentUser.hasRole("guest"));
        assertFalse("Admin role not expected", currentUser.hasRole("admin"));
        currentUser.logout();
        try {
            token = new UsernamePasswordToken("impostor", "guest");
            currentUser.login(token);
            fail("Impostor - you are not welcome !!!");
        } catch (Exception e) {
            System.out.println("Impostor - welcome you as a guest only !");
        }
    }

    @Test
    public void test() {
        testShiro("classpath:shiro.ini");
    }

    @Test
    public void test1() {
        testShiro("classpath:custom.ini");
    }

    @Test
    public void test2() {
        testShiro("classpath:inject.ini");
    }

}
