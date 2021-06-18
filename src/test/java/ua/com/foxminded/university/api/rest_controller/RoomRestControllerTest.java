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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.controller.TestData;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.service.RoomService;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class RoomRestControllerTest {

    @Autowired
    private TestData testData;

    private static final String jsonRoom = "{\"roomId\": 1, \"roomNumber\": 101, \"roomCapacity\": 10}";
    private static final String jsonListOfRoom = "[{'roomId': 1, 'roomNumber': 101, 'roomCapacity': 10}," +
                                                  "{'roomId': 2, 'roomNumber': 102, 'roomCapacity': 10}," +
                                                  "{'roomId': 3, 'roomNumber': 103, 'roomCapacity': 10}]";
    private Room testRoom;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomRestController roomRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomRestController).build();
        testRoom = testData.getTestRooms().get(0);
    }

    @Test
    void findByIdShouldReturnCorrectJson() throws Exception {
        when(roomService.findById(anyInt())).thenReturn(testRoom);

        mockMvc.perform(get("/api/rooms/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonRoom));

        verify(roomService, only()).findById(anyInt());
    }

    @Test
    void findAllShouldReturnCorrectJson() throws Exception {
        when(roomService.findAll()).thenReturn(testData.getTestRooms());

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfRoom));

        verify(roomService, only()).findAll();
    }

    @Test
    void saveShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        mockMvc.perform(post("/api/rooms")
                .content(jsonRoom)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());

        verify(roomService, only()).save(testRoom);
    }

    @Test
    void updateShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        int expectedRoomId = 1;

        mockMvc.perform(put("/api/rooms/{id}", expectedRoomId)
                .content(jsonRoom)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        verify(roomService, only()).save(testRoom);
    }

    @Test
    void deleteShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/rooms/{id}", 1))
                .andExpect(status().isOk());

        verify(roomService, only()).deactivate(anyInt());
    }
}
