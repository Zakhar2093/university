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
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class GroupRestControllerTest {

    @Autowired
    private TestData testData;

    private static final String jsonGroup = "{\"groupId\": 1, \"groupName\": \"Java\"}";
    private static final String jsonListOfGroup = "[{'groupId': 1, 'groupName': 'Java'},{'groupId': 2, 'groupName': 'C++'},{'groupId': 3, 'groupName': 'PHP'}]";
    private Group testGroup;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupRestController groupRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupRestController).build();
        testGroup = testData.getTestGroups().get(0);
    }

    @Test
    void findByIdShouldReturnCorrectJson() throws Exception {
        when(groupService.findById(anyInt())).thenReturn(testGroup);

        mockMvc.perform(get("/api/groups/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonGroup));

        verify(groupService, only()).findById(anyInt());
    }

    @Test
    void findAllShouldReturnCorrectJson() throws Exception {
        when(groupService.findAll()).thenReturn(testData.getTestGroups());

        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfGroup));

        verify(groupService, only()).findAll();
    }

    @Test
    void saveShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        mockMvc.perform(post("/api/groups")
                        .content(jsonGroup)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());

        verify(groupService, only()).save(testGroup);
    }

    @Test
    void updateShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        int expectedGroupId = 1;

        mockMvc.perform(put("/api/groups/{id}", expectedGroupId)
                        .content(jsonGroup)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(groupService, only()).save(testGroup);
    }

    @Test
    void deleteShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/groups/{id}", 1))
                .andExpect(status().isOk());

        verify(groupService, only()).deactivate(anyInt());
    }
}