package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.payload.requests.HotelRequest;
import gr.uoa.di.rent.payload.requests.filters.PagedResponseFilter;
import gr.uoa.di.rent.payload.responses.HotelResponse;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.payload.responses.RoomResponse;
import gr.uoa.di.rent.repositories.BusinessRepository;
import gr.uoa.di.rent.repositories.HotelRepository;
import gr.uoa.di.rent.repositories.RoomRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.Principal;
import gr.uoa.di.rent.services.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Validated
@RequestMapping("/hotels")
public class HotelController {

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

    private final HotelService hotelService;

    private final BusinessRepository businessRepository;

    private final RoomRepository roomRepository;

    private final HotelRepository hotelRepository;

    private final UserRepository userRepository;

    private final AtomicInteger counter = new AtomicInteger();

    public HotelController(HotelService hotelService, BusinessRepository businessRepository, RoomRepository roomRepository, HotelRepository hotelRepository, UserRepository userRepository) {
        this.hotelService = hotelService;
        this.businessRepository = businessRepository;
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
    }


    @PostMapping("")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<?> createHotel(@Valid @RequestBody HotelRequest hotelRequest, @Valid @CurrentUser Principal principal) {

        User provider = principal.getUser();

        // Get business for this provider (principal).
        Optional<Business> business_opt = businessRepository.findById(provider.getBusiness().getId());
        if ( !business_opt.isPresent() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No business was found for provider with username: " + provider.getUsername());
        }

        logger.debug(business_opt.get().toString());

        Business business = business_opt.get();

        // Create a hotel object which will be under that business.
        Hotel hotel = hotelRequest.asHotel(business.getId());

        hotel.setBusiness(business);

        List<Room> rooms = new ArrayList<Room>();

        hotel.setRooms(rooms);

        List<File> hotel_photos = new ArrayList<>();
        hotel.setHotel_photos(hotel_photos);

        hotel = hotelService.createHotel(hotel);
        // Get the new hotel-object having its id assigned by the database.

        // Store the hotel in the database.
        return ResponseEntity.ok(new HotelResponse(hotel));
    }

    @GetMapping("/{hotelId}")
    public Hotel getHotelByID(@PathVariable(value = "hotelId") Long hotelId) {
        return hotelService.findHotelByID(hotelId);
    }

}
