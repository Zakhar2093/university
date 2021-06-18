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

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void saveShouldInvokeOnlyOnceWhenTakesStudent() {
        studentService.save(new Student());
        verify(studentRepository, only()).save(any(Student.class));
    }

    @Test
    void saveShouldInvokeOnlyOnceWhenTakesStudentDto() {
        studentService.save(createStudentDto());
        verify(studentRepository, only()).save(any(Student.class));
    }

    private StudentDto createStudentDto(){
        StudentDto studentDto = new StudentDto(1, "1", "1", 1, true);
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        return studentDto;
    }

    @Test
    void findAllShouldInvokeOnlyOnce() {
        studentService.findAll();
        verify(studentRepository, only()).findAll();
    }

    @Test
    void findAllDtoShouldInvokeOnlyOnce() {
        studentService.findAllDto();
        verify(studentRepository, only()).findAll();
    }

    @Test
    void findByIdShouldInvokeOnlyOnce() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(new Student()));
        studentService.findById(1);
        verify(studentRepository, only()).findById(anyInt());
    }

    @Test
    void findDtoByIdShouldInvokeOnlyOnce() {
        Student student = new Student(1, "one", "two", new Group(), false);
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));
        studentService.findDtoById(1);
        verify(studentRepository, only()).findById(anyInt());
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
    void getStudentsDtoByGroupIdShouldInvokeOnlyOnce() {
        studentService.getStudentsDtoByGroupId(1);
        verify(studentRepository, only()).findByGroupGroupId(anyInt());
    }

    @Test
    void whenGetByIdGetUnexistIdShouldThrowServiceException() {
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.findById(1);
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 does not exist"));
    }

    @Test
    void whenDtoWithGroupNullMapToStudentShouldThrowServiceException(){
        Optional<Group> optional = Optional.empty();
        when(groupRepository.findById(1)).thenReturn(optional);
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            studentService.save(new StudentDto(1, "Josh", "Smith", 1, false));
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 does not exist"));
    }
}