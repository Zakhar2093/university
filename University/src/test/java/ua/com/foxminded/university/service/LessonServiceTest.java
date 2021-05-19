package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.RoomRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.model.model_dto.LessonDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class LessonServiceTest {

    private static final String EMPTY_STRING = "";
    private static final LocalDateTime TIME = LocalDateTime.now();

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void createShouldInvokeOnlyOnceWhenTakesLesson() {
        lessonService.create(new Lesson());
        verify(lessonRepository, only()).create(any(Lesson.class));
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesLessonDto() {
        lessonService.create(mockRepository());
        verify(lessonRepository, only()).create(any(Lesson.class));
    }

    private LessonDto mockRepository(){
        LessonDto lessonDto = new LessonDto();
        lessonDto.setDate("11 04 2021 12:44 AM");
        Group group = new Group(1, "Math", false);
        Room room = new Room(1, 101);
        Teacher teacher = new Teacher(1, "one", "two", false);
        when(groupRepository.getById(anyInt())).thenReturn(group);
        when(roomRepository.getById(anyInt())).thenReturn(room);
        when(teacherRepository.getById(anyInt())).thenReturn(teacher);
        return lessonDto;
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        lessonService.getAll();
        verify(lessonRepository, only()).getAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        lessonService.getAllActivated();
        verify(lessonRepository, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        lessonService.getById(1);
        verify(lessonRepository, only()).getById(anyInt());
    }

    @Test
    void getDtoByIdShouldInvokeOnlyOnce() {
        Lesson lesson = new Lesson(1, "Math", null, null, null, LocalDateTime.now(), false);
        when(lessonRepository.getById(anyInt())).thenReturn(lesson);
        lessonService.getDtoById(1);
        verify(lessonRepository, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnceWhenTakesLesson() {
        lessonService.update(new Lesson());
        verify(lessonRepository, only()).update(any(Lesson.class));
    }

    @Test
    void updateShouldInvokeOnlyOnceWhenTakesLessonDto() {
        lessonService.update(mockRepository());
        verify(lessonRepository, only()).update(any(Lesson.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        lessonService.deactivate(1);
        verify(lessonRepository, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        lessonService.activate(1);
        verify(lessonRepository, only()).activate(anyInt());
    }
    
    @Test
    void getLessonByTeacherForDayShouldInvokeOnlyOnce(){
        lessonService.getLessonByTeacherIdForDay(1, TIME);
        verify(lessonRepository, only()).getLessonByTeacherIdForDay(anyInt(), eq(TIME));
    }

    @Test
    void getLessonByTeacherForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByTeacherIdForMonth(1, TIME);
        verify(lessonRepository, only()).getLessonByTeacherIdForMonth(anyInt(), eq(TIME));
    }

    @Test
    void getLessonByStudentForDayShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentIdForDay(1, TIME);
        verify(lessonRepository, only()).getLessonByStudentIdForDay(anyInt(), eq(TIME));
    }

    @Test
    void getLessonByStudentForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentIdForMonth(1, TIME);
        verify(lessonRepository, only()).getLessonByStudentIdForMonth(anyInt(), eq(TIME));
    }

    @Test
    void whenCreateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).create(any(Lesson.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.create(new Lesson());
        });
    }
    
    @Test
    void whenGetAllCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getAll();
        });    
    }
    
    @Test
    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getById(1);
        });
    }
    
    @Test
    void whenUpdateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).update(any(Lesson.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.update(new Lesson());
        });
    }
    
    @Test
    void whenDeactivateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.deactivate(1);
        });
    }
    
    @Test
    void whenActivateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.activate(1);
        });
    }
    
    @Test
    void whenGetLessonByTeacherForDayCatchRepositoryExceptionShouldThrowServiceException(){
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonByTeacherIdForDay(anyInt(), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByTeacherIdForDay(1, TIME);
        });
    }

    @Test
    void whenGetLessonByTeacherForMonthCatchRepositoryExceptionShouldThrowServiceException(){
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonByTeacherIdForMonth(anyInt(), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByTeacherIdForMonth(1, TIME);
        });
    }

    @Test
    void whenGetLessonByStudentForDayCatchRepositoryExceptionShouldThrowServiceException(){
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonByStudentIdForDay(anyInt(), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByStudentIdForDay(1, TIME);
        });
    }

    @Test
    void whenGetLessonByStudentForMonthCatchRepositoryExceptionShouldThrowServiceException(){
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonByStudentIdForMonth(anyInt(), eq(TIME));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByStudentIdForMonth(1, TIME);
        });
    }

    @Test
    void getLessonsByGroupIdShouldInvokeOnlyOnce(){
        lessonService.getLessonsByGroupId(1);
        verify(lessonRepository, only()).getLessonsByGroupId(anyInt());
    }

    @Test
    void getLessonsByTeacherIdShouldInvokeOnlyOnce(){
        lessonService.getLessonsByTeacherId(1);
        verify(lessonRepository, only()).getLessonsByTeacherId(anyInt());
    }

    @Test
    void getLessonsByRoomIdShouldInvokeOnlyOnce(){
        lessonService.getLessonsByRoomId(1);
        verify(lessonRepository, only()).getLessonsByRoomId(anyInt());
    }

    @Test
    void whenGetLessonsByTeacherIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonsByTeacherId(anyInt());
        assertThrows(ServiceException.class, () -> {lessonService.getLessonsByTeacherId(1);});
    }

    @Test
    void whenGetLessonsByGroupIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonsByGroupId(anyInt());
        assertThrows(ServiceException.class, () -> {lessonService.getLessonsByGroupId(1);});
    }

    @Test
    void whenGetLessonsByRoomIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonsByRoomId(anyInt());
        assertThrows(ServiceException.class, () -> { lessonService.getLessonsByRoomId(1);});
    }

}