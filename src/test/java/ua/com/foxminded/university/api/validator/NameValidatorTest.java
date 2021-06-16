package ua.com.foxminded.university.api.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.api.controler.LessonController;
import ua.com.foxminded.university.api.exception_handler.ControllerExceptionHandler;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.service.LessonService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
public class NameValidatorTest {
    private static final String EMPTY_STRING = "";

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void whenNameIsEmptyShouldReturnErrorPage() throws Exception {
        LessonDto lessonDto = new LessonDto(1, EMPTY_STRING, 1, 1, 1, "2021-06-11", 1);
        doRequest(lessonDto);
    }

    @Test
    void whenNameIsLessThan2CharsShouldReturnErrorPage() throws Exception {
        LessonDto lessonDto = new LessonDto(1, "De", 1, 1, 1, "2021-06-11", 1);
        doRequest(lessonDto);
    }

    @Test
    void whenNameIsGraterThan12CharsShouldReturnErrorPage() throws Exception {
        LessonDto lessonDto = new LessonDto(1, "Geasadadadada", 1, 1, 1, "2021-06-11", 1);
        doRequest(lessonDto);
    }

    @Test
    void whenNameContainsDigitsOrCharsShouldReturnErrorPage() throws Exception {
        LessonDto lessonDto = new LessonDto(1, "History12!", 1, 1, 1, "2021-06-11", 1);
        doRequest(lessonDto);
    }

    private void doRequest(LessonDto lessonDto) throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/lessons/")
                .flashAttr("lessonDto", lessonDto);

        mockMvc.perform(request)
                .andExpect(view().name("errorPage"))
                .andExpect(model().attribute("title", "ValidationException"))
                .andExpect(model().attribute("message", "Name can not be empty and not contain special characters and digits. Name length must be grater then 2 and less then 12"));

        verify(lessonService, times(0)).save(lessonDto);
    }
}
