package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.exceptions.ApiError;
import gr.uoa.di.rent.exceptions.NotAuthorizedException;
import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.payload.requests.ReservationRequest;
import gr.uoa.di.rent.payload.requests.RoomRequest;
import gr.uoa.di.rent.payload.requests.filters.PagedResponseFilter;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.payload.responses.RoomResponse;
import gr.uoa.di.rent.repositories.*;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@Validated
@RequestMapping("/hotels/{hotelId}/rooms")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomRepository roomRepository;

    private final HotelRepository hotelRepository;

    private final CalendarRepository calendarRepository;

    private final TransactionRepository transactionRepository;

    private final AtomicInteger counter = new AtomicInteger();

    public RoomController(RoomRepository roomRepository, HotelRepository hotelRepository,
                          CalendarRepository calendarRepository, TransactionRepository transactionRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.calendarRepository = calendarRepository;
        this.transactionRepository = transactionRepository;

    }

    @GetMapping("")
    public List<Room> getHotelRooms(@PathVariable(value = "hotelId") Long hotelId) {

        return roomRepository.findAllByHotel_id(hotelId);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<?> insertRooms(
            @PathVariable(value = "hotelId") Long hotelId,
            @Valid @CurrentUser Principal current_user,
            @Valid @RequestBody List<RoomRequest> requests) {

        // Check if the given hotel exists.
        Optional<Hotel> hotel_opt = hotelRepository.findById(hotelId);
        if (!hotel_opt.isPresent())
            return ResponseEntity.badRequest().body("No hotel exists with id = " + hotelId);

        Hotel hotel = hotel_opt.get();

        // Check if the current-user is the hotel-owner or if it's the admin, otherwise throw a "NotAuthorizedException".
        if ( !current_user.getUser().getId().equals(hotel.getBusiness().getProvider_id())
                && !current_user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) )
            throw new NotAuthorizedException("You are not authorized to add rooms in hotel " + hotel.getName() + " !");

        hotel.setNumberOfRooms(hotel.getNumberOfRooms() + requests.size());

        for (RoomRequest req : requests) {
            //check if room# already exists
            //todo

            Room r = new Room(req.getRoom_number(), hotelId, req.getCapacity(), req.getPrice());
            r.setHotel(hotel);

            roomRepository.save(r);
        }

        return ResponseEntity.created(null).body("Rooms successfully created!");
    }

    @GetMapping("/paged")
    public PagedResponse<Room> getHotelRoomsPaginated(PagedResponseFilter pagedResponseFilter) {

        return null;
    }

    @GetMapping("/{roomId:[\\d]+}")
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

    @PostMapping("/{roomId:[\\d]+}/reservation")
    @PreAuthorize("hasRole('USER') or hasRole('PROVIDER')")
    public ResponseEntity<?> reservation(
            @CurrentUser Principal principal, @PathVariable(value = "hotelId") Long hotelId,
            @PathVariable(value = "roomId") Long roomId, @Valid @RequestBody ReservationRequest reservationRequest
    ) {

        // Check if hotel exists
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        if (!hotel.isPresent())
            return ResponseEntity.badRequest().body(
                    new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "Hotel does not exist!",
                            Collections.singletonList("No hotel was found with id " + hotelId)));

        // Check if room exists.
        Optional<Room> room = roomRepository.findById(roomId);
        if (!room.isPresent())
            return ResponseEntity.badRequest().body(
                    new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "The requested room was not found in the requested hotel!",
                            Collections.singletonList("Room with id " + roomId + " was not found in hotel with id " + hotelId)));

        // Check if valid date format given
        if (reservationRequest.getEndDate().isBefore(reservationRequest.getStartDate()))
            return ResponseEntity.badRequest().body(
                    new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "Check-out day cannot be before Check-in day!",
                            Collections.singletonList("Check-out day " + reservationRequest.getEndDate() +
                                    " cannot be before Check-in day " + reservationRequest.getStartDate())));

        // Check if reservation is for 0 days   ( ex:  from  30/6/2019 to  30/6/2019 )
        if (reservationRequest.getEndDate().isEqual(reservationRequest.getStartDate()))
            return ResponseEntity.badRequest().body(
                    new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "Cannot reserve a room for 0 Days!",
                            Collections.singletonList("Check-out date " + reservationRequest.getEndDate() +
                                    " is the same as Check-in date " + reservationRequest.getStartDate())));

        // Check calendar (if already reserved one of these days)
        if (!isAvailable(reservationRequest.getStartDate(), reservationRequest.getEndDate(), roomId))
            return ResponseEntity.badRequest().body(
                    new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "The requested room is not available these days!",
                            Collections.singletonList("Room " + roomId + " of hotel " + hotelId +
                                    " is not available all the days between " + reservationRequest.getStartDate() + " and " +
                                    reservationRequest.getEndDate())));


        int total_price = (int) (room.get().getPrice() * DAYS.between(reservationRequest.getStartDate(), reservationRequest.getEndDate()));

        Transaction transaction = new Transaction(principal.getUser(), hotel.get().getBusiness(), total_price);

        Calendar calendar = new Calendar(
                reservationRequest.getStartDate(),
                reservationRequest.getEndDate(),
                null, room.get()
        );
        Reservation reservation = new Reservation(room.get(), null, calendar);

        calendar.setReservation(reservation);

        Transaction transaction_s = transactionRepository.save(transaction);
        reservation.setTransaction(transaction_s);

        calendarRepository.save(calendar);

        //execute sql query to subtract money from user, and add said money to business' wallet + admin's business wallet
        hotelRepository.transferMoney(principal.getUser().getId(), hotelId, (double) total_price);

        return ResponseEntity.ok().body("Room Successfully Booked!");
    }

    private boolean isAvailable(LocalDate startDate, LocalDate endDate, Long roomID) {
        List<Calendar> calendars = calendarRepository.getOverlappingCalendars(startDate, endDate, roomID);
        return calendars.isEmpty();
    }
}
