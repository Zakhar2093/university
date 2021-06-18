package ua.com.foxminded.university.api.rest_controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.controller.TestData;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.service.LessonService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class LessonRestControllerTest {

    private static final String jsonLesson = "{" +
                                                      "\"lessonId\": 1,\n" +
                                                      "\"lessonName\": \"Math\",\n" +
                                                      "\"teacherId\": 1,\n" +
                                                      "\"groupId\": 1,\n" +
                                                      "\"roomId\": 2,\n" +
                                                      "\"date\": \"2021-11-04\",\n" +
                                                      "\"lessonNumber\": 1" +
                                                "}";
    private static final String jsonListOfLesson = "[    {\n" +
                                                    "        \"lessonId\": 1,\n" +
                                                    "        \"lessonName\": \"Math\",\n" +
                                                    "        \"teacherId\": 1,\n" +
                                                    "        \"groupId\": 1,\n" +
                                                    "        \"roomId\": 2,\n" +
                                                    "        \"date\": \"2021-11-04\",\n" +
                                                    "        \"lessonNumber\": 1\n" +
                                                    "    }," +
                                                    "    {\n" +
                                                    "        \"lessonId\": 2,\n" +
                                                    "        \"lessonName\": \"History\",\n" +
                                                    "        \"teacherId\": 1,\n" +
                                                    "        \"groupId\": 1,\n" +
                                                    "        \"roomId\": 1,\n" +
                                                    "        \"date\": \"2021-11-04\",\n" +
                                                    "        \"lessonNumber\": 2\n" +
                                                    "    }," +
                                                    "    {\n" +
                                                    "        \"lessonId\": 3,\n" +
                                                    "        \"lessonName\": \"English\",\n" +
                                                    "        \"teacherId\": 2,\n" +
                                                    "        \"groupId\": 2,\n" +
                                                    "        \"roomId\": 1,\n" +
                                                    "        \"date\": \"2021-11-04\",\n" +
                                                    "        \"lessonNumber\": 3\n" +
                                                    "    }]";

    @Autowired
    TestData testData;

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonRestController lessonRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonRestController).build();
    }

    @Test
    void findByIdShouldReturnCorrectJson() throws Exception {
        when(lessonService.findDtoById(anyInt())).thenReturn(getTestLessonDto());

        mockMvc.perform(get("/api/lessons/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonLesson));

        verify(lessonService, only()).findDtoById(anyInt());
    }

    @Test
    void findAllShouldReturnCorrectJson() throws Exception {
        when(lessonService.findAllDto()).thenReturn(getTestListOfLessonsDto());

        mockMvc.perform(get("/api/lessons"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfLesson));

        verify(lessonService, only()).findAllDto();
    }

    @Test
    void saveShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        mockMvc.perform(post("/api/lessons")
                .content(jsonLesson)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());

        verify(lessonService, only()).save(getTestLessonDto());
    }

    @Test
    void updateShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        int expectedLessonId = 1;

        mockMvc.perform(put("/api/lessons/{id}", expectedLessonId)
                .content(jsonLesson)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        verify(lessonService, only()).save(getTestLessonDto());
    }

    @Test
    void deleteShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/lessons/{id}", 1))
                .andExpect(status().isOk());

        verify(lessonService, only()).deactivate(anyInt());
    }

    @Test
    void showLessonDtoByGroupShouldReturnCorrectJson() throws Exception {
        when(lessonService.getLessonsDtoByGroupId(anyInt())).thenReturn(getTestListOfLessonsDto());

        mockMvc.perform(get("/api/groups/{id}/lessons", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfLesson));

        verify(lessonService, only()).getLessonsDtoByGroupId(anyInt());
    }

    @Test
    void showLessonDtoByRoomShouldReturnCorrectJson() throws Exception {
        when(lessonService.getLessonsDtoByRoomId(anyInt())).thenReturn(getTestListOfLessonsDto());

        mockMvc.perform(get("/api/rooms/{id}/lessons", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfLesson));

        verify(lessonService, only()).getLessonsDtoByRoomId(anyInt());
    }

    @Test
    void showLessonDtoByTeacherShouldReturnCorrectJson() throws Exception {
        when(lessonService.getLessonsDtoByTeacherId(anyInt())).thenReturn(getTestListOfLessonsDto());

        mockMvc.perform(get("/api/teachers/{id}/lessons", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfLesson));

        verify(lessonService, only()).getLessonsDtoByTeacherId(anyInt());
    }

    @Test
    void getScheduleForStudentForDayShouldReturnCorrectJsonAndCollRightService() throws Exception {
        String entity = "Student";
        String duration = "Day";
        int id = 1;
        String date = "2121-06-11";
        LocalDate localDate = LocalDate.parse(date);


        when(lessonService.getLessonByStudentIdForDay(anyInt(), eq(localDate))).thenReturn(testData.getTestLessons());
        when(lessonService.mapListOfLessonsToListOfLessonsDto(testData.getTestLessons())).thenReturn(getTestListOfLessonsDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/lessons/Schedule")
                .param("entity", entity)
                .param("duration", duration)
                .param("id", String.valueOf(id))
                .param("date", date);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfLesson));

        verify(lessonService, times(1)).getLessonByStudentIdForDay(id, localDate);
        verify(lessonService, never()).getLessonByStudentIdForMonth(id, localDate);
        verify(lessonService, never()).getLessonByTeacherIdForDay(id, localDate);
        verify(lessonService, never()).getLessonByTeacherIdForMonth(id, localDate);
    }

    @Test
    void getScheduleForStudentForMouthShouldReturnCorrectJsonAndCollRightService() throws Exception {
        String entity = "Student";
        String duration = "Month";
        int id = 1;
        String date = "2121-06-11";
        LocalDate localDate = LocalDate.parse(date);

        when(lessonService.getLessonByStudentIdForMonth(anyInt(), eq(localDate))).thenReturn(testData.getTestLessons());
        when(lessonService.mapListOfLessonsToListOfLessonsDto(testData.getTestLessons())).thenReturn(getTestListOfLessonsDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/lessons/Schedule")
                .param("entity", entity)
                .param("duration", duration)
                .param("id", String.valueOf(id))
                .param("date", date);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfLesson));

        verify(lessonService, never()).getLessonByStudentIdForDay(id, localDate);
        verify(lessonService, times(1)).getLessonByStudentIdForMonth(id, localDate);
        verify(lessonService, never()).getLessonByTeacherIdForDay(id, localDate);
        verify(lessonService, never()).getLessonByTeacherIdForMonth(id, localDate);
    }

    @Test
    void getScheduleForTeacherForDayShouldReturnCorrectJsonAndCollRightService() throws Exception {
        String entity = "Teacher";
        String duration = "Day";
        int id = 1;
        String date = "2121-06-11";
        LocalDate localDate = LocalDate.parse(date);

        when(lessonService.getLessonByTeacherIdForDay(anyInt(), eq(localDate))).thenReturn(testData.getTestLessons());
        when(lessonService.mapListOfLessonsToListOfLessonsDto(testData.getTestLessons())).thenReturn(getTestListOfLessonsDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/lessons/Schedule")
                .param("entity", entity)
                .param("duration", duration)
                .param("id", String.valueOf(id))
                .param("date", date);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfLesson));

        verify(lessonService, never()).getLessonByStudentIdForDay(id, localDate);
        verify(lessonService, never()).getLessonByStudentIdForMonth(id, localDate);
        verify(lessonService, times(1)).getLessonByTeacherIdForDay(id, localDate);
        verify(lessonService, never()).getLessonByTeacherIdForMonth(id, localDate);
    }

    @Test
    void getScheduleForTeacherForMonthShouldReturnCorrectJsonAndCollRightService() throws Exception {
        String entity = "Teacher";
        String duration = "Month";
        int id = 1;
        String date = "2121-06-11";
        LocalDate localDate = LocalDate.parse(date);

        when(lessonService.getLessonByTeacherIdForMonth(anyInt(), eq(localDate))).thenReturn(testData.getTestLessons());
        when(lessonService.mapListOfLessonsToListOfLessonsDto(testData.getTestLessons())).thenReturn(getTestListOfLessonsDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/lessons/Schedule")
                .param("entity", entity)
                .param("duration", duration)
                .param("id", String.valueOf(id))
                .param("date", date);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfLesson));

        verify(lessonService, never()).getLessonByStudentIdForDay(id, localDate);
        verify(lessonService, never()).getLessonByStudentIdForMonth(id, localDate);
        verify(lessonService, never()).getLessonByTeacherIdForDay(id, localDate);
        verify(lessonService, times(1)).getLessonByTeacherIdForMonth(id, localDate);
    }


    private LessonDto getTestLessonDto(){
        return new LessonDto(1, "Math", 1, 1, 2, "2021-11-04", 1);
    }


    private List<LessonDto> getTestListOfLessonsDto() {
        List<LessonDto> lessons = new ArrayList<>();
        lessons.add(new LessonDto(1, "Math", 1, 1, 2, "2021-11-04", 1));
        lessons.add(new LessonDto(2, "History", 1, 1, 1, "2021-11-04", 2));
        lessons.add(new LessonDto(3, "English", 2, 2, 1, "2021-11-04", 3));
        return lessons;
    }
}
