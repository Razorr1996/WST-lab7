package ru.basa62.wst.lab7.ws.exception;

import lombok.Getter;

import javax.xml.ws.WebFault;

@WebFault(faultBean = "ru.basa62.wst.lab7.ws.exception.BooksServiceFault")
public class BooksServiceException extends Exception {
    @Getter
    private final BooksServiceFault faultInfo;

    public BooksServiceException(String message, BooksServiceFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public BooksServiceException(String message, Throwable cause, BooksServiceFault faultInfo) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }
}
