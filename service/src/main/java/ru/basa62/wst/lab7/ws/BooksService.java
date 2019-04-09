package ru.basa62.wst.lab7.ws;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.basa62.wst.lab7.BooksDAO;
import ru.basa62.wst.lab7.BooksEntity;
import ru.basa62.wst.lab7.ws.exception.BooksServiceException;
import ru.basa62.wst.lab7.ws.exception.BooksServiceFault;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@WebService(serviceName = "BooksService")
public class BooksService {
    @Inject
    @NonNull
    private BooksDAO booksDAO;

    @WebMethod
    public List<BooksEntity> findAll() throws BooksServiceException {
        try {
            return booksDAO.findAll();
        } catch (SQLException e) {
            String message = "SQL error: " + e.getMessage() + "; State:" + e.getSQLState();
            throw new BooksServiceException(message, e, new BooksServiceFault(message));
        }
    }

    @WebMethod
    public List<BooksEntity> filter(@WebParam(name = "id") Long id, @WebParam(name = "name") String name,
                                    @WebParam(name = "author") String author,
                                    @WebParam(name = "publicDate") String publicDate, @WebParam(name = "isbn") String isbn) throws BooksServiceException {
        try {
            return booksDAO.filter(id, name, author, getDate(publicDate), isbn);
        } catch (SQLException e) {
            String message = "SQL error: " + e.getMessage() + "; State:" + e.getSQLState();
            throw new BooksServiceException(message, e, new BooksServiceFault(message));
        } catch (ParseException e) {
            String message = "Parse error: " + e.getMessage();
            throw new BooksServiceException(message, e, new BooksServiceFault(message));
        }
    }

    @WebMethod
    public Long create(@WebParam(name = "name") String name,
                       @WebParam(name = "author") String author,
                       @WebParam(name = "publicDate") String publicDate, @WebParam(name = "isbn") String isbn) throws BooksServiceException {
        try {
            return booksDAO.create(name, author, getDate(publicDate), isbn);
        } catch (SQLException e) {
            String message = "SQL error: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new BooksServiceException(message, e, new BooksServiceFault(message));
        } catch (ParseException e) {
            String message = "Parse error: " + e.getMessage();
            throw new BooksServiceException(message, e, new BooksServiceFault(message));
        }
    }

    @WebMethod
    public int delete(@WebParam(name = "id") long id) throws BooksServiceException {
        try {
            int count = booksDAO.delete(id);
            if (count == 0) {
                String message = "Book with id=" + id + " doesn't exist.";
                throw new BooksServiceException(message, new BooksServiceFault(message));
            }
            return count;
        } catch (SQLException e) {
            String message = "SQL error: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new BooksServiceException(message, e, new BooksServiceFault(message));
        }
    }

    @WebMethod
    public int update(@WebParam(name = "id") Long id,
                      @WebParam(name = "name") String name,
                      @WebParam(name = "author") String author,
                      @WebParam(name = "publicDate") String publicDate, @WebParam(name = "isbn") String isbn) throws BooksServiceException {
        try {
            int count = booksDAO.update(id, name, author, getDate(publicDate), isbn);
            if (count == 0) {
                String message = "Book with id=" + id + " doesn't exist.";
                throw new BooksServiceException(message, new BooksServiceFault(message));
            }
            return count;
        } catch (SQLException e) {
            String message = "SQL error: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new BooksServiceException(message, e, new BooksServiceFault(message));
        } catch (ParseException e) {
            String message = "Parse error: " + e.getMessage();
            throw new BooksServiceException(message, e, new BooksServiceFault(message));
        }
    }

    private Date getDate(String string) throws ParseException {
        if (string == null) {
            return null;
        } else {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            return new Date(simpleDateFormat.parse(string).getTime());
        }
    }
}
