package ru.basa62.wst.lab7;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooksEntity {
    private Long id;
    private String name;
    private String author;
    private Date publicDate;
    private String isbn;
}
