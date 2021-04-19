package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.RoomService;
import ua.com.foxminded.university.service.TeacherService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class LessonControllerTest {
    private static final String FORMAT = "dd MM yyyy hh:mm a";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    @Mock
    private LessonService lessonService;
    @Mock
    private RoomService roomService;
    @Mock
    private GroupService groupService;
    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private LessonController lessonController;

    private MockMvc mockMvc;


    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }

    @Test
    void getAllShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(lessonService.getAllActivated()).thenReturn(getTestLessons());
        when(roomService.getAllActivated()).thenReturn(getTestRooms());
        when(groupService.getAllActivated()).thenReturn(getTestGroups());
        when(teacherService.getAllActivated()).thenReturn(getTestTeachers());

        mockMvc.perform(get("/lessons/"))
                .andExpect(view().name("lessons/index"))
                .andExpect(model().attribute("lessons", getTestLessons()))
                .andExpect(model().attribute("rooms", getTestRooms()))
                .andExpect(model().attribute("groups", getTestGroups()))
                .andExpect(model().attribute("teachers", getTestTeachers()));
    }

    @Test
    void createShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(roomService.getAllActivated()).thenReturn(getTestRooms());
        when(groupService.getAllActivated()).thenReturn(getTestGroups());
        when(teacherService.getAllActivated()).thenReturn(getTestTeachers());

        LessonDto lessonDto = new LessonDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/add").flashAttr("lessonDto", lessonDto);
        mockMvc.perform(request)
                .andExpect(view().name("lessons/add"))
                .andExpect(model().attribute("rooms", getTestRooms()))
                .andExpect(model().attribute("groups", getTestGroups()))
                .andExpect(model().attribute("teachers", getTestTeachers()));
    }

    @Test
    void submitCreateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        LessonDto lessonDto = new LessonDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lessons/").flashAttr("lessonDto", lessonDto);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/lessons"));

        verify(lessonService, only()).create(lessonDto);
    }

    @Test
    void updateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        LessonDto lessonDto = new LessonDto();
        when(lessonService.getDtoById(anyInt())).thenReturn(lessonDto);
        when(roomService.getAllActivated()).thenReturn(getTestRooms());
        when(groupService.getAllActivated()).thenReturn(getTestGroups());
        when(teacherService.getAllActivated()).thenReturn(getTestTeachers());

        mockMvc.perform(get("/lessons/{id}/edit", 2))
                .andExpect(view().name("lessons/update"))
                .andExpect(model().attribute("lessonDto", lessonDto))
                .andExpect(model().attribute("rooms", getTestRooms()))
                .andExpect(model().attribute("groups", getTestGroups()))
                .andExpect(model().attribute("teachers", getTestTeachers()));;
    }

    @Test
    void submitUpdateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int expectedLessonDtoId = 2;
        LessonDto lessonDto = new LessonDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/lessons/{id}", expectedLessonDtoId).flashAttr("lessonDto", lessonDto);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/lessons"));

        verify(lessonService, only()).update(lessonDto);
        int actualLessonDtoId = lessonDto.getLessonId();
        assertEquals(expectedLessonDtoId, actualLessonDtoId);
    }

    @Test
    void deleteShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        mockMvc.perform(delete("/lessons/{id}", 2))
                .andExpect(view().name("redirect:/lessons"));

        verify(lessonService, only()).deactivate(anyInt());
    }

    @Test
    void getScheduleForStudentForDayShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        String entity = "Student";
        String duration = "Day";
        int id = 1;
        String date = "11 04 2021 12:44 AM";
        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);

        when(lessonService.getLessonByStudentIdForDay(anyInt(), eq(localDateTime))).thenReturn(getTestLessons());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/getSchedule")
                .param("entity", entity)
                .param("duration", duration)
                .param("id", String.valueOf(id))
                .param("date", date);
        mockMvc.perform(request)
                .andExpect(model().attribute("lessons", getTestLessons()))
                .andExpect(view().name("lessons/index"));

        verify(lessonService, only()).getLessonByStudentIdForDay(id, localDateTime);
        verify(lessonService, never()).getLessonByStudentIdForMonth(id, localDateTime);
        verify(lessonService, never()).getLessonByTeacherIdForDay(id, localDateTime);
        verify(lessonService, never()).getLessonByTeacherIdForMonth(id, localDateTime);
    }

    @Test
    void getScheduleForStudentForMouthShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        String entity = "Student";
        String duration = "Month";
        int id = 1;
        String date = "11 04 2021 12:44 AM";
        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);

        when(lessonService.getLessonByStudentIdForMonth(anyInt(), eq(localDateTime))).thenReturn(getTestLessons());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/getSchedule")
                .param("entity", entity)
                .param("duration", duration)
                .param("id", String.valueOf(id))
                .param("date", date);
        mockMvc.perform(request)
                .andExpect(model().attribute("lessons", getTestLessons()))
                .andExpect(view().name("lessons/index"));

        verify(lessonService, never()).getLessonByStudentIdForDay(id, localDateTime);
        verify(lessonService, only()).getLessonByStudentIdForMonth(id, localDateTime);
        verify(lessonService, never()).getLessonByTeacherIdForDay(id, localDateTime);
        verify(lessonService, never()).getLessonByTeacherIdForMonth(id, localDateTime);
    }

    @Test
    void getScheduleForTeacherForDayShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        String entity = "Teacher";
        String duration = "Day";
        int id = 1;
        String date = "11 04 2021 12:44 AM";
        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);

        when(lessonService.getLessonByTeacherIdForDay(anyInt(), eq(localDateTime))).thenReturn(getTestLessons());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/getSchedule")
                .param("entity", entity)
                .param("duration", duration)
                .param("id", String.valueOf(id))
                .param("date", date);
        mockMvc.perform(request)
                .andExpect(model().attribute("lessons", getTestLessons()))
                .andExpect(view().name("lessons/index"));

        verify(lessonService, never()).getLessonByStudentIdForDay(id, localDateTime);
        verify(lessonService, never()).getLessonByStudentIdForMonth(id, localDateTime);
        verify(lessonService, only()).getLessonByTeacherIdForDay(id, localDateTime);
        verify(lessonService, never()).getLessonByTeacherIdForMonth(id, localDateTime);
    }

    @Test
    void getScheduleForTeacherForMonthShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        String entity = "Teacher";
        String duration = "Month";
        int id = 1;
        String date = "11 04 2021 12:44 AM";
        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);

        when(lessonService.getLessonByTeacherIdForMonth(anyInt(), eq(localDateTime))).thenReturn(getTestLessons());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/getSchedule")
                .param("entity", entity)
                .param("duration", duration)
                .param("id", String.valueOf(id))
                .param("date", date);
        mockMvc.perform(request)
                .andExpect(model().attribute("lessons", getTestLessons()))
                .andExpect(view().name("lessons/index"));

        verify(lessonService, never()).getLessonByStudentIdForDay(id, localDateTime);
        verify(lessonService, never()).getLessonByStudentIdForMonth(id, localDateTime);
        verify(lessonService, never()).getLessonByTeacherIdForDay(id, localDateTime);
        verify(lessonService, only()).getLessonByTeacherIdForMonth(id, localDateTime);
    }

    @Test
    void showLessonsByRoomShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int id = 1;
        when(lessonService.getLessonsByRoomId(id)).thenReturn(getTestLessons());
        when(roomService.getById(id)).thenReturn(getTestRooms().get(0));
        when(groupService.getAllActivated()).thenReturn(getTestGroups());
        when(teacherService.getAllActivated()).thenReturn(getTestTeachers());

        mockMvc.perform(get("/lessons/byRoom/{id}", id))
                .andExpect(view().name("lessons/index"))
                .andExpect(model().attribute("lessons", getTestLessons()))
                .andExpect(model().attribute("room", getTestRooms().get(0)))
                .andExpect(model().attribute("groups", getTestGroups()))
                .andExpect(model().attribute("teachers", getTestTeachers()));
    }

    @Test
    void showLessonsByTeacherShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int id = 1;
        when(lessonService.getLessonsByTeacherId(id)).thenReturn(getTestLessons());
        when(roomService.getAllActivated()).thenReturn(getTestRooms());
        when(groupService.getAllActivated()).thenReturn(getTestGroups());
        when(teacherService.getById(id)).thenReturn(getTestTeachers().get(0));

        mockMvc.perform(get("/lessons/byTeacher/{id}", id))
                .andExpect(view().name("lessons/index"))
                .andExpect(model().attribute("lessons", getTestLessons()))
                .andExpect(model().attribute("rooms", getTestRooms()))
                .andExpect(model().attribute("groups", getTestGroups()))
                .andExpect(model().attribute("teacher", getTestTeachers().get(0)));
    }

    @Test
    void showLessonsByGroupShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int id = 1;
        when(lessonService.getLessonsByGroupId(id)).thenReturn(getTestLessons());
        when(roomService.getAllActivated()).thenReturn(getTestRooms());
        when(groupService.getById(id)).thenReturn(getTestGroups().get(0));
        when(teacherService.getAllActivated()).thenReturn(getTestTeachers());

        mockMvc.perform(get("/lessons/byGroup/{id}", id))
                .andExpect(view().name("lessons/index"))
                .andExpect(model().attribute("lessons", getTestLessons()))
                .andExpect(model().attribute("rooms", getTestRooms()))
                .andExpect(model().attribute("group", getTestGroups().get(0)))
                .andExpect(model().attribute("teachers", getTestTeachers()));
    }

    private List<Room> getTestRooms() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(1, 101));
        rooms.add(new Room(2, 102));
        rooms.add(new Room(3, 103));
        return rooms;
    }

    private List<Group> getTestGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "Java", false));
        groups.add(new Group(2, "C++", false));
        groups.add(new Group(3, "PHP", false));
        return groups;
    }

    private List<Teacher> getTestTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "one", "one", false));
        teachers.add(new Teacher(2, "two", "two", false));
        teachers.add(new Teacher(3, "Three", "Three", false));
        return teachers;
    }

    private List<Lesson> getTestLessons() {
        List<Teacher> teachers = getTestTeachers();
        List<Group> groups = getTestGroups();
        List<Room> rooms = getTestRooms();
        LocalDateTime date = LocalDateTime.parse("11 04 2021 12:44 AM", FORMATTER);

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "Math", teachers.get(1), groups.get(0), rooms.get(0), date, false));
        lessons.add(new Lesson(2, "History", teachers.get(1), groups.get(1), rooms.get(0), date, false));
        lessons.add(new Lesson(3, "English", teachers.get(2), groups.get(2), rooms.get(2), date, false));
        lessons.add(new Lesson(4, "Math", teachers.get(0), groups.get(1), rooms.get(1), date, false));
        lessons.add(new Lesson(5, "Bio", teachers.get(0), groups.get(2), rooms.get(1), date, false));
        return lessons;
    }
}
