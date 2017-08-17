package config;

import dao.XUsersJdbcDao;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by sergeybp on 20.07.17.
 */
public class JdbcDaoContextConfiguration {

    public XUsersJdbcDao xusersJdbcDao() {
        return new XUsersJdbcDao(this.dataSource());
    }

    private DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:xusers.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}