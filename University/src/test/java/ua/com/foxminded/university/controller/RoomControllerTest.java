package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.RoomService;
import ua.com.foxminded.university.service.TeacherService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class RoomControllerTest {

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
    private RoomController roomController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        testData = new TestData();
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    void getAllShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(roomService.getAllActivated()).thenReturn(testData.getTestRooms());

        mockMvc.perform(get("/rooms/"))
                .andExpect(view().name("rooms/index"))
                .andExpect(model().attribute("rooms", testData.getTestRooms()));
    }

    @Test
    void createShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Room room = new Room();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rooms/").flashAttr("room", room);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/rooms"));

        verify(roomService, only()).create(room);
    }

    @Test
    void updateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Room room = new Room();
        when(roomService.getById(anyInt())).thenReturn(room);

        mockMvc.perform(get("/rooms/{id}/edit", 2))
                .andExpect(view().name("rooms/update"))
                .andExpect(model().attribute("room", room));
    }

    @Test
    void submitUpdateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int expectedRoomId = 2;
        Room room = new Room();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/rooms/{id}", expectedRoomId).flashAttr("room", room);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/rooms"));

        verify(roomService, only()).update(room);
        int actualRoomId = room.getRoomId();
        assertEquals(expectedRoomId, actualRoomId);
    }

    @Test
    void deleteShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 2))
                .andExpect(view().name("redirect:/rooms"));

        verify(roomService, only()).deactivate(anyInt());
    }

    @Test
    void showLessonsByRoomShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int id = 1;
        when(lessonService.getLessonsByRoomId(id)).thenReturn(testData.getTestLessons());
        when(roomService.getById(id)).thenReturn(testData.getTestRooms().get(0));
        when(groupService.getAllActivated()).thenReturn(testData.getTestGroups());
        when(teacherService.getAllActivated()).thenReturn(testData.getTestTeachers());

        mockMvc.perform(get("/rooms/{id}/lessons", id))
                .andExpect(view().name("lessons/index"))
                .andExpect(model().attribute("lessons", testData.getTestLessons()))
                .andExpect(model().attribute("room", testData.getTestRooms().get(0)))
                .andExpect(model().attribute("groups", testData.getTestGroups()))
                .andExpect(model().attribute("teachers", testData.getTestTeachers()));
    }
}