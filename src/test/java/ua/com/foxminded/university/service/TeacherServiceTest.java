package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.repository.TeacherRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void saveShouldInvokeOnlyOnce() {
        teacherService.save(new Teacher());
        verify(teacherRepository, only()).save(any(Teacher.class));
    }

    @Test
    void findAllActivatedShouldInvokeOnlyOnce() {
        teacherService.findAll();
        verify(teacherRepository, only()).findAll();
    }

    @Test
    void findByIdShouldInvokeOnlyOnce() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(new Teacher()));
        teacherService.findById(1);
        verify(teacherRepository, only()).findById(anyInt());
    }

    @Test
    void deactivateShouldInvokeOnlyOnce() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(new Teacher()));
        teacherService.deactivate(1);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
        verify(teacherRepository, times(1)).removeTeacherFromAllLessons(anyInt());
    }

    @Test
    void activateShouldInvokeOnlyOnce() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(new Teacher()));
        teacherService.activate(1);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            teacherService.findById(1);
        });
        assertTrue(thrown.getMessage().contains("Teacher with such id 1 does not exist"));
    }
}