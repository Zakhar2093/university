package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
class StudentServiceTest {

    private static final String EMPTY_STRING = "";

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void createShouldInvokeOnlyOnceWhenTakesStudent() {
        studentService.create(new Student());
        verify(studentRepository, only()).save(any(Student.class));
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesStudentDto() {
        studentService.create(createStudentDto());
        verify(studentRepository, only()).save(any(Student.class));
    }

    private StudentDto createStudentDto(){
        StudentDto studentDto = new StudentDto(1, "1", "1", 1, true);
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        return studentDto;
    }

    @Test
    void getAllShouldInvokeOnlyOnce() {
        studentService.getAll();
        verify(studentRepository, only()).findAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        studentService.getAllActivated();
        verify(studentRepository, only()).findAll();
    }

    @Test
    void getByIdShouldInvokeOnlyOnce() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(new Student()));
        studentService.getById(1);
        verify(studentRepository, only()).findById(anyInt());
    }

    @Test
    void getDtoByIdShouldInvokeOnlyOnce() {
        Student student = new Student(1, "one", "two", new Group(), false);
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));
        studentService.getDtoById(1);
        verify(studentRepository, only()).findById(anyInt());
    }

    @Test
    void updateShouldInvokeOnlyOnceWhenTakesStudent() {
        studentService.update(new Student());
        verify(studentRepository, only()).save(any(Student.class));
    }

    @Test
    void updateShouldInvokeOnlyOnceWhenTakesStudentDto() {
        studentService.update(createStudentDto());
        verify(studentRepository, only()).save(any(Student.class));
    }

    @Test
    void deactivateShouldInvokeOnlyOnce() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(new Student()));
        studentService.deactivate(1);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void activateShouldInvokeOnlyOnce() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(new Student()));
        studentService.activate(1);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void getStudentsByGroupIdShouldInvokeOnlyOnce() {
        studentService.getStudentsByGroupId(1);
        verify(studentRepository, only()).findByGroupGroupId(anyInt());
    }

    @Test
    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 does not exist"));
    }
}