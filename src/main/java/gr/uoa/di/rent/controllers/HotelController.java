package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.models.Hotel;
import gr.uoa.di.rent.repositories.HotelRepository;
import gr.uoa.di.rent.services.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/hotels")
public class HotelController {

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

    @Autowired
    private HotelService hotelService;

    @GetMapping("/{hotelId}")
    public Hotel getHotelByID(@PathVariable(value = "hotelId") Long hotelId) {
        return hotelService.findHotelByID(hotelId);
    }

}
