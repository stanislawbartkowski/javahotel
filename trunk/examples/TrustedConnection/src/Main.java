
import java.sql.SQLException;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.db2.trusted.TrustedConnection;

public class Main {

    private static void goTrusted() throws SQLException {
        TrustedConnection trust = new TrustedConnection(50000, "SAMPLE", "trust",
                "trustme", "192.168.1.6", "DB2INST1");
        trust.trustConnect();
        JdbcTemplate jTemplate = new JdbcTemplate(
                trust.constructReuseDataSource("john"));
        int no = jTemplate.queryForInt("SELECT COUNT(*) FROM ACT");
        String sql = "update employee set bonus=30000 where lastname='WONG'";
        jTemplate.update(sql);
        
        jTemplate = new JdbcTemplate(trust.constructReuseDataSource("mary"));
        no = jTemplate.queryForInt("SELECT COUNT(*) FROM EMPLOYEE");
        
        sql = "update employee set bonus=30000 where lastname='WONG'";
        try {
          jTemplate.update(sql);
        } catch (BadSqlGrammarException e) {
            // as expected
            // SQL0551N code
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            goTrusted();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
