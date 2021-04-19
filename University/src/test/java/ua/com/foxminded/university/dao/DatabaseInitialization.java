package ua.com.foxminded.university.dao;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseInitialization {
    
    public void initialization() {
        String url = "jdbc:h2:~/university";
        String login = "sa";
        String password = "";
        try (Connection connection = DriverManager.getConnection(url, login, password);) {
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader(new File("src/test/resources/createTables.sql")));
            sr.runScript(reader);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
