package ru.basa62.wst.lab7.beans;

import lombok.Data;
import ru.basa62.wst.lab7.BooksDAO;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@Data
@ApplicationScoped
public class DAOFactory {
    @Resource(lookup = "jdbc/ifmo-ws")
    private DataSource dataSource;

    @Produces
    public BooksDAO booksDAO() {
        return new BooksDAO(dataSource);
    }
}
