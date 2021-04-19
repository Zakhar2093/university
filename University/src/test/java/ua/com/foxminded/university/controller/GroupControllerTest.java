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
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

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
        when(groupService.getAllActivated()).thenReturn(getTestGroups());

        mockMvc.perform(get("/groups/"))
                .andExpect(view().name("groups/index"))
                .andExpect(model().attribute("groups", getTestGroups()));
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


    private List<Group> getTestGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "Java", false));
        groups.add(new Group(2, "C++", false));
        groups.add(new Group(3, "PHP", false));
        return groups;
    }
}