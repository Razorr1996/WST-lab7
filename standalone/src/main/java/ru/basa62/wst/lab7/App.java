package ru.basa62.wst.lab7;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.basa62.wst.lab7.util.Configuration;
import ru.basa62.wst.lab7.ws.BooksService;
import ru.basa62.wst.lab7.ws.TestService;

import javax.sql.DataSource;
import javax.xml.ws.Endpoint;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class App {
    public static void main(String[] args) {
        Configuration conf = new Configuration("config.properties");
        String scheme = conf.get("scheme", "http");
        String host = conf.get("host", "localhost");
        String port = conf.get("port", "8081");
        String baseUrl = scheme + "://" + host + ":" + port;

        String booksName = conf.get("books.name", "Books");
        String testName = conf.get("test.name", "Test");

        String booksUrl = baseUrl + "/" + booksName;
        String testUrl = baseUrl + "/" + testName;

        DataSource dataSource = initDataSource();

        log.info("Starting");
        Endpoint.publish(testUrl, new TestService());
        Endpoint.publish(booksUrl, new BooksService(new BooksDAO(dataSource)));
        log.info("All started");
    }

    @SneakyThrows
    private static DataSource initDataSource() {
        InputStream inputStream = App.class.getClassLoader().getResourceAsStream("datasource.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        HikariConfig hikariConfig = new HikariConfig(properties);
        return new HikariDataSource(hikariConfig);
    }
}
