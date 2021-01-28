package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

class LessonServiceTest {

    private DatabaseInitialization dbInit = new DatabaseInitialization();

    private LessonService lessonService;
    @Mock
    private LessonDao lessonDao;


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        lessonService = new LessonService(lessonDao);
        dbInit.initialization();
    }

    @Test
    void createShouldInvokeOnlyOnce() {
        lessonService.create(new Lesson());
        verify(lessonDao, only()).create(any(Lesson.class));
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        lessonService.getAll();
        verify(lessonDao, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        lessonService.getById(1);
        verify(lessonDao, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnce() {
        lessonService.update(new Lesson());
        verify(lessonDao, only()).update(any(Lesson.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        lessonService.deactivate(1);
        verify(lessonDao, times(1)).removeGroupFromLesson(anyInt());
        verify(lessonDao, times(1)).removeRoomFromLesson(anyInt());
        verify(lessonDao, times(1)).removeTeacherFromLesson(anyInt());
        verify(lessonDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        lessonService.activate(1);
        verify(lessonDao, only()).activate(anyInt());
    }
    
    @Test
    void addGroupToLessonShouldInvokeOnlyOnce(){
        lessonService.addGroupToLesson(1, 1);
        verify(lessonDao, only()).addGroupToLesson(anyInt(), anyInt());
    }    
    
    @Test
    void removeGroupFromLessonShouldInvokeOnlyOnce() {
        lessonService.removeGroupFromLesson(1);;
        verify(lessonDao, only()).removeGroupFromLesson(anyInt());
    }
    
    @Test
    void addRoomToLessonShouldInvokeOnlyOnce() {
        lessonService.addRoomToLesson(1, 1);
        verify(lessonDao, only()).addRoomToLesson(anyInt(), anyInt());
    }
    
    @Test
    void removeRoomFromLessonShouldInvokeOnlyOnce() {
        lessonService.removeRoomFromLesson(1);;
        verify(lessonDao, only()).removeRoomFromLesson(anyInt());
    }
    
    @Test
    void addTeacherToLessonShouldInvokeOnlyOnce() {
        lessonService.addTeacherToLesson(1, 1);
        verify(lessonDao, only()).addTeacherToLesson(anyInt(), anyInt());
    }
    
    @Test
    void removeTeacherFromLessonShouldInvokeOnlyOnce() {
        lessonService.removeTeacherFromLesson(1);;
        verify(lessonDao, only()).removeTeacherFromLesson(anyInt());
    }
    
    @Test
    void getLessonByTeacherForDayShouldInvokeOnlyOnce(){
//        List<String> list = mock(List.class);
        lessonService.getLessonByTeacherForDay(new Teacher(), LocalDateTime.now());
//        when(lessonDao.getLessonByTeacherForDay(any(Teacher.class), LocalDateTime.now())).thenReturn(list)));
        verify(lessonDao, only()).getLessonByTeacherForDay(any(Teacher.class), LocalDateTime.now());
    }

    @Test
    void getLessonByTeacherForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByTeacherForMonth(new Teacher(), LocalDateTime.now());
        verify(lessonDao, only()).getLessonByTeacherForMonth(any(Teacher.class), LocalDateTime.now());
    }

    @Test
    void getLessonByStudentForDayShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentForDay(new Student(), LocalDateTime.now());
        verify(lessonDao, only()).getLessonByStudentForDay(any(Student.class), LocalDateTime.now());
    }

    @Test
    void getLessonByStudentForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentForMonth(new Student(), LocalDateTime.now());
        verify(lessonDao, only()).getLessonByStudentForMonth(any(Student.class), LocalDateTime.now());
    }
}
