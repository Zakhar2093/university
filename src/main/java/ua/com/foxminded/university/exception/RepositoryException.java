package ua.com.foxminded.university.exception;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    
    public RepositoryException(String errorMessage) {
        super(errorMessage);
    }

}