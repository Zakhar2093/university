package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.model.Teacher;

class TeacherServiceTest {

    private DatabaseInitialization dbInit = new DatabaseInitialization();

    private TeacherService teacherService;
    @Mock
    private TeacherDao teacherDao;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        teacherService = new TeacherService(teacherDao);
        dbInit.initialization();
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
}
