package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.exceptions.BadRequestException;
import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.payload.requests.HotelRequest;
import gr.uoa.di.rent.payload.requests.filters.PagedHotelsFilter;
import gr.uoa.di.rent.payload.responses.HotelResponse;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.repositories.*;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.Principal;
import gr.uoa.di.rent.services.HotelService;
import gr.uoa.di.rent.util.AppConstants;
import gr.uoa.di.rent.util.ModelMapper;
import gr.uoa.di.rent.util.PaginatedResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
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

    private final HotelRepository hotelRepository;

    private final AtomicInteger counter = new AtomicInteger();

    public HotelController(HotelService hotelService,
                           BusinessRepository businessRepository,
                           HotelRepository hotelRepository) {
        this.hotelService = hotelService;
        this.businessRepository = businessRepository;
        this.hotelRepository = hotelRepository;
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

        // Store the hotel in the database.
        hotel = hotelService.createHotel(hotel);
        // Get the new hotel-object having its id assigned by the database.
        return ResponseEntity.ok(new HotelResponse(hotel));
    }

    @GetMapping("/{hotelId:[\\d]+}")
    public ResponseEntity<?> getHotelByID(@PathVariable(value = "hotelId") Long hotelId) {

        Hotel hotel = hotelService.findHotelByID(hotelId);
        if ( hotel == null ) {
            logger.warn("No hotel exists with id = " + hotelId);
            return ResponseEntity.notFound().build();
        }
        else
            return ResponseEntity.ok(new HotelResponse(hotel));
    }

    @GetMapping("/search")
    public PagedResponse<Hotel> searchHotels(@Valid PagedHotelsFilter pagedHotelsFilters){

        System.out.println("Sort-field is: "+ pagedHotelsFilters.getSort_field());



        try {
            PaginatedResponseUtil.validateParameters( pagedHotelsFilters.getPage(),
                    pagedHotelsFilters.getSize(), pagedHotelsFilters.getSort_field(), Hotel.class);
        } catch (BadRequestException bre) {
            throw bre;
        } catch (Exception e) {
            throw new BadRequestException("Instantiation problem!");
        }

        Sort.Direction sort_order;

        // Default order is ASC, otherwise DESC
        if (AppConstants.DEFAULT_ORDER.equals(pagedHotelsFilters.getOrder()))
            sort_order = Sort.Direction.ASC;
        else
            sort_order = Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(pagedHotelsFilters.getPage(), pagedHotelsFilters.getSize(),
                sort_order, pagedHotelsFilters.getSort_field());


        // TODO: Na allax8oyn ola ta pedia sta models se camel case giati alliws vgazei error -> reproduce with sort_field=description_short
        Page<Hotel> hotels =  hotelRepository.findAll(pageable);

        if (hotels.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), hotels.getNumber(),
                    hotels.getSize(), hotels.getTotalElements(), hotels.getTotalPages(), hotels.isLast());
        }

        List<Hotel> hotelResponses = hotels.map(ModelMapper::mapHoteltoHotelResponse).getContent();

        return new PagedResponse<>(hotelResponses, hotels.getNumber(),
                hotels.getSize(), hotels.getTotalElements(), hotels.getTotalPages(), hotels.isLast());
    }

}