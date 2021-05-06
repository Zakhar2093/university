package ua.com.foxminded.university;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import ua.com.foxminded.university.config.SpringConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@PropertySource("classpath:application.properties")
@Configuration
@ComponentScan("ua.com.foxminded.university")
@Import(SpringConfig.class)
public class SpringConfigTest {

    @Value("${dataSource.DriverClassName}")
    private String driver;
    @Value("${dataSource.Url}")
    private String url;
    @Value("${dataSource.Username}")
    private String user;
    @Value("${dataSource.Password}")
    private String password;
    @Value("${dataSource.dialectTest}")
    private String dialect;

    @Bean
    public DataSource dataSource() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        createDatabase(dataSource);
        return dataSource;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("hibernate.dialect", dialect);
        return hibernateProperties;
    }

    void createDatabase(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("createTables.sql"));
        }
    }

}
