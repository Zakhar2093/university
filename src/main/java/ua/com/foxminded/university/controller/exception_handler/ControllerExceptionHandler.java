package ua.com.foxminded.university.controller.exception_handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ua.com.foxminded.university.exception.ServiceException;

import javax.validation.ValidationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final String EXCEPTION_VIEW = "errorPage";

    @ExceptionHandler(ValidationException.class)
    public ModelAndView handleViolationException(Exception e) {
        return prepareModel(e, EXCEPTION_VIEW);
    }

    @ExceptionHandler(ServiceException.class)
    public ModelAndView handleServiceException(Exception e) {
        return prepareModel(e, EXCEPTION_VIEW);
    }

    private ModelAndView prepareModel(Exception e, String view) {
        ModelAndView modelView = new ModelAndView(view);
        modelView.addObject("title", e.getClass().getSimpleName());
        modelView.addObject("message", e.getMessage());
        return modelView;
    }
}
