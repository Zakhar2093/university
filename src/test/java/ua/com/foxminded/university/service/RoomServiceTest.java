package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.repository.RoomRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createShouldInvokeOnlyOnce() {
        roomService.create(new Room(1, 101, 10, false));
        verify(roomRepository, only()).save(any(Room.class));
    }

    @Test
    void getAllShouldInvokeOnlyOnce() {
        roomService.getAll();
        verify(roomRepository, only()).findAll();
    }

    @Test
    void getAllActivatedShouldInvokeOnlyOnce() {
        roomService.getAllActivated();
        verify(roomRepository, only()).findAll();
    }

    @Test
    void getByIdShouldInvokeOnlyOnce() {
        when(roomRepository.findById(1)).thenReturn(Optional.of(new Room()));
        roomService.getById(1);
        verify(roomRepository, only()).findById(anyInt());
    }

    @Test
    void updateShouldInvokeOnlyOnce() {
        roomService.update(new Room(1, 101, 10, false));
        verify(roomRepository, only()).save(any(Room.class));
    }

    @Test
    void deactivateShouldInvokeOnlyOnce() {
        when(roomRepository.findById(1)).thenReturn(Optional.of(new Room()));
        roomService.deactivate(1);;
        verify(roomRepository, times(1)).save(any(Room.class));
        verify(roomRepository, times(1)).removeRoomFromAllLessons(anyInt());
    }

    @Test
    void activateShouldInvokeOnlyOnce() {
        when(roomRepository.findById(1)).thenReturn(Optional.of(new Room()));
        roomService.activate(1);
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            roomService.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Room with such id 1 does not exist"));
    }
}