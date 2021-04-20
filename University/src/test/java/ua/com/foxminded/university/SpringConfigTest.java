package ua.com.foxminded.university;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;
import javax.sql.DataSource;

@PropertySource("classpath:application.properties")
@Configuration
@ComponentScan("ua.com.foxminded.university")
public class SpringConfigTest {

    @Value("${dataSource.JNDI_JDBC_H2}")
    private String JNDI_JDBC_H2;

    @Bean
    public DataSource dataSourse() throws NamingException {
        return (DataSource) new JndiTemplate().lookup(JNDI_JDBC_H2);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws NamingException {
        return new JdbcTemplate(dataSourse());
    }

}
