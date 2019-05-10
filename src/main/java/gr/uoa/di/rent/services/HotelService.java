package gr.uoa.di.rent.services;


import gr.uoa.di.rent.models.Hotel;
import gr.uoa.di.rent.models.Profile;
import gr.uoa.di.rent.repositories.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HotelService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel findHotelByID(long id)
    {
        return hotelRepository.findById(id);
    }
}
