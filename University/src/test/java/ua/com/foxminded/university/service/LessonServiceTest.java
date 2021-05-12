package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.model.model_dto.LessonDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class LessonServiceTest {

    private static final String EMPTY_STRING = "";
    private static final LocalDateTime TIME = LocalDateTime.now();

    @Mock
    private GroupDao groupDao;

    @Mock
    private TeacherDao teacherDao;

    @Mock
    private RoomDao roomDao;

    @Mock
    private LessonDao lessonDao;

    @InjectMocks
    private LessonService lessonService;


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
        lessonService.create(mockDao());
        verify(lessonDao, only()).create(any(Lesson.class));
    }

    private LessonDto mockDao(){
        LessonDto lessonDto = new LessonDto();
        lessonDto.setDate("11 04 2021 12:44 AM");
        Group group = new Group(1, "Math", false);
        Room room = new Room(1, 101);
        Teacher teacher = new Teacher(1, "one", "two", false);
        when(groupDao.getById(anyInt())).thenReturn(group);
        when(roomDao.getById(anyInt())).thenReturn(room);
        when(teacherDao.getById(anyInt())).thenReturn(teacher);
        return lessonDto;
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
        Lesson lesson = new Lesson(1, "Math", null, null, null, LocalDateTime.now(), false);
        when(lessonDao.getById(anyInt())).thenReturn(lesson);
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
        lessonService.update(mockDao());
        verify(lessonDao, only()).update(any(Lesson.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        lessonService.deactivate(1);
        verify(lessonDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        lessonService.activate(1);
        verify(lessonDao, only()).activate(anyInt());
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

    @Test
    void getLessonsByGroupIdShouldInvokeOnlyOnce(){
        lessonService.getLessonsByGroupId(1);
        verify(lessonDao, only()).getLessonsByGroupId(anyInt());
    }

    @Test
    void getLessonsByTeacherIdShouldInvokeOnlyOnce(){
        lessonService.getLessonsByTeacherId(1);
        verify(lessonDao, only()).getLessonsByTeacherId(anyInt());
    }

    @Test
    void getLessonsByRoomIdShouldInvokeOnlyOnce(){
        lessonService.getLessonsByRoomId(1);
        verify(lessonDao, only()).getLessonsByRoomId(anyInt());
    }

    @Test
    void whenGetLessonsByTeacherIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).getLessonsByTeacherId(anyInt());
        assertThrows(ServiceException.class, () -> {lessonService.getLessonsByTeacherId(1);});
    }

    @Test
    void whenGetLessonsByGroupIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).getLessonsByGroupId(anyInt());
        assertThrows(ServiceException.class, () -> {lessonService.getLessonsByGroupId(1);});
    }

    @Test
    void whenGetLessonsByRoomIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(lessonDao).getLessonsByRoomId(anyInt());
        assertThrows(ServiceException.class, () -> { lessonService.getLessonsByRoomId(1);});
    }

}