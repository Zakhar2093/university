package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTest {

    private TestData testData = new TestData();;

    @Mock
    private StudentService studentService;
    @Mock
    private LessonService lessonService;
    @Mock
    private RoomService roomService;
    @Mock
    private GroupService groupService;
    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private GroupController groupController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
//        testData = new TestData();
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void getAllShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(groupService.getAllActivated()).thenReturn(testData.getTestGroups());

        mockMvc.perform(get("/groups/"))
                .andExpect(view().name("groups/index"))
                .andExpect(model().attribute("groups", testData.getTestGroups()));
    }

    @Test
    void createShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Group group = new Group();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/groups/").flashAttr("group", group);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, only()).create(group);
    }

    @Test
    void updateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Group group = new Group();
        when(groupService.getById(anyInt())).thenReturn(group);

        mockMvc.perform(get("/groups/{id}/edit", 2))
                .andExpect(view().name("groups/update"))
                .andExpect(model().attribute("group", group));
    }

    @Test
    void submitUpdateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int expectedGroupId = 2;
        Group group = new Group();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/groups/{id}", expectedGroupId).flashAttr("group", group);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, only()).update(group);
        int actualGroupId = group.getGroupId();
        assertEquals(expectedGroupId, actualGroupId);
    }

    @Test
    void deleteShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        mockMvc.perform(delete("/groups/{id}", 2))
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, only()).deactivate(anyInt());
    }

    @Test
    void showLessonsByGroupShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int id = 1;
        when(lessonService.getLessonsByGroupId(id)).thenReturn(testData.getTestLessons());
        when(roomService.getAllActivated()).thenReturn(testData.getTestRooms());
        when(groupService.getById(id)).thenReturn(testData.getTestGroups().get(0));
        when(teacherService.getAllActivated()).thenReturn(testData.getTestTeachers());

        mockMvc.perform(get("/groups/{id}/lessons", id))
                .andExpect(view().name("lessons/index"))
                .andExpect(model().attribute("lessons", testData.getTestLessons()))
                .andExpect(model().attribute("rooms", testData.getTestRooms()))
                .andExpect(model().attribute("group", testData.getTestGroups().get(0)))
                .andExpect(model().attribute("teachers", testData.getTestTeachers()));
    }

    @Test
    void showStudentsByGroupShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int id = 1;
        when(studentService.getStudentsByGroupId(id)).thenReturn(testData.getTestStudent());
        when(groupService.getById(id)).thenReturn(testData.getTestGroups().get(0));

        mockMvc.perform(get("/groups/{id}/students", id))
                .andExpect(view().name("students/index"))
                .andExpect(model().attribute("students", testData.getTestStudent()))
                .andExpect(model().attribute("group", testData.getTestGroups().get(0)));
    }
}