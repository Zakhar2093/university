package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class GroupServiceTest {
    
    private static final String EMPTY_STRING = "";
    private GroupService groupService;
    @Mock
    private GroupRepository groupRepository;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        groupService = new GroupService(groupRepository);
    }

    @Test
    void createShouldInvokeOnlyOnce() {
        groupService.create(new Group());
        verify(groupRepository, only()).create(any(Group.class));
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        groupService.getAll();
        verify(groupRepository, only()).getAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        groupService.getAllActivated();
        verify(groupRepository, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        groupService.getById(1);
        verify(groupRepository, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnce() {
        groupService.update(new Group());
        verify(groupRepository, only()).update(any(Group.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        groupService.deactivate(1);
        verify(groupRepository, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        groupService.activate(1);
        verify(groupRepository, only()).activate(anyInt());
    }
    
    @Test
    void whenCreateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).create(any(Group.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.create(new Group());
        });
    }
    
    @Test
    void whenGetAllCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.getAll();
        });    
    }
    
    @Test
    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.getById(1);
        });
    }
    
    @Test
    void whenUpdateCatchRepositoryExceptionShouldThrowServiceException() {
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).update(any(Group.class));
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
        doThrow(new RepositoryException(EMPTY_STRING)).when(groupRepository).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.activate(1);
        });
    }
}