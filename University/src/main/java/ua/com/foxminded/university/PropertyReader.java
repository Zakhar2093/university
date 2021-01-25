package ua.com.foxminded.university;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class PropertyReader {
    
    public String read(String fullPropertyFileName, String key) {
        Properties property = new Properties();
        try {
            FileInputStream fis = new FileInputStream(fullPropertyFileName);
            property.load(fis);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property.getProperty(key);
    }
}