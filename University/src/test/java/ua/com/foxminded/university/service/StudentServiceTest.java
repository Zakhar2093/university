package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Student;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private static final String EMPTY_STRING = "";
    private StudentService studentService;

    @Mock
    private GroupRepository groupDao;
    
    @Mock
    private StudentRepository studentDao;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentService(studentDao, groupDao);
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesStudent() {
        studentService.create(new Student());
        verify(studentDao, only()).create(any(Student.class));
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesStudentDto() {
        studentService.create(mockGroup());
        verify(studentDao, only()).create(any(Student.class));
    }

    private StudentDto mockGroup(){
        StudentDto studentDto = new StudentDto();
        Group group = new Group(1, "Math", false);
        when(groupDao.getById(anyInt())).thenReturn(group);
        return studentDto;
    }

    @Test
    void getAllShouldInvokeOnlyOnce() {
        studentService.getAll();
        verify(studentDao, only()).getAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        studentService.getAllActivated();
        verify(studentDao, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        studentService.getById(1);
        verify(studentDao, only()).getById(anyInt());
    }

    @Test
    void getStudentsByGroupIdShouldInvokeOnlyOnce() {
        studentService.getStudentsByGroupId(1);
        verify(studentDao, only()).getStudentsByGroupId(anyInt());
    }

    @Test
    void whenGetStudentsByGroupIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentDao).getStudentsByGroupId(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {studentService.getStudentsByGroupId(1); });
    }

    @Test
    void getDtoByIdShouldInvokeOnlyOnce() {
        Student student = new Student(1, "one", "two", new Group(), false);
        when(studentDao.getById(anyInt())).thenReturn(student);
        studentService.getDtoById(1);
        verify(studentDao, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnceWhenTakesStudent() {
        studentService.update(new Student());
        verify(studentDao, only()).update(any(Student.class));
    }

    @Test
    void updateShouldInvokeOnlyOnceWhenTakesStudentDto() {
        studentService.update(mockGroup());
        verify(studentDao, only()).update(any(Student.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        studentService.deactivate(1);
        verify(studentDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        studentService.activate(1);
        verify(studentDao, only()).activate(anyInt());
    }

    @Test
    void whenCreateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentDao).create(any(Student.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.create(new Student());
        });
    }
    
    @Test
    void whenGetAllCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentDao).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.getAll();
        });    
    }
    
    @Test
    void whenGetByIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentDao).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.getById(1);
        });
    }
    
    @Test
    void whenUpdateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentDao).update(any(Student.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.update(new Student());
        });
    }
    
    @Test
    void whenDeactivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentDao).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.deactivate(1);
        });
    }
    
    @Test
    void whenActivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentDao).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.activate(1);
        });
    }
}