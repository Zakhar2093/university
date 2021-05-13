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
    private GroupRepository groupRepository;
    
    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentService(studentRepository, groupRepository);
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesStudent() {
        studentService.create(new Student());
        verify(studentRepository, only()).create(any(Student.class));
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesStudentDto() {
        studentService.create(mockGroup());
        verify(studentRepository, only()).create(any(Student.class));
    }

    private StudentDto mockGroup(){
        StudentDto studentDto = new StudentDto();
        Group group = new Group(1, "Math", false);
        when(groupRepository.getById(anyInt())).thenReturn(group);
        return studentDto;
    }

    @Test
    void getAllShouldInvokeOnlyOnce() {
        studentService.getAll();
        verify(studentRepository, only()).getAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        studentService.getAllActivated();
        verify(studentRepository, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        studentService.getById(1);
        verify(studentRepository, only()).getById(anyInt());
    }

    @Test
    void getStudentsByGroupIdShouldInvokeOnlyOnce() {
        studentService.getStudentsByGroupId(1);
        verify(studentRepository, only()).getStudentsByGroupId(anyInt());
    }

    @Test
    void whenGetStudentsByGroupIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentRepository).getStudentsByGroupId(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {studentService.getStudentsByGroupId(1); });
    }

    @Test
    void getDtoByIdShouldInvokeOnlyOnce() {
        Student student = new Student(1, "one", "two", new Group(), false);
        when(studentRepository.getById(anyInt())).thenReturn(student);
        studentService.getDtoById(1);
        verify(studentRepository, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnceWhenTakesStudent() {
        studentService.update(new Student());
        verify(studentRepository, only()).update(any(Student.class));
    }

    @Test
    void updateShouldInvokeOnlyOnceWhenTakesStudentDto() {
        studentService.update(mockGroup());
        verify(studentRepository, only()).update(any(Student.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        studentService.deactivate(1);
        verify(studentRepository, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        studentService.activate(1);
        verify(studentRepository, only()).activate(anyInt());
    }

    @Test
    void whenCreateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentRepository).create(any(Student.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.create(new Student());
        });
    }
    
    @Test
    void whenGetAllCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentRepository).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.getAll();
        });    
    }
    
    @Test
    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentRepository).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.getById(1);
        });
    }
    
    @Test
    void whenUpdateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentRepository).update(any(Student.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.update(new Student());
        });
    }
    
    @Test
    void whenDeactivateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentRepository).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.deactivate(1);
        });
    }
    
    @Test
    void whenActivateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(studentRepository).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.activate(1);
        });
    }
}