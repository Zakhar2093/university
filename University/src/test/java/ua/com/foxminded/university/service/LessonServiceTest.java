package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.model_dto.LessonDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class LessonServiceTest {

    private static final String EMPTY_STRING = "";
    private static final LocalDateTime TIME = LocalDateTime.now();
    private LessonService lessonService;
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private RoomDao roomDao;

    @Mock
    private LessonDao lessonDao;


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        lessonService = new LessonService(lessonDao, groupDao, teacherDao, roomDao);
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesLesson() {
        lessonService.create(new Lesson());
        verify(lessonDao, only()).create(any(Lesson.class));
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesLessonDto() {
        lessonService.create(new LessonDto());
        verify(lessonDao, only()).create(any(Lesson.class));
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        lessonService.getAll();
        verify(lessonDao, only()).getAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        lessonService.getAllActivated();
        verify(lessonDao, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        lessonService.getById(1);
        verify(lessonDao, only()).getById(anyInt());
    }

    @Test
    void getDtoByIdShouldInvokeOnlyOnce() {
        lessonService.getDtoById(1);
        verify(lessonDao, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnceWhenTakesLesson() {
        lessonService.update(new Lesson());
        verify(lessonDao, only()).update(any(Lesson.class));
    }

    @Test
    void updateShouldInvokeOnlyOnceWhenTakesLessonDto() {
        lessonService.update(new LessonDto());
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
        lessonService.getLessonByTeacherIdForDay(1, TIME);
        verify(lessonDao, only()).getLessonByTeacherIdForDay(anyInt(), eq(TIME));
    }

    @Test
    void getLessonByTeacherForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByTeacherIdForMonth(1, TIME);
        verify(lessonDao, only()).getLessonByTeacherIdForMonth(anyInt(), eq(TIME));
    }

    @Test
    void getLessonByStudentForDayShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentIdForDay(1, TIME);
        verify(lessonDao, only()).getLessonByStudentIdForDay(anyInt(), eq(TIME));
    }

    @Test
    void getLessonByStudentForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentIdForMonth(1, TIME);
        verify(lessonDao, only()).getLessonByStudentIdForMonth(anyInt(), eq(TIME));
    }

    @Test
    void whenCreateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).create(any(Lesson.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.create(new Lesson());
        });
    }
    
    @Test
    void whenGetAllCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getAll();
        });    
    }
    
    @Test
    void whenGetByIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getById(1);
        });
    }
    
    @Test
    void whenUpdateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).update(any(Lesson.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.update(new Lesson());
        });
    }
    
    @Test
    void whenDeactivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.deactivate(1);
        });
    }
    
    @Test
    void whenActivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.activate(1);
        });
    }
    
    @Test
    void whenAddGroupToLessonCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).addGroupToLesson(anyInt(), anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.addGroupToLesson(1, 1);
        });
    }     
    
    @Test
    void whenRemoveGroupFromLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).removeGroupFromLesson(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.removeGroupFromLesson(1);;
        });
    }
    
    @Test
    void whenAddRoomToLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).addRoomToLesson(anyInt(), anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.addRoomToLesson(1, 1);
        });
    }
    
    @Test
    void whenRemoveRoomFromLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).removeRoomFromLesson(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.removeRoomFromLesson(1);;
        });
    }
    
    @Test
    void whenAddTeacherToLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).addTeacherToLesson(anyInt(), anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.addTeacherToLesson(1, 1);
        });
    }
    
    @Test
    void whenRemoveTeacherFromLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).removeTeacherFromLesson(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.removeTeacherFromLesson(1);;
        });
    }
    
    @Test
    void whenGetLessonByTeacherForDayCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).getLessonByTeacherIdForDay(anyInt(), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByTeacherIdForDay(1, TIME);
        });
    }

    @Test
    void whenGetLessonByTeacherForMonthCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).getLessonByTeacherIdForMonth(anyInt(), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByTeacherIdForMonth(1, TIME);
        });
    }

    @Test
    void whenGetLessonByStudentForDayCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).getLessonByStudentIdForDay(anyInt(), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByStudentIdForDay(1, TIME);
        });
    }

    @Test
    void whenGetLessonByStudentForMonthCatchDaoExceptionShouldThrowServiceException(){
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).getLessonByStudentIdForMonth(anyInt(), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByStudentIdForMonth(1, TIME);
        });
    }
}