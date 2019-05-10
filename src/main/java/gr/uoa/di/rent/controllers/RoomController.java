package gr.uoa.di.rent.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import gr.uoa.di.rent.models.Room;
import gr.uoa.di.rent.payload.requests.filters.PagedResponseFilter;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.repositories.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("hotels/{hotelId}/rooms")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomRepository roomRepository;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("")
    public List<Room> getHotelRooms(@PathVariable(value = "hotelId") Long hotelId) {

        return roomRepository.findAllByHotel_id(hotelId);
    }

    @GetMapping("paged")
    public PagedResponse<Room> getHotelRoomsPaginated(PagedResponseFilter pagedResponseFilter) {

        return null;
    }


    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')or hasRole('PROVIDER') or hasRole('ADMIN')")
    public List<Room> getTest() {
        return roomRepository.findAll();
    }
}
