package com.spring.BookStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Setter;

@RequestMapping("/displayBooks")
public class DisplayBooksServlet {
	private static final String query = "select * from Books";

    @Setter
    private DBConnectionManager db;

    @GetMapping
    @ResponseBody
    public String displayTasks() {
        StringBuilder htmlResponse = new StringBuilder();
        db.openConnection();
        Connection connection = db.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<Book> books = new ArrayList<Book>();
            while (rs.next()) {
                books.add(new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4)));
            }

            htmlResponse.append("<html><head> </head><body>");
            htmlResponse.append("<table>");
            htmlResponse.append("<tr>");
            htmlResponse.append("<th>Book ID</th>");
            htmlResponse.append("<th>Author</th>");
            htmlResponse.append("<th>Title</th>");
            htmlResponse.append("<th>Price</th>");
            htmlResponse.append("<th>Delete</th>");
            htmlResponse.append("</tr>");
            for (Book book : books) {
                htmlResponse.append("<tr>");
                htmlResponse.append("<td>").append(book.id).append("</td>");
                htmlResponse.append("<td>").append(book.author).append("</td>");
                htmlResponse.append("<td>").append(book.title).append("</td>");
                htmlResponse.append("<td>").append(book.price).append("</td>");
                htmlResponse.append("<td><form method='post' action='/deleteTask?id=").append(book.id)
                        .append("'>")
                        .append("<input type='submit' value='Delete'/>")
                        .append("</form></td>");
                htmlResponse.append("</tr>");
            }
            htmlResponse.append("</table>");
            htmlResponse.append("</body></html>");
        } catch (SQLException se) {
            se.printStackTrace();
            htmlResponse.append("<h1>").append(se.getMessage()).append("</h1>");
        } catch (Exception e) {
            e.printStackTrace();
            htmlResponse.append("<h1>").append(e.getMessage()).append("</h1>");
        }

        htmlResponse.append("</body></html>");
        db.closeConnection();
        return htmlResponse.toString();
    }
}
