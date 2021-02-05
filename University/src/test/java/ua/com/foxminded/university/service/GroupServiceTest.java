package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;

class GroupServiceTest {
    
    private static final String EMPTY_STRING = "";
    private GroupService groupService;
    @Mock
    private GroupDao groupDao;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        groupService = new GroupService(groupDao);
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
    
    @Test
    void getGroupByLessonShouldInvokeOnlyOnce() {
        groupService.getGroupByLesson(1);;
        verify(groupDao, only()).getGroupByLesson(anyInt());
    }
    
    @Test
    void getGroupByStudentShouldInvokeOnlyOnce() {
        groupService.getGroupByStudent(1);;
        verify(groupDao, only()).getGroupByStudent(anyInt());
    }
    
    @Test
    void whenCreateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(groupDao).create(any(Group.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.create(new Group());
        });
    }
    
    @Test
    void whenGetAllCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(groupDao).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.getAll();
        });    
    }
    
    @Test
    void whenGetByIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(groupDao).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.getById(1);
        });
    }
    
    @Test
    void whenUpdateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(groupDao).update(any(Group.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.update(new Group());
        });
    }
    
    @Test
    void whenDeactivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(groupDao).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.deactivate(1);
        });
    }
    
    @Test
    void whenActivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(groupDao).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.activate(1);
        });
    }
    
    @Test
    void whenGetGroupByLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(groupDao).getGroupByLesson(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.getGroupByLesson(1);;
        });
    }
    
    @Test
    void whenGetGroupByStudentCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(groupDao).getGroupByStudent(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            groupService.getGroupByStudent(1);;
        });
    }
}