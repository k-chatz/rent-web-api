package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.payload.requests.HotelRequest;
import gr.uoa.di.rent.payload.requests.ReservationRequest;
import gr.uoa.di.rent.payload.requests.filters.PagedResponseFilter;
import gr.uoa.di.rent.payload.responses.HotelResponse;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.payload.responses.RoomResponse;
import gr.uoa.di.rent.repositories.*;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.Principal;
import gr.uoa.di.rent.services.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

    private final CalendarRepository calendarRepository;

    private final UserRepository userRepository;

    private final AtomicInteger counter = new AtomicInteger();

    public HotelController(HotelService hotelService, BusinessRepository businessRepository, RoomRepository roomRepository, HotelRepository hotelRepository,
                           UserRepository userRepository, CalendarRepository calendarRepository) {
        this.hotelService = hotelService;
        this.businessRepository = businessRepository;
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
    }


    @PostMapping("")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<?> createHotel(@Valid @RequestBody HotelRequest hotelRequest, @Valid @CurrentUser Principal principal) {

        User provider = principal.getUser();

        // Get business for this provider (principal).
        Optional<Business> business_opt = businessRepository.findById(provider.getBusiness().getId());
        if (!business_opt.isPresent()) {
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


    @GetMapping("/{hotelId}/rooms")
    public List<Room> getHotelRooms(@PathVariable(value = "hotelId") Long hotelId) {

        return roomRepository.findAllByHotel_id(hotelId);
    }

    @GetMapping("/{hotelId}/rooms/paged")
    public PagedResponse<Room> getHotelRoomsPaginated(PagedResponseFilter pagedResponseFilter) {

        return null;
    }

    @GetMapping("/{hotelId}/rooms/{roomId:[\\d]+}")
    public ResponseEntity<?> getHotelRoom(@PathVariable(value = "hotelId") Long hotelId, @PathVariable(value = "roomId") Long roomId) {

        // Check if the given hotel exists.
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        if (!hotel.isPresent())
            return ResponseEntity.badRequest().body("No hotel exists with id = " + hotelId);

        // Check and return the room.
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isPresent())
            return ResponseEntity.ok(new RoomResponse(room.get()));
        else
            return ResponseEntity.badRequest().body("No room with id = " + roomId + " was found in hotel with id = " + hotelId);
    }

    @PostMapping("/{hotelId}/rooms/{roomId:[\\d]+}/reservation")
    public ResponseEntity<?> reservation(
            @CurrentUser Principal principal,
            @PathVariable(value = "hotelId") Long hotelId,
            @PathVariable(value = "roomId") Long roomId,
            @Valid @RequestBody ReservationRequest reservationRequest
    ) {

       //User currentUser = principal.getUser();

        // Check hotel.
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        if (!hotel.isPresent())
            return ResponseEntity.badRequest().body("No hotel exists with id = " + hotelId);

        // Check room.
        Optional<Room> room = roomRepository.findById(roomId);
        if (!room.isPresent())
            return ResponseEntity.badRequest().body("No room with id = " + roomId + " was found in hotel with id = " + hotelId);

        // TODO na ginetai check kai gia thn twrinh hmera
        // TODO na ginontai validate ta dates na mhn afhnei akyra, px 2019-06-35
        // TODO checkare an to room einai dia8esimo ekeines tis hmeromhnies
        // TODO An einai, tote kane to aparaithto transaction, ftia3e thn nea eggrafh calendar kai kane to reservation


        if(reservationRequest.getEndDate().before(reservationRequest.getStartDate()))
            return ResponseEntity.badRequest().body("End date cannot be before start date!");

        if (!isAvailable(reservationRequest.getStartDate(), reservationRequest.getEndDate(), roomId) ) {
            return ResponseEntity.badRequest().body("The room " + roomId + " is not available these days!");
        }

        return ResponseEntity.ok().body("Available!");

      //  //remove money from renter
        //Double payment = 2.0;  //principal.getUser().getWallet();
       // Double current_balance = currentUser.getWallet().getBalance();

//        Wallet from = currentUser.getWallet();
//        from.setBalance(current_balance - payment);
//
//        currentUser.setWallet(from);
//
//        userRepository.save(principal.getUser());
/*        hotelRepository.transferMoney( hotelId , 2);

        //add money$$ to business wallet

        return ResponseEntity.ok().body("Done!");*/
    }

    private boolean isAvailable(Date startDate, Date endDate, Long roomID) {

        List<Calendar> calendars = calendarRepository.isRoomAvailable(dateCoversion(startDate), dateCoversion(endDate), roomID);

        return calendars.isEmpty();
    }

/*    @RequestMapping("/{hotelId}/rooms")
    ModelAndView redirectToRooms(@PathVariable(value = "hotelId") Long hotelId){
        return new ModelAndView("redirect:/rooms", hotelId);
    }*/


    private LocalDate dateCoversion(Date date){
        return date.toInstant()                  // Convert from legacy class `java.util.Date` (a moment in UTC) to a modern `java.time.Instant` (a moment in UTC).
                .atZone( ZoneId.of( "Europe/Athens" ) )  // Adjust from UTC to a particular time zone, to determine a date. Instantiating a `ZonedDateTime`.
                .toLocalDate();
    }


}


//@RestController
//@Validated
//@RequestMapping("hotels/{hotelId}/rooms")
//public class RoomController {
//
//    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private HotelRepository hotelRepository;
//
//    private final AtomicInteger counter = new AtomicInteger();
//
//    @GetMapping("")
//    public List<Room> getHotelRooms(@PathVariable(value = "hotelId") Long hotelId) {
//
//        return roomRepository.findAllByHotel_id(hotelId);
//    }
//
//    @GetMapping("paged")
//    public PagedResponse<Room> getHotelRoomsPaginated(PagedResponseFilter pagedResponseFilter) {
//
//        return null;
//    }
//
//    @GetMapping("{roomId:[\\d]+}")
//    public ResponseEntity<?> getHotelRoom(@PathVariable(value = "hotelId") Long hotelId, @PathVariable(value = "roomId") Long roomId) {
//
//        // Check if the given hotel exists.
//        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
//        if (!hotel.isPresent())
//            return ResponseEntity.badRequest().body("No hotel exists with id = " + hotelId);
//
//        // Check and return the room.
//        Optional<Room> room = roomRepository.findById(roomId);
//        if (room.isPresent())
//            return ResponseEntity.ok(new RoomResponse(room.get()));
//        else
//            return ResponseEntity.badRequest().body("No room with id = " + roomId + " was found in hotel with id = " + hotelId);
//    }
//
//
//    @GetMapping("{roomId:[\\d]+}/reservation")
//    public ResponseEntity<?> reservation(
//            @CurrentUser Principal principal,
//            @PathVariable(value = "hotelId") Long hotelId,
//            @PathVariable(value = "roomId") Long roomId
//    ) {
//        // Check if the given hotel exists.
//        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
//        if (!hotel.isPresent())
//            return ResponseEntity.badRequest().body("No hotel exists with id = " + hotelId);
//
//        // Check and return the room.
//        Optional<Room> room = roomRepository.findById(roomId);
//        if (room.isPresent()) {
//
//        }
//
//        Double balance = principal.getUser().getWallet().getBalance();
//
//
//        return ResponseEntity.ok(principal);
//    }
//}
