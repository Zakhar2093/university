package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Student;

class StudentServiceTest {

    private StudentService studentService;
    
    @Mock
    private StudentDao studentDao;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentService(studentDao);
    }

    @Test
    void createShouldInvokeOnlyOnce() {
        studentService.create(new Student());
        verify(studentDao, only()).create(any(Student.class));
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        studentService.getAll();
        verify(studentDao, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        studentService.getById(1);
        verify(studentDao, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnce() {
        studentService.update(new Student());
        verify(studentDao, only()).update(any(Student.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        studentService.deactivate(1);
        verify(studentDao, times(1)).removeStudentFromGroup(anyInt());
        verify(studentDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        studentService.activate(1);
        verify(studentDao, only()).activate(anyInt());
    }
    
    @Test
    void addStudentToGroupShouldInvokeOnlyOnce() {
        studentService.addStudentToGroup(1, 1);
        verify(studentDao, only()).addStudentToGroup(anyInt(), anyInt());
    }
    
    @Test
    void removeStudentFromGroupShouldInvokeOnlyOnce() {
        studentService.removeStudentFromGroup(1);
        verify(studentDao, only()).removeStudentFromGroup(anyInt());
    }
    
    @Test
    void WhenCreateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(studentDao).create(any(Student.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.create(new Student());
        });
    }
    
    @Test
    void WhenGetAllCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(studentDao).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.getAll();
        });    
    }
    
    @Test
    void WhenGetByIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(studentDao).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.getById(1);
        });
    }
    
    @Test
    void WhenUpdateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(studentDao).update(any(Student.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.update(new Student());
        });
    }
    
    @Test
    void WhenDeactivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(studentDao).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.deactivate(1);
        });
    }
    
    @Test
    void WhenActivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(studentDao).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.activate(1);
        });
    }
    
    @Test
    void WhenAddStudentToGroupCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(studentDao).addStudentToGroup(anyInt(), anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.addStudentToGroup(1, 1);
        });
    }
    
    @Test
    void WhenRemoveStudentFromGroupCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(studentDao).removeStudentFromGroup(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.removeStudentFromGroup(1);
        });
    }
}