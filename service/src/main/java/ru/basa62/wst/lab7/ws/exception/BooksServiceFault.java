package ru.basa62.wst.lab7.ws.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksServiceFault {
    private String message;
}
