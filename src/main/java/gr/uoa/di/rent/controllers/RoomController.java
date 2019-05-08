package gr.uoa.di.rent.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import gr.uoa.di.rent.models.Room;
import gr.uoa.di.rent.payload.requests.filters.PagedResponseFilter;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.repositories.RoomRepository;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/rooms")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomRepository roomRepository;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Room> getRooms(@CurrentUser Principal principal) {
        return roomRepository.findAll();
    }

    @GetMapping("paged")
    public PagedResponse<Room> getRooms(
            @CurrentUser Principal principal,
            PagedResponseFilter pagedResponseFilter) {

        return null;
    }


    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public List<Room> getTest() {
        return roomRepository.findAll();
    }
}
