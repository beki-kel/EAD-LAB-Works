package com.spring.BookStore;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Book {
    public int id;
    public String author;
    public String title;
    public Double price;
}
