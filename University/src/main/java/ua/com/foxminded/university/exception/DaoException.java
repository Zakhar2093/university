package ua.com.foxminded.university.exception;

public class DaoException extends RuntimeException { 
    public DaoException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    
    public DaoException(String errorMessage) {
        super(errorMessage);
    }
    
}