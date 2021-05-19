//package ua.com.foxminded.university.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import ua.com.foxminded.university.Application;
//import ua.com.foxminded.university.repository.RoomRepository;
//import ua.com.foxminded.university.exception.RepositoryException;
//import ua.com.foxminded.university.exception.ServiceException;
//import ua.com.foxminded.university.model.Room;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(classes = Application.class)
//class RoomServiceTest {
//
//    private static final String EMPTY_STRING = "";
//
//    @Mock
//    private RoomRepository roomRepository;
//
//    @InjectMocks
//    private RoomService roomService;
//
//    @Test
//    void createShouldInvokeOnlyOnce() {
//        roomService.create(new Room(1, 101));
//        verify(roomRepository, only()).create(any(Room.class));
//    }
//
//    @Test
//    void getAllShouldInvokeOnlyOnce() {
//        roomService.getAll();
//        verify(roomRepository, only()).getAll();
//    }
//
//    @Test
//    void getAllActivatedShouldInvokeOnlyOnce() {
//        roomService.getAllActivated();
//        verify(roomRepository, only()).getAll();
//    }
//
//    @Test
//    void getByIdShouldInvokeOnlyOnce() {
//        roomService.getById(1);
//        verify(roomRepository, only()).getById(anyInt());
//    }
//
//    @Test
//    void updateShouldInvokeOnlyOnce() {
//        roomService.update(new Room(1, 101));
//        verify(roomRepository, only()).update(any(Room.class));
//    }
//
//    @Test
//    void deactivateShouldInvokeOnlyOnce() {
//        roomService.deactivate(1);;
//        verify(roomRepository, times(1)).deactivate(anyInt());
//    }
//
//    @Test
//    void activateShouldInvokeOnlyOnce() {
//        roomService.activate(1);
//        verify(roomRepository, only()).activate(anyInt());
//    }
//
//
//    @Test
//    void whenCreateCatchRepositoryExceptionShouldThrowServiceException() {
//        doThrow(new RepositoryException(EMPTY_STRING)).when(roomRepository).create(any(Room.class));
//        ServiceException thrown = assertThrows(ServiceException.class, () -> {
//            roomService.create(new Room());
//        });
//    }
//
//    @Test
//    void whenGetAllCatchRepositoryExceptionShouldThrowServiceException() {
//        doThrow(new RepositoryException(EMPTY_STRING)).when(roomRepository).getAll();
//        ServiceException thrown = assertThrows(ServiceException.class, () -> {
//            roomService.getAll();
//        });
//    }
//
//    @Test
//    void whenGetByIdCatchRepositoryExceptionShouldThrowServiceException() {
//        doThrow(new RepositoryException(EMPTY_STRING)).when(roomRepository).getById(anyInt());
//        ServiceException thrown = assertThrows(ServiceException.class, () -> {
//            roomService.getById(1);
//        });
//    }
//
//    @Test
//    void whenUpdateCatchRepositoryExceptionShouldThrowServiceException() {
//        doThrow(new RepositoryException(EMPTY_STRING)).when(roomRepository).update(any(Room.class));
//        ServiceException thrown = assertThrows(ServiceException.class, () -> {
//            roomService.update(new Room());
//        });
//    }
//
//    @Test
//    void whenDeactivateCatchRepositoryExceptionShouldThrowServiceException() {
//        doThrow(new RepositoryException(EMPTY_STRING)).when(roomRepository).deactivate(anyInt());
//        ServiceException thrown = assertThrows(ServiceException.class, () -> {
//            roomService.deactivate(1);
//        });
//    }
//
//    @Test
//    void whenActivateCatchRepositoryExceptionShouldThrowServiceException() {
//        doThrow(new RepositoryException(EMPTY_STRING)).when(roomRepository).activate(anyInt());
//        ServiceException thrown = assertThrows(ServiceException.class, () -> {
//            roomService.activate(1);
//        });
//    }
//}