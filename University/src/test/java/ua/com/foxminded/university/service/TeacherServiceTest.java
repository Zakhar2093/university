package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Teacher;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class TeacherServiceTest {

    private static final String EMPTY_STRING = "";

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void createShouldInvokeOnlyOnce() {
        teacherService.create(new Teacher());
        verify(teacherRepository, only()).create(any(Teacher.class));
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        teacherService.getAll();
        verify(teacherRepository, only()).getAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        teacherService.getAllActivated();
        verify(teacherRepository, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        teacherService.getById(1);
        verify(teacherRepository, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnce() {
        teacherService.update(new Teacher());
        verify(teacherRepository, only()).update(any(Teacher.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        teacherService.deactivate(1);
        verify(teacherRepository, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        teacherService.activate(1);
        verify(teacherRepository, only()).activate(anyInt());
    }
    
    @Test
    void whenCreateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(teacherRepository).create(any(Teacher.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.create(new Teacher());
        });
    }
    
    @Test
    void whenGetAllCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(teacherRepository).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.getAll();
        });    
    }
    
    @Test
    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(teacherRepository).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.getById(1);
        });
    }
    
    @Test
    void whenUpdateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(teacherRepository).update(any(Teacher.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.update(new Teacher());
        });
    }
    
    @Test
    void whenDeactivateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(teacherRepository).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.deactivate(1);
        });
    }
    
    @Test
    void whenActivateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(teacherRepository).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.activate(1);
        });
    }
}