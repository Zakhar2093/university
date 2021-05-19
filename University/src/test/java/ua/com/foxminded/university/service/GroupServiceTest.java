package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.repository.GroupRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
class GroupServiceTest {

    private static final String EMPTY_STRING = "";

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Test
    void createShouldInvokeOnlyOnce() {
        groupService.create(new Group());
        verify(groupRepository, only()).save(any(Group.class));
    }

    @Test
    void getAllShouldInvokeOnlyOnce() {
        groupService.getAll();
        verify(groupRepository, only()).findAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        groupService.getAllActivated();
        verify(groupRepository, only()).findAll();
    }

    @Test
    void getByIdShouldInvokeOnlyOnce() {
        when(groupRepository.findById(1)).thenReturn(Optional.of(new Group()));
        groupService.getById(1);
        verify(groupRepository, only()).findById(anyInt());
    }

    @Test
    void updateShouldInvokeOnlyOnce() {
        groupService.update(new Group());
        verify(groupRepository, only()).save(any(Group.class));
    }

    @Test
    void deactivateShouldInvokeOnlyOnce() {
        groupService.deactivate(1);
        verify(groupRepository, times(1)).deactivate(anyInt());
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
    void whenCreateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).save(any(Group.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.create(new Group());
        });
    }

    @Test
    void whenGetAllCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).findAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.getAll();
        });
    }

    @Test
    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).findById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.getById(1);
        });
    }

    @Test
    void whenUpdateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).save(any(Group.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.update(new Group());
        });
    }

    @Test
    void whenDeactivateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.deactivate(1);
        });
    }

    @Test
    void whenActivateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).save(any(Group.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.activate(1);
        });
    }
}