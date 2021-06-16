package ua.com.foxminded.university.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.controler.RoomController;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.service.RoomService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class RoomControllerTest {

    @Autowired
    private TestData testData;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    void getAllShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(roomService.findAll()).thenReturn(testData.getTestRooms());

        mockMvc.perform(get("/rooms/"))
                .andExpect(view().name("rooms/index"))
                .andExpect(model().attribute("rooms", testData.getTestRooms()));
    }

    @Test
    void createShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Room room = createRoom();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rooms/").flashAttr("room", room);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/rooms"));

        verify(roomService, only()).save(room);
    }

    @Test
    void updateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Room room = createRoom();
        when(roomService.findById(anyInt())).thenReturn(room);

        mockMvc.perform(get("/rooms/{id}/edit", 2))
                .andExpect(view().name("rooms/update"))
                .andExpect(model().attribute("room", room));
    }

    @Test
    void submitUpdateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int expectedRoomId = 2;
        Room room = createRoom();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/rooms/{id}", expectedRoomId).flashAttr("room", room);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/rooms"));

        verify(roomService, only()).save(room);
        int actualRoomId = room.getRoomId();
        assertEquals(expectedRoomId, actualRoomId);
    }

    @Test
    void deleteShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 2))
                .andExpect(view().name("redirect:/rooms"));

        verify(roomService, only()).deactivate(anyInt());
    }

    private Room createRoom(){
        return new Room(1, 101, 30, false);
    }
}