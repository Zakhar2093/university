package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.repository.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void createShouldInvokeOnlyOnceWhenTakesLesson() {
        lessonService.create(new Lesson());
        verify(lessonRepository, only()).save(any(Lesson.class));
    }

    @Test
    void createShouldInvokeOnlyOnceWhenTakesLessonDto() {
        lessonService.create(createLessonDto());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    private LessonDto createLessonDto(){
        LessonDto lessonDto = new LessonDto(1, "1", 1, 1, 1, "1", true);
        lessonDto.setDate("11 04 2021 12:44 AM");
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        when(roomRepository.findById(1)).thenReturn(Optional.of(new Room()));
        when(teacherRepository.findById(1)).thenReturn(Optional.of(new Teacher()));
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
        when(lessonRepository.findById(1)).thenReturn(Optional.of(new Lesson()));
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
        lessonService.update(createLessonDto());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void deactivateShouldInvokeOnlyOnce() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(new Lesson()));
        lessonService.deactivate(1);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
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
    void whenGetByIdTakesUnexcitedIdShouldThrowServiceException() {
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Lesson with such id 1 does not exist"));
    }

    @Test
    void whenGetLessonByStudentIdForDayTakesUnexcitedIdShouldThrowServiceException() {
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByStudentIdForDay(1, eq(TIME));
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 does not exist"));
    }

    @Test
    void whenGetLessonByStudentIdForMonthTakesUnexcitedIdShouldThrowServiceException() {
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.getLessonByStudentIdForMonth(1, eq(TIME));
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 does not exist"));
    }
}