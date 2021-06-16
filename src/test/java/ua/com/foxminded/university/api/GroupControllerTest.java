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
import ua.com.foxminded.university.api.controler.GroupController;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class GroupControllerTest {

    @Autowired
    private TestData testData;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void getAllShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(groupService.findAll()).thenReturn(testData.getTestGroups());

        mockMvc.perform(get("/groups/"))
                .andExpect(view().name("groups/index"))
                .andExpect(model().attribute("groups", testData.getTestGroups()));
    }

    @Test
    void createShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Group group = createGroup();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/groups/").flashAttr("group", group);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, only()).save(group);
    }

    @Test
    void updateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Group group = createGroup();
        when(groupService.findById(anyInt())).thenReturn(group);

        mockMvc.perform(get("/groups/{id}/edit", 2))
                .andExpect(view().name("groups/update"))
                .andExpect(model().attribute("group", group));
    }

    @Test
    void submitUpdateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int expectedGroupId = 2;
        Group group = createGroup();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/groups/{id}", expectedGroupId).flashAttr("group", group);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, only()).save(group);
        int actualGroupId = group.getGroupId();
        assertEquals(expectedGroupId, actualGroupId);
    }

    @Test
    void deleteShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        mockMvc.perform(delete("/groups/{id}", 2))
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, only()).deactivate(anyInt());
    }

    private Group createGroup(){
        return new Group(1, "Java", false);
    }
}