package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.RoomRepository;
import ua.com.foxminded.university.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
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
        verify(lessonRepository, only()).save(any(Lesson.class));
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesLessonDto() {
        lessonService.create(mockRepository());
        verify(lessonRepository, only()).save(any(Lesson.class));
    }

    private LessonDto mockRepository(){
        LessonDto lessonDto = new LessonDto();
        lessonDto.setDate("11 04 2021 12:44 AM");
        Group group = new Group(1, "Math", false);
        Room room = new Room(1, 101);
        Teacher teacher = new Teacher(1, "one", "two", false);
        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(group));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacher));
        return lessonDto;
    }

    @Test
    void getAllShouldInvokeOnlyOnce() {
        lessonService.getAll();
        verify(lessonRepository, only()).findAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        lessonService.getAllActivated();
        verify(lessonRepository, only()).findAll();
    }

    @Test
    void getByIdShouldInvokeOnlyOnce() {
        lessonService.getById(1);
        verify(lessonRepository, only()).findById(anyInt());
    }

    @Test
    void getDtoByIdShouldInvokeOnlyOnce() {
        Lesson lesson = new Lesson(1, "Math", null, null, null, LocalDateTime.now(), false);
        when(lessonRepository.findById(anyInt())).thenReturn(Optional.of(lesson));
        lessonService.getDtoById(1);
        verify(lessonRepository, only()).findById(anyInt());
    }

    @Test
    void updateShouldInvokeOnlyOnceWhenTakesLesson() {
        lessonService.update(new Lesson());
        verify(lessonRepository, only()).save(any(Lesson.class));
    }

    @Test
    void updateShouldInvokeOnlyOnceWhenTakesLessonDto() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(new Lesson()));
        lessonService.update(mockRepository());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void deactivateShouldInvokeOnlyOnce() {
        lessonService.deactivate(1);
        verify(lessonRepository, times(1)).deactivate(anyInt());
    }

    @Test
    void activateShouldInvokeOnlyOnce() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(new Lesson()));
        lessonService.activate(1);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void getLessonByTeacherForDayShouldInvokeOnlyOnce(){
        lessonService.getLessonByTeacherIdForDay(1, TIME);
        verify(lessonRepository, only()).getLessonByTeacherIdForDay(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void getLessonByTeacherForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByTeacherIdForMonth(1, TIME);
        verify(lessonRepository, only()).getLessonByTeacherIdForMonth(anyInt(), anyInt(), anyInt());
    }

    @Test
    void getLessonByStudentForDayShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentIdForDay(1, TIME);
        verify(lessonRepository, only()).getLessonByGroupIdForDay(any(Group.class), anyInt(), anyInt(), anyInt());
    }

    @Test
    void getLessonByStudentForMonthShouldInvokeOnlyOnce(){
        lessonService.getLessonByStudentIdForMonth(1, TIME);
        verify(lessonRepository, only()).getLessonByGroupIdForMonth(any(Group.class), anyInt(), anyInt());
    }

    @Test
    void whenCreateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).save(any(Lesson.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.create(new Lesson());
        });
    }

    @Test
    void whenGetAllCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).findAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getAll();
        });
    }

    @Test
    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).findById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getById(1);
        });
    }

    @Test
    void whenUpdateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).save(any(Lesson.class));
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
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).save(any(Lesson.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.activate(1);
        });
    }

    @Test
    void whenGetLessonByTeacherForDayCatchRepositoryExceptionShouldThrowServiceException(){
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonByTeacherIdForDay(anyInt(), anyInt(), anyInt(), anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByTeacherIdForDay(1, TIME);
        });
    }

    @Test
    void whenGetLessonByTeacherForMonthCatchRepositoryExceptionShouldThrowServiceException(){
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonByTeacherIdForMonth(anyInt(), anyInt(), anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByTeacherIdForMonth(1, TIME);
        });
    }
// todo tests
//    @Test
//    void whenGetLessonByStudentForDayCatchRepositoryExceptionShouldThrowServiceException(){
//        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonByGroupIdForDay(any(Group.class), anyInt(), anyInt(), anyInt());
//        ServiceException thrown = assertThrows(ServiceException.class, () -> {
//            lessonService.getLessonByStudentIdForDay(1, TIME);
//        });
//    }
//
//    @Test
//    void whenGetLessonByStudentForMonthCatchRepositoryExceptionShouldThrowServiceException(){
//        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).getLessonByGroupIdForMonth(any(Group.class), anyInt(), anyInt());
//        ServiceException thrown = assertThrows(ServiceException.class, () -> {
//            lessonService.getLessonByStudentIdForMonth(1, TIME);
//        });
//    }

    @Test
    void getLessonsByGroupIdShouldInvokeOnlyOnce(){
        lessonService.getLessonsByGroupId(1);
        verify(lessonRepository, only()).findByGroupGroupId(anyInt());
    }

    @Test
    void getLessonsByTeacherIdShouldInvokeOnlyOnce(){
        lessonService.getLessonsByTeacherId(1);
        verify(lessonRepository, only()).findByTeacherTeacherId(anyInt());
    }

    @Test
    void getLessonsByRoomIdShouldInvokeOnlyOnce(){
        lessonService.getLessonsByRoomId(1);
        verify(lessonRepository, only()).findByRoomRoomId(anyInt());
    }

    @Test
    void whenGetLessonsByTeacherIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).findByTeacherTeacherId(anyInt());
        assertThrows(ServiceException.class, () -> {lessonService.getLessonsByTeacherId(1);});
    }

    @Test
    void whenGetLessonsByGroupIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).findByGroupGroupId(anyInt());
        assertThrows(ServiceException.class, () -> {lessonService.getLessonsByGroupId(1);});
    }

    @Test
    void whenGetLessonsByRoomIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(lessonRepository).findByRoomRoomId(anyInt());
        assertThrows(ServiceException.class, () -> { lessonService.getLessonsByRoomId(1);});
    }
}