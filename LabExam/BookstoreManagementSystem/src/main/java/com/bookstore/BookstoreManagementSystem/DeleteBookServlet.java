package com.bookstore.BookstoreManagementSystem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/deleteBook")
public class DeleteBookServlet extends HttpServlet {
    private static final long serialVersionUID = -9190209876419201320L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/BookstoreDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Bekumsa1995!"; 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");

        int bookId;
        try {
            bookId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.getWriter().println("your book ID is invalid.");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            response.getWriter().println("JDBC Driver not found.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Books WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, bookId);
                int rowsAffected = stmt.executeUpdate();

                try (PrintWriter out = response.getWriter()) {
                    if (rowsAffected > 0) {
                        out.println("You deleted the book succesfully");
                    } else {
                        out.println("Book not found.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error deleting the book: " + e.getMessage());
        }
    }
}
