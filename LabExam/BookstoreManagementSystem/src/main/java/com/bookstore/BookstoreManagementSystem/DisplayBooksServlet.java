package com.bookstore.BookstoreManagementSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/displayBooks")
public class DisplayBooksServlet extends HttpServlet {
    private static final long serialVersionUID = -9190209876419201320L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/BookstoreDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Bekumsa1995!"; 
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            response.getWriter().println(" JDBC Driver not found.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM Books";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                PrintWriter out = response.getWriter();
                out.println("<html><body><table border='1'>");
                out.println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Price</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("title") +
                            "</td><td>" + rs.getString("author") + "</td><td>" + rs.getDouble("price") + "</td></tr>");
                }
                out.println("</table></body></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error displaying Books: " + e.getMessage());
        }
    }

}
