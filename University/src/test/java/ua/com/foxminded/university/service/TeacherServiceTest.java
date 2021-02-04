package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Teacher;

class TeacherServiceTest {

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
        verify(teacherDao, times(1)).removeTeacherFromAllLessons(anyInt());
        verify(teacherDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        teacherService.activate(1);
        verify(teacherDao, only()).activate(anyInt());
    }
    
    @Test
    void getTeacherByLessonShouldInvokeOnlyOnce() {
        teacherService.getTeacherByLesson(1);
        verify(teacherDao, only()).getTeacherByLesson(anyInt());
    }
    
    @Test
    void WhenCreateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(teacherDao).create(any(Teacher.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.create(new Teacher());
        });
    }
    
    @Test
    void WhenGetAllCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(teacherDao).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.getAll();
        });    
    }
    
    @Test
    void WhenGetByIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(teacherDao).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.getById(1);
        });
    }
    
    @Test
    void WhenUpdateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(teacherDao).update(any(Teacher.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.update(new Teacher());
        });
    }
    
    @Test
    void WhenDeactivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(teacherDao).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.deactivate(1);
        });
    }
    
    @Test
    void WhenActivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(teacherDao).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.activate(1);
        });
    }
    
    @Test
    void WhenGetTeacherByLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(teacherDao).getTeacherByLesson(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.getTeacherByLesson(1);
        });
    }
}