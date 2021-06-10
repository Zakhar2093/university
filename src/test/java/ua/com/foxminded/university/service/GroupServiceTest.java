package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.repository.GroupRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Test
    void saveShouldInvokeOnlyOnce() {
        groupService.save(new Group());
        verify(groupRepository, only()).save(any(Group.class));
    }

    @Test
    void findAllShouldInvokeOnlyOnce() {
        groupService.findAll();
        verify(groupRepository, only()).findAll();
    }

    @Test
    void findByIdShouldInvokeOnlyOnce() {
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        groupService.findById(1);
        verify(groupRepository, only()).findById(anyInt());
    }

    @Test
    void deactivateShouldInvokeOnlyOnce() {
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        groupService.deactivate(1);
        verify(groupRepository, times(1)).removeGroupFromAllStudents(anyInt());
        verify(groupRepository, times(1)).removeGroupFromAllLessons(anyInt());
    }

    @Test
    void activateShouldInvokeOnlyOnce() {
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        groupService.activate(1);
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void whenGetByIdTakesUnexcitedIdShouldThrowServiceException() {
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.findById(1);
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 does not exist"));
    }
}