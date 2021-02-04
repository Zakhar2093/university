package ua.com.foxminded.university.exception;

public class ServiceException extends RuntimeException {

    public ServiceException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    
    public ServiceException(String errorMessage) {
        super(errorMessage);
    }
    
    public ServiceException(Throwable err) {
        super(err);
    }
}
