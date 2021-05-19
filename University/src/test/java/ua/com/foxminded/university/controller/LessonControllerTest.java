package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.RoomService;
import ua.com.foxminded.university.service.TeacherService;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
public class LessonControllerTest {
    private static final String FORMAT = "dd MM yyyy hh:mm a";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    @Autowired
    private TestData testData;

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
        when(lessonService.getAllActivated()).thenReturn(testData.getTestLessons());
        when(roomService.getAllActivated()).thenReturn(testData.getTestRooms());
        when(groupService.getAllActivated()).thenReturn(testData.getTestGroups());
        when(teacherService.getAllActivated()).thenReturn(testData.getTestTeachers());

        mockMvc.perform(get("/lessons/"))
                .andExpect(view().name("lessons/index"))
                .andExpect(model().attribute("lessons", testData.getTestLessons()))
                .andExpect(model().attribute("rooms", testData.getTestRooms()))
                .andExpect(model().attribute("groups", testData.getTestGroups()))
                .andExpect(model().attribute("teachers", testData.getTestTeachers()));
    }

    @Test
    void createShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(roomService.getAllActivated()).thenReturn(testData.getTestRooms());
        when(groupService.getAllActivated()).thenReturn(testData.getTestGroups());
        when(teacherService.getAllActivated()).thenReturn(testData.getTestTeachers());

        LessonDto lessonDto = new LessonDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/add").flashAttr("lessonDto", lessonDto);
        mockMvc.perform(request)
                .andExpect(view().name("lessons/add"))
                .andExpect(model().attribute("rooms", testData.getTestRooms()))
                .andExpect(model().attribute("groups", testData.getTestGroups()))
                .andExpect(model().attribute("teachers", testData.getTestTeachers()));
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
        when(roomService.getAllActivated()).thenReturn(testData.getTestRooms());
        when(groupService.getAllActivated()).thenReturn(testData.getTestGroups());
        when(teacherService.getAllActivated()).thenReturn(testData.getTestTeachers());

        mockMvc.perform(get("/lessons/{id}/edit", 2))
                .andExpect(view().name("lessons/update"))
                .andExpect(model().attribute("lessonDto", lessonDto))
                .andExpect(model().attribute("rooms", testData.getTestRooms()))
                .andExpect(model().attribute("groups", testData.getTestGroups()))
                .andExpect(model().attribute("teachers", testData.getTestTeachers()));;
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

