package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.model.Group;

class GroupServiceTest {

    private DatabaseInitialization dbInit = new DatabaseInitialization();

    private GroupService groupService;
    @Mock
    private GroupDao groupDao;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        groupService = new GroupService(groupDao);
        dbInit.initialization();
    }

    @Test
    void createShouldInvokeOnlyOnce() {
        groupService.create(new Group());
        verify(groupDao, only()).create(any(Group.class));
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        groupService.getAll();
        verify(groupDao, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        groupService.getById(1);
        verify(groupDao, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnce() {
        groupService.update(new Group());
        verify(groupDao, only()).update(any(Group.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        groupService.deactivate(1);
        verify(groupDao, times(1)).removeGroupFromAllLessons(anyInt());
        verify(groupDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        groupService.activate(1);
        verify(groupDao, only()).activate(anyInt());
    }
}
