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
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class TeacherRestControllerTest {

    @Autowired
    private TestData testData;

    private static final String jsonTeacher = "{\"teacherId\": 1, \"firstName\": \"one\", \"lastName\": \"one\"}";
    private static final String jsonListOfTeacher = "[{'teacherId': 1, 'firstName': 'one', 'lastName': 'one'}," +
                                                    "{'teacherId': 2, 'firstName': 'two', 'lastName': 'two'}," +
                                                    "{'teacherId': 3, 'firstName': 'Three', 'lastName': 'Three'}]";
    private Teacher testTeacher;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherRestController teacherRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherRestController).build();
        testTeacher = testData.getTestTeachers().get(0);
    }

    @Test
    void findByIdShouldReturnCorrectJson() throws Exception {
        when(teacherService.findById(anyInt())).thenReturn(testTeacher);

        mockMvc.perform(get("/api/teachers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTeacher));

        verify(teacherService, only()).findById(anyInt());
    }

    @Test
    void findAllShouldReturnCorrectJson() throws Exception {
        when(teacherService.findAll()).thenReturn(testData.getTestTeachers());

        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfTeacher));

        verify(teacherService, only()).findAll();
    }

    @Test
    void saveShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        mockMvc.perform(post("/api/teachers")
                .content(jsonTeacher)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());

        verify(teacherService, only()).save(testTeacher);
    }

    @Test
    void updateShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        int expectedTeacherId = 1;

        mockMvc.perform(put("/api/teachers/{id}", expectedTeacherId)
                .content(jsonTeacher)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        verify(teacherService, only()).save(testTeacher);
    }

    @Test
    void deleteShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", 1))
                .andExpect(status().isOk());

        verify(teacherService, only()).deactivate(anyInt());
    }
}
