package gr.uoa.di.rent.controllers;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import gr.uoa.di.rent.models.Hotel;
import gr.uoa.di.rent.models.Room;
import gr.uoa.di.rent.payload.requests.filters.PagedResponseFilter;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.payload.responses.RoomResponse;
import gr.uoa.di.rent.repositories.HotelRepository;
import gr.uoa.di.rent.repositories.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private HotelRepository hotelRepository;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("")
    public List<Room> getHotelRooms(@PathVariable(value = "hotelId") Long hotelId) {

        return roomRepository.findAllByHotel_id(hotelId);
    }

    @GetMapping("paged")
    public PagedResponse<Room> getHotelRoomsPaginated(PagedResponseFilter pagedResponseFilter) {

        return null;
    }

    @GetMapping("{roomId:[\\d]+}")
    public ResponseEntity<?> getHotelRoom(@PathVariable(value = "hotelId") Long hotelId, @PathVariable(value = "roomId") Long roomId) {

        // Check if the given hotel exists.
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        if ( !hotel.isPresent() )
            return ResponseEntity.badRequest().body("No hotel exists with id = " + hotelId);

        // Check and return the room.
        Optional<Room> room = roomRepository.findById(roomId);
        if ( room.isPresent() )
            return ResponseEntity.ok(new RoomResponse(room.get()));
        else
            return ResponseEntity.badRequest().body("No room with id = " + roomId + " was found in hotel with id = " + hotelId);
    }
}
