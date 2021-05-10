package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Teacher;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    private static final String EMPTY_STRING = "";
    private TeacherService teacherService;
    @Mock
    private TeacherDao teacherDao;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        teacherService = new TeacherService(teacherDao);
    }

    @Test
    void createShouldInvokeOnlyOnce() {
        teacherService.create(new Teacher());
        verify(teacherDao, only()).create(any(Teacher.class));
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        teacherService.getAll();
        verify(teacherDao, only()).getAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        teacherService.getAllActivated();
        verify(teacherDao, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        teacherService.getById(1);
        verify(teacherDao, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnce() {
        teacherService.update(new Teacher());
        verify(teacherDao, only()).update(any(Teacher.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        teacherService.deactivate(1);
        verify(teacherDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        teacherService.activate(1);
        verify(teacherDao, only()).activate(anyInt());
    }
    
    @Test
    void whenCreateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(teacherDao).create(any(Teacher.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.create(new Teacher());
        });
    }
    
    @Test
    void whenGetAllCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(teacherDao).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.getAll();
        });    
    }
    
    @Test
    void whenGetByIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(teacherDao).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.getById(1);
        });
    }
    
    @Test
    void whenUpdateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(teacherDao).update(any(Teacher.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.update(new Teacher());
        });
    }
    
    @Test
    void whenDeactivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(teacherDao).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.deactivate(1);
        });
    }
    
    @Test
    void whenActivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(teacherDao).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.activate(1);
        });
    }
}