//    @Test
//    void getScheduleForStudentForDayShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
//        String entity = "Student";
//        String duration = "Day";
//        int id = 1;
//        String date = "11 04 2021 12:44 AM";
//        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);
//
//        when(lessonService.getLessonByStudentIdForDay(anyInt(), eq(localDateTime))).thenReturn(testData.getTestLessons());
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/Schedule")
//                .param("entity", entity)
//                .param("duration", duration)
//                .param("id", String.valueOf(id))
//                .param("date", date);
//        mockMvc.perform(request)
//                .andExpect(model().attribute("lessons", testData.getTestLessons()))
//                .andExpect(view().name("lessons/index"));
//
//        verify(lessonService, only()).getLessonByStudentIdForDay(id, localDateTime);
//        verify(lessonService, never()).getLessonByStudentIdForMonth(id, localDateTime);
//        verify(lessonService, never()).getLessonByTeacherIdForDay(id, localDateTime);
//        verify(lessonService, never()).getLessonByTeacherIdForMonth(id, localDateTime);
//    }
//
//    @Test
//    void getScheduleForStudentForMouthShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
//        String entity = "Student";
//        String duration = "Month";
//        int id = 1;
//        String date = "11 04 2021 12:44 AM";
//        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);
//
//        when(lessonService.getLessonByStudentIdForMonth(anyInt(), eq(localDateTime))).thenReturn(testData.getTestLessons());
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/Schedule")
//                .param("entity", entity)
//                .param("duration", duration)
//                .param("id", String.valueOf(id))
//                .param("date", date);
//        mockMvc.perform(request)
//                .andExpect(model().attribute("lessons", testData.getTestLessons()))
//                .andExpect(view().name("lessons/index"));
//
//        verify(lessonService, never()).getLessonByStudentIdForDay(id, localDateTime);
//        verify(lessonService, only()).getLessonByStudentIdForMonth(id, localDateTime);
//        verify(lessonService, never()).getLessonByTeacherIdForDay(id, localDateTime);
//        verify(lessonService, never()).getLessonByTeacherIdForMonth(id, localDateTime);
//    }
//
//    @Test
//    void getScheduleForTeacherForDayShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
//        String entity = "Teacher";
//        String duration = "Day";
//        int id = 1;
//        String date = "11 04 2021 12:44 AM";
//        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);
//
//        when(lessonService.getLessonByTeacherIdForDay(anyInt(), eq(localDateTime))).thenReturn(testData.getTestLessons());
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/Schedule")
//                .param("entity", entity)
//                .param("duration", duration)
//                .param("id", String.valueOf(id))
//                .param("date", date);
//        mockMvc.perform(request)
//                .andExpect(model().attribute("lessons", testData.getTestLessons()))
//                .andExpect(view().name("lessons/index"));
//
//        verify(lessonService, never()).getLessonByStudentIdForDay(id, localDateTime);
//        verify(lessonService, never()).getLessonByStudentIdForMonth(id, localDateTime);
//        verify(lessonService, only()).getLessonByTeacherIdForDay(id, localDateTime);
//        verify(lessonService, never()).getLessonByTeacherIdForMonth(id, localDateTime);
//    }
//
//    @Test
//    void getScheduleForTeacherForMonthShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
//        String entity = "Teacher";
//        String duration = "Month";
//        int id = 1;
//        String date = "11 04 2021 12:44 AM";
//        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);
//
//        when(lessonService.getLessonByTeacherIdForMonth(anyInt(), eq(localDateTime))).thenReturn(testData.getTestLessons());
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lessons/Schedule")
//                .param("entity", entity)
//                .param("duration", duration)
//                .param("id", String.valueOf(id))
//                .param("date", date);
//        mockMvc.perform(request)
//                .andExpect(model().attribute("lessons", testData.getTestLessons()))
//                .andExpect(view().name("lessons/index"));
//
//        verify(lessonService, never()).getLessonByStudentIdForDay(id, localDateTime);
//        verify(lessonService, never()).getLessonByStudentIdForMonth(id, localDateTime);
//        verify(lessonService, never()).getLessonByTeacherIdForDay(id, localDateTime);
//        verify(lessonService, only()).getLessonByTeacherIdForMonth(id, localDateTime);
//    }
//
//    @Test
//    void showLessonsByGroupShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
//        int id = 1;
//        when(lessonService.getLessonsByGroupId(id)).thenReturn(testData.getTestLessons());
//        when(roomService.getAllActivated()).thenReturn(testData.getTestRooms());
//        when(groupService.getById(id)).thenReturn(testData.getTestGroups().get(0));
//        when(teacherService.getAllActivated()).thenReturn(testData.getTestTeachers());
//
//        mockMvc.perform(get("/groups/{id}/lessons", id))
//                .andExpect(view().name("lessons/index"))
//                .andExpect(model().attribute("lessons", testData.getTestLessons()))
//                .andExpect(model().attribute("rooms", testData.getTestRooms()))
//                .andExpect(model().attribute("group", testData.getTestGroups().get(0)))
//                .andExpect(model().attribute("teachers", testData.getTestTeachers()));
//    }
//
//    @Test
//    void showLessonsByRoomShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
//        int id = 1;
//        when(lessonService.getLessonsByRoomId(id)).thenReturn(testData.getTestLessons());
//        when(roomService.getById(id)).thenReturn(testData.getTestRooms().get(0));
//        when(groupService.getAllActivated()).thenReturn(testData.getTestGroups());
//        when(teacherService.getAllActivated()).thenReturn(testData.getTestTeachers());
//
//        mockMvc.perform(get("/rooms/{id}/lessons", id))
//                .andExpect(view().name("lessons/index"))
//                .andExpect(model().attribute("lessons", testData.getTestLessons()))
//                .andExpect(model().attribute("room", testData.getTestRooms().get(0)))
//                .andExpect(model().attribute("groups", testData.getTestGroups()))
//                .andExpect(model().attribute("teachers", testData.getTestTeachers()));
//    }
//
//    @Test
//    void showLessonsByTeacherShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
//        int id = 1;
//        when(lessonService.getLessonsByTeacherId(id)).thenReturn(testData.getTestLessons());
//        when(roomService.getAllActivated()).thenReturn(testData.getTestRooms());
//        when(groupService.getAllActivated()).thenReturn(testData.getTestGroups());
//        when(teacherService.getById(id)).thenReturn(testData.getTestTeachers().get(0));
//
//        mockMvc.perform(get("/teachers/{id}/lessons", id))
//                .andExpect(view().name("lessons/index"))
//                .andExpect(model().attribute("lessons", testData.getTestLessons()))
//                .andExpect(model().attribute("rooms", testData.getTestRooms()))
//                .andExpect(model().attribute("groups", testData.getTestGroups()))
//                .andExpect(model().attribute("teacher", testData.getTestTeachers().get(0)));
//    }
}