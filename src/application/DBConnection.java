package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnection {

	private final String dburl = "jdbc:mysql://localhost:3306/javafx_jasper";
	private final String username = "ken"; // my  username is ken
	private final String password = ""; // my password is null
	private Connection connect;

	public DBConnection() {

	}

	public Connection getConnection() {
		try {
			connect = DriverManager.getConnection(dburl, username, password);
		}catch(Exception e) {
			e.printStackTrace();
		}

		return connect;
	}

	public void close(Connection connect, PreparedStatement pstmt, ResultSet rs) {
		try {
			if(connect != null)
				connect.close();
			if(pstmt != null)
				pstmt.close();
			if(rs != null)
				rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void close(Connection connect, PreparedStatement pstmt) {
		try {
			close(connect, pstmt, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void close(PreparedStatement pstmt) {
		try {
			close(null, pstmt, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
