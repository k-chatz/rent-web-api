package gr.uoa.di.rent.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import gr.uoa.di.rent.models.Room;
import gr.uoa.di.rent.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    @Autowired
    RoomRepository roomRepository;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("/rooms")
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }
}
