package jdbc_connection;

import java.sql.*;

public class JDBCDemo {
	public static void main(String[] args) {
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/StudentsDB";
			String username = "root"; // your username
			String password = "root"; // your password
			Class.forName(driver); // optional
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Established Connection");
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
	}
}
