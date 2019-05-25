package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenity, Long> {

    Optional<Amenity> findAmenityByName(String amenity);

}
