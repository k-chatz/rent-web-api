package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findAll();

    Hotel findById(long id);
}
