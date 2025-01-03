package com.bookstore.BookstoreManagementSystem;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnectionManager {
    private static final String URL = "jdbc:mysql://localhost:3306/BookstoreDB";
    private static final String USER = "root";
    String driver = "com.mysql.cj.jdbc.Driver";
    private static final String PASSWORD = "Bekumsa1995!";
    
    public Connection openConnection() throws SQLException {
    	try {
            Class.forName(driver);
	        System.out.println("the driver has been loaded sucessfully");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection == null) {
		        System.out.println("connection can't be established");
            }
	        return connection;
    	} 
    	catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }
		return null;
    }

}
