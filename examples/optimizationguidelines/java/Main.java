import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {

	private static final String user = "db2inst1";
	private static final String password = "db2inst1";
	private static final String url = "jdbc:db2://aixt:60004/sample:currentExplainMode=YES;optimizationProfile=SELECT_SIMPLE_OPTIMIZER;";
	
	private static void test(Connection con) throws SQLException {
		Statement st = con.createStatement();
		ResultSet res = st.executeQuery("SELECT * FROM TESTTS WHERE IDE=10");
		while (res.next()) {
			String name = res.getString("NAME");
			System.out.println(name);
		}
		
	}

	
	public static void main(String[] args) {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			Connection con = DriverManager.getConnection(url, user, password);
			test(con);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
