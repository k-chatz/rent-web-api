package gr.uoa.di.rent.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import gr.uoa.di.rent.models.Room;
import gr.uoa.di.rent.repositories.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    RoomRepository roomRepository;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("")
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public List<Room> getTest() {
        return roomRepository.findAll();
    }
}
