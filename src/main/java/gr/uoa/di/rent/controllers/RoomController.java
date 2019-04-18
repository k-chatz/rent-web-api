package gr.uoa.di.rent.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import gr.uoa.di.rent.models.Room;
import gr.uoa.di.rent.repositories.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    RoomRepository roomRepository;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("/rooms")
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }
}
