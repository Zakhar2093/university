package ua.com.foxminded.university.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.model.Room;

class RoomServiceTest {
    
    private DatabaseInitialization dbInit = new DatabaseInitialization();

    
    private RoomService roomService;
    @Mock
    private RoomDao roomDao;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        roomService = new RoomService(roomDao);
        dbInit.initialization();
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
        roomService.deactivate(1);
        verify(roomDao, times(1)).removeRoomFromAllLessons(anyInt());
        verify(roomDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        roomService.activate(1);
        verify(roomDao, only()).activate(anyInt());
    }
    
    @Test
    void getRoomByLessonShouldInvokeOnlyOnce() {
        roomService.getRoomByLesson(1);;
        verify(roomDao, only()).getRoomByLesson(anyInt());
    }
}
