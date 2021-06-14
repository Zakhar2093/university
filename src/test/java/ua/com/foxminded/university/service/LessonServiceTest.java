package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.repository.*;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
class LessonServiceTest {

    private static final LocalDate TIME = LocalDate.now();

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
    void saveShouldInvokeOnlyOnceWhenTakesLesson() {
        lessonService.save(new Lesson());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void saveShouldInvokeOnlyOnceWhenTakesLessonDto() {
        lessonService.save(createLessonDto());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    private LessonDto createLessonDto(){
        LessonDto lessonDto = new LessonDto(1, "1", 1, 1, 1, "1", 1);
        lessonDto.setDate("2021-04-11");
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        when(roomRepository.findById(1)).thenReturn(Optional.of(new Room()));
        when(teacherRepository.findById(1)).thenReturn(Optional.of(new Teacher()));
        return lessonDto;
    }

    @Test
    void findAllShouldInvokeOnlyOnce() {
        lessonService.findAll();
        verify(lessonRepository, only()).findAll();
    }

    @Test
    void findByIdShouldInvokeOnlyOnce() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(new Lesson()));
        lessonService.findById(1);
        verify(lessonRepository, only()).findById(anyInt());
    }

    @Test
    void findDtoByIdShouldInvokeOnlyOnce() {
        Lesson lesson = new Lesson(1, "Math", null, null, null, LocalDate.now(), 1);
        when(lessonRepository.findById(anyInt())).thenReturn(Optional.of(lesson));
        lessonService.findDtoById(1);
        verify(lessonRepository, only()).findById(anyInt());
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
        Group group = new Group(1, "any", false);
        Student student = new Student(1, "one", "one", group, false);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        lessonService.getLessonByStudentIdForDay(1, TIME);
        verify(lessonRepository, only()).getLessonByGroupIdForDay(any(Group.class), anyInt(), anyInt(), anyInt());
    }

    @Test
    void getLessonByStudentForMonthShouldInvokeOnlyOnce(){
        Group group = new Group(1, "any", false);
        Student student = new Student(1, "one", "one", group, false);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
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
            lessonService.findById(1);
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

    @Test
    void whenDtoWithGroupNullMapToLessonShouldThrowServiceException(){
        when(roomRepository.findById(1)).thenReturn(Optional.of(new Room()));
        when(teacherRepository.findById(1)).thenReturn(Optional.of(new Teacher()));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.save( new LessonDto(1, "1", 1, 1, 1, "2121-06-06", 1));
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 does not exist"));
    }

    @Test
    void whenDtoWithTeacherNullMapToLessonShouldThrowServiceException(){
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        when(roomRepository.findById(1)).thenReturn(Optional.of(new Room()));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.save( new LessonDto(1, "1", 1, 1, 1, "2121-06-06", 1));
        });
        assertTrue(thrown.getMessage().contains("Teacher with such id 1 does not exist"));
    }

    @Test
    void whenDtoWithRoomNullMapToLessonShouldThrowServiceException(){
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        when(teacherRepository.findById(1)).thenReturn(Optional.of(new Teacher()));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            lessonService.save( new LessonDto(1, "1", 1, 1, 1, "2121-06-06", 1));
        });
        assertEquals(thrown.getMessage(), "Room with such id 1 does not exist");
    }

    @Test
    void whenGroupIsBusyValidateShouldThrowValidationException(){
        LocalDate date = LocalDate.parse("2021-06-11");
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson());
        when(lessonRepository.findByGroupGroupIdAndDateAndLessonNumberAndLessonInactiveFalse(1, date, 1)).thenReturn(lessons);
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            lessonService.save(createLesson());
        });
        assertTrue(thrown.getMessage().contains("The group has already been busy in another lesson. Please choose another day or lesson number."));
    }

    @Test
    void whenRoomIsBusyValidateShouldThrowValidationException(){
        LocalDate date = LocalDate.parse("2021-06-11");
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson());
        when(lessonRepository.findByRoomRoomIdAndDateAndLessonNumberAndLessonInactiveFalse(1, date, 1)).thenReturn(lessons);
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            lessonService.save(createLesson());
        });
        assertTrue(thrown.getMessage().contains("The room has already been busy in another lesson. Please choose another day or lesson number."));
    }

    @Test
    void whenTeacherIsBusyValidateShouldThrowValidationException(){
        LocalDate date = LocalDate.parse("2021-06-11");
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson());
        when(lessonRepository.findByTeacherTeacherIdAndDateAndLessonNumberAndLessonInactiveFalse(1, date, 1)).thenReturn(lessons);
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            lessonService.save(createLesson());
        });
        assertTrue(thrown.getMessage().contains("The teacher has already been busy in another lesson. Please choose another day or lesson number."));
    }

    private Lesson createLesson(){
        Group group = new Group(1, "Java", false);
        Teacher teacher = new Teacher(1, "one", "one", false);
        Room room = new Room(1, 101, 10, false);
        return new Lesson(1, "Math", teacher, group, room, LocalDate.parse("2021-06-11"), 1);
    }
}