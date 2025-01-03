package com.spring.BookStore;
//Bereket Kelay Belihu UGR/9587/14

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Setter;

@RequestMapping("/addBook")
public class BookRegistrationServlet {
	private static final String query = "insert into Books(title, author, price) values(?, ?, ?)";
    @Setter
    private DBConnectionManager db;

    @GetMapping
    @ResponseBody
    public String showForm() {
        return "<html><body>"
                + "<h2>Book Registration Form</h2>"
                + "<form method='post' action='/addTask'>"
                + "Title: <input type='text' name='title'><br>"
                + "Author: <input type='text' name='author'><br>"
                + "Price: <input type='text' name='price'><br>"
                + "<input type='number' value='Add Task'>"
                + "</form>"
                + "</body></html>";
    }

    @PostMapping
    @ResponseBody
    public String addTask(@RequestParam String title, @RequestParam String author, @RequestParam Double price) {
        db.openConnection();

        try {
            PreparedStatement ps = db.getConnection().prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, price.toString());
            int count = ps.executeUpdate();
            db.closeConnection();
            if (count == 1) {
                return "Book Registration Successful";
            } else {
                return "Book Registration Failed";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Book Registration Failed";
        } finally {
            db.closeConnection();
        }
    }
}
