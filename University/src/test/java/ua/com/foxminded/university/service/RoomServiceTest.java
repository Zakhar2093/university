package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Room;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class RoomServiceTest {
    
    private static final String EMPTY_STRING = "";
    private RoomService roomService;
    @Mock
    private RoomDao roomDao;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        roomService = new RoomService(roomDao);
    }

    @Test
    void createShouldInvokeOnlyOnce() {
        roomService.create(new Room(1, 101));
        verify(roomDao, only()).create(any(Room.class));
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        roomService.getAll();
        verify(roomDao, only()).getAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        roomService.getAllActivated();
        verify(roomDao, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        roomService.getById(1);
        verify(roomDao, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnce() {
        roomService.update(new Room(1, 101));
        verify(roomDao, only()).update(any(Room.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        roomService.deactivate(1);;
        verify(roomDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        roomService.activate(1);
        verify(roomDao, only()).activate(anyInt());
    }
    
    @Test
    void getRoomByLessonShouldInvokeOnlyOnce() {
        roomService.getRoomByLesson(1);
        verify(roomDao, only()).getRoomByLesson(anyInt());
    }
    
    @Test
    void whenCreateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(roomDao).create(any(Room.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            roomService.create(new Room());
        });
    }
    
    @Test
    void whenGetAllCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(roomDao).getAll();
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            roomService.getAll();
        });    
    }
    
    @Test
    void whenGetByIdCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(roomDao).getById(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            roomService.getById(1);
        });
    }
    
    @Test
    void whenUpdateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(roomDao).update(any(Room.class));
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            roomService.update(new Room());
        });
    }
    
    @Test
    void whenDeactivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(roomDao).deactivate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            roomService.deactivate(1);
        });
    }
    
    @Test
    void whenActivateCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(roomDao).activate(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            roomService.activate(1);
        });
    }
    
    @Test
    void whenGetRoomByLessonCatchDaoExceptionShouldThrowServiceException() {
        doThrow(new DaoException(EMPTY_STRING)).when(roomDao).getRoomByLesson(anyInt());
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            roomService.getRoomByLesson(1);
        });
    }
}