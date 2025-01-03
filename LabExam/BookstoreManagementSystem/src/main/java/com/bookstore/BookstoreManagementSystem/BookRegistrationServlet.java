package com.bookstore.BookstoreManagementSystem;

// Bereket Kelay UGR/9587/14

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class BookRegistrationServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    // Database credentials and URL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/TodoDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Bekumsa1995!";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        
        String author = request.getParameter("author");
        String title = request.getParameter("title");
        String price = request.getParameter("price");

        // Validate input
        if (author == null || title == null ||price == null) {
            response.getWriter().println("your input is invalid.");
            return;
        }

        // Load MySQL driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("JDBC Driver not found.");
            return;
        }

        // Connect to the database and insert the task
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO Books (author, title, price) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, author);
                stmt.setString(2, title);
                stmt.setString(3, price);
                stmt.executeUpdate();

                response.getWriter().println("Book successfully registered!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error registering the Book: " + e.getMessage());
        }
    }
}
