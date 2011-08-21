
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.db2.trusted.TrustedConnection;

public class Main {

    private static void goTrusted() throws SQLException {
//        TrustedConnection trust = new TrustedConnection(50000, "SAMPLE", "trust",
//                "trustme", "think", "DB2INST1");
        TrustedConnection trust = new TrustedConnection(50013, "SAMPLE", "trust",
                "trustme", "ubu64", "DB2SAMPL");
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
        	System.out.println("Failed");
        }
    }
    
    private static class Employee {
    	
    	private String firstName,lastName;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
    	
    }
    
    private static class EMapper implements RowMapper<Employee> {

		@Override
		public Employee mapRow(ResultSet arg0, int arg1) throws SQLException {
			Employee e = new Employee();
			e.setFirstName(arg0.getString(2));
			e.setLastName(arg0.getString(4));
			return e;
		}
    	
    }
    
    private static void printE(TrustedConnection trust, String user) {
        JdbcTemplate jTemplate = new JdbcTemplate(
                trust.constructReuseDataSource(user));
        System.out.println("List of employees");
        System.out.println("-----------------");
        List<Employee>  listE = jTemplate.query("SELECT * FROM LBAC_EMPLOYEE", new EMapper());
        for (Employee e : listE) {
        	System.out.println(e.getFirstName() + " " + e.getLastName());
        }
        System.out.println(user + " : " + listE.size());    	
    }
    
    private static void runEQuery(String user1,String user2) throws SQLException {
        TrustedConnection trust = new TrustedConnection(50013, "SAMPLE", "trust",
                "trustme", "ubu64", "DB2SAMPL");
        trust.trustConnect();
    	printE(trust,user1);
    	printE(trust,user2);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
        	if (args.length == 3 && args[0].equals("-u")) {
        		runEQuery(args[1],args[2]);
        	} else { goTrusted(); }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
