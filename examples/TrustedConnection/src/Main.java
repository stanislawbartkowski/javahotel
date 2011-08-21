
/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import java.sql.SQLException;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.db2.trusted.TrustedConnection;

public class Main {

    private static void goTrusted() throws SQLException {
        TrustedConnection trust = new TrustedConnection(50000, "SAMPLE", "trust",
                "trustme", "think", "DB2INST1");
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
