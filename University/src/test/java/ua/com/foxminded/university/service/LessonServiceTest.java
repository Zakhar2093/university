package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

class LessonServiceTest {

    private static final LocalDateTime TIME = LocalDateTime.now();
    private LessonService lessonService;
    @Mock
    private LessonDao lessonDao;


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        lessonService = new LessonService(lessonDao);
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
        lessonService.getLessonByTeacherForDay(new Teacher(), TIME);
        verify(lessonDao, only()).getLessonByTeacherForDay(any(Teacher.class), eq(TIME));
    }

    @Test
    void getLessonByTeacherForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByTeacherForMonth(new Teacher(), TIME);
        verify(lessonDao, only()).getLessonByTeacherForMonth(any(Teacher.class), eq(TIME));
    }

    @Test
    void getLessonByStudentForDayShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentForDay(new Student(), TIME);
        verify(lessonDao, only()).getLessonByStudentForDay(any(Student.class), eq(TIME));
    }

    @Test
    void getLessonByStudentForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentForMonth(new Student(), TIME);
        verify(lessonDao, times(1)).getLessonByStudentForMonth(any(Student.class), eq(TIME));
    }

    @Test
    void WhenCreateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).create(any(Lesson.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.create(new Lesson());
        });
    }
    
    @Test
    void WhenGetAllCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getAll();
        });    
    }
    
    @Test
    void WhenGetByIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getById(1);
        });
    }
    
    @Test
    void WhenUpdateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).update(any(Lesson.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.update(new Lesson());
        });
    }
    
    @Test
    void WhenDeactivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.deactivate(1);
        });
    }
    
    @Test
    void WhenActivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.activate(1);
        });
    }
    
    @Test
    void WhenAddGroupToLessonCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException("")).when(lessonDao).addGroupToLesson(anyInt(), anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.addGroupToLesson(1, 1);
        });
    }     
    
    @Test
    void WhenRemoveGroupFromLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).removeGroupFromLesson(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.removeGroupFromLesson(1);;
        });
    }
    
    @Test
    void WhenAddRoomToLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).addRoomToLesson(anyInt(), anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.addRoomToLesson(1, 1);
        });
    }
    
    @Test
    void WhenRemoveRoomFromLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).removeRoomFromLesson(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.removeRoomFromLesson(1);;
        });
    }
    
    @Test
    void WhenAddTeacherToLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).addTeacherToLesson(anyInt(), anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.addTeacherToLesson(1, 1);
        });
    }
    
    @Test
    void WhenRemoveTeacherFromLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException("")).when(lessonDao).removeTeacherFromLesson(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.removeTeacherFromLesson(1);;
        });
    }
    
    @Test
    void WhenGetLessonByTeacherForDayCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException("")).when(lessonDao).getLessonByTeacherForDay(any(Teacher.class), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByTeacherForDay(new Teacher(), TIME);
        });
    }

    @Test
    void WhenGetLessonByTeacherForMonthCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException("")).when(lessonDao).getLessonByTeacherForMonth(any(Teacher.class), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByTeacherForMonth(new Teacher(), TIME);
        });
    }

    @Test
    void WhenGetLessonByStudentForDayCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException("")).when(lessonDao).getLessonByStudentForDay(any(Student.class), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByStudentForDay(new Student(), TIME);
        });
    }

    @Test
    void WhenGetLessonByStudentForMonthCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException("")).when(lessonDao).getLessonByStudentForMonth(any(Student.class), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByStudentForMonth(new Student(), TIME);
        });
    }
}