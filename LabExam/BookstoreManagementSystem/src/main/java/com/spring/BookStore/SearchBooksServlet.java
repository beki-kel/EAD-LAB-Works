package com.spring.BookStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Setter;

@RequestMapping("/searchTasks")
public class SearchBooksServlet {
	rivate static final String query = "select * from Books where title like ?";

    @Setter
    private DBConnectionManager db;

    @GetMapping
    @ResponseBody
    public String searchBasks(@RequestParam("title") String title) {
        StringBuilder htmlResponse = new StringBuilder();
        db.openConnection();
        Connection connection = db.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "%" + title + "%");
            ResultSet rs = ps.executeQuery();
            ArrayList<Book> books = new ArrayList<Book>();
            while (rs.next()) {
                books.add(new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4)));
            }

            htmlResponse.append("<html><head> </head><body>");
            htmlResponse.append("<h2>Showing search results for: '" + title+ "'</h2>");
            htmlResponse.append("<table style='border: 2px solid black; border-spacing: 10px;'>");
            htmlResponse.append("<tr>");
            htmlResponse.append("<th>Book ID</th>");
            htmlResponse.append("<th>Title</th>");
            htmlResponse.append("<th>author</th>");
            htmlResponse.append("<th>Due Date</th>");
            htmlResponse.append("</tr>");
            for (Book task : ts) {
                htmlResponse.append("<tr>");
                htmlResponse.append("<td>").append(task.id).append("</td>");
                htmlResponse.append("<td>").append(task.description).append("</td>");
                htmlResponse.append("<td>").append(task.status).append("</td>");
                htmlResponse.append("<td>").append(task.dueDate).append("</td>");
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
