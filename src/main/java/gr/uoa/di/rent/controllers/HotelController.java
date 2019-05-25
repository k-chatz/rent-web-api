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
import gr.uoa.di.rent.util.AppConstants;
import gr.uoa.di.rent.util.ModelMapper;
import gr.uoa.di.rent.util.PaginatedResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/hotels")
public class HotelController {

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

    private final BusinessRepository businessRepository;

    private final HotelRepository hotelRepository;

    private final AmenitiesRepository amenitiesRepository;

    private final AtomicInteger counter = new AtomicInteger();

    public HotelController(BusinessRepository businessRepository,
                           HotelRepository hotelRepository,
                           AmenitiesRepository amenitiesRepository) {
        this.businessRepository = businessRepository;
        this.hotelRepository = hotelRepository;
        this.amenitiesRepository = amenitiesRepository;

    }


    @PostMapping("")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<?> createHotel(@Valid @RequestBody HotelRequest hotelRequest, @Valid @CurrentUser Principal principal) {

        User provider = principal.getUser();

        /* Get business for this provider (principal). */
        Optional<Business> business_opt = businessRepository.findById(provider.getBusiness().getId());
        if (!business_opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No business was found for provider with username: " + provider.getUsername());
        }

        logger.debug(business_opt.get().toString());

        Business business = business_opt.get();

        /* Create a hotel object which will belong to that business. */
        Hotel hotel = hotelRequest.asHotel(business.getId());
        hotel.setBusiness(business);

        List<Room> rooms = new ArrayList<>();
        hotel.setRooms(rooms);

        List<File> hotel_photos = new ArrayList<>();
        hotel.setHotel_photos(hotel_photos);

        /* Get list of amenities from request */
        Collection<Amenity> amenities = amenitiesRepository.findAll()
                .stream()
                .filter(amenity -> hotelRequest.getAmenities().contains(amenity.getName()))
                .collect(Collectors.toList());

        hotel.setAmenities(amenities);

        /* Store the hotel in the database. */
        hotel = hotelRepository.save(hotel);

        return ResponseEntity.ok(new HotelResponse(hotel));
    }

    @GetMapping("/{hotelId:[\\d]+}")
    public ResponseEntity<?> getHotelByID(@PathVariable(value = "hotelId") Long hotelId) {

        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        if (!hotel.isPresent()) {
            logger.warn("No hotel exists with id = " + hotelId);
            return ResponseEntity.notFound().build();
        } else
            return ResponseEntity.ok(new HotelResponse(hotel.get()));
    }

    @GetMapping("/search")
    public PagedResponse<Hotel> searchHotels(@Valid PagedHotelsFilter pagedHotelsFilters) {

        try {
            PaginatedResponseUtil.validateParameters(pagedHotelsFilters.getPage(), pagedHotelsFilters.getSize(),
                    pagedHotelsFilters.getSort_field(), Hotel.class);
        } catch (BadRequestException bre) {
            throw bre;
        } catch (Exception e) {
            throw new BadRequestException("Instantiation problem!");
        }

        Sort.Direction sort_order;

        /* Default order is ASC, otherwise DESC */
        if (AppConstants.DEFAULT_ORDER.equals(pagedHotelsFilters.getOrder()))
            sort_order = Sort.Direction.ASC;
        else
            sort_order = Sort.Direction.DESC;

        /* Create a list with all the amenity filters that are to be applied */
        Field[] fields = pagedHotelsFilters.getClass().getDeclaredFields();

        List<String> queryAmenities = new ArrayList<>();
        Arrays.stream(fields)
                .filter(field -> {
                            field.setAccessible(true);
                            try {
                                return field.get(pagedHotelsFilters).equals(true) && AppConstants.amenity_names.contains(field.getName());
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                throw new BadRequestException("An error occurred accessing a field when trying to create the amenity filter list!");
                            }
                        }
                )
                .map(Field::getName)
                .forEach(queryAmenities::add);

        Pageable pageable = PageRequest.of(pagedHotelsFilters.getPage(), pagedHotelsFilters.getSize(),
                sort_order, pagedHotelsFilters.getSort_field());

        double radius_km = 1000000000000000.0;

        /* Get All Hotels  */
        Page<Hotel> hotels;

        /* If no amenities were given, search only with the basic filters */
        //TODO Add price range and rating filters to the sql query.
        if (!queryAmenities.isEmpty()) {
            hotels = hotelRepository.findWithFilters(
                    pagedHotelsFilters.getStart_date(), pagedHotelsFilters.getEnd_date(),
                    pagedHotelsFilters.getLng(), pagedHotelsFilters.getLat(), radius_km,
                    pagedHotelsFilters.getVisitors(),
                    queryAmenities, queryAmenities.size(),
                    pageable);
        } else {
            hotels = hotelRepository.findAll(pageable);
        }

        if (hotels.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), hotels.getNumber(),
                    hotels.getSize(), hotels.getTotalElements(), hotels.getTotalPages(), hotels.isLast());
        }

        List<Hotel> hotelResponses = hotels.map(ModelMapper::mapHoteltoHotelResponse).getContent();

        return new PagedResponse<>(hotelResponses, hotels.getNumber(),
                hotels.getSize(), hotels.getTotalElements(), hotels.getTotalPages(), hotels.isLast());
    }

}