package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByHotel_id(Long hotelId);

    Optional<Room> findById(Long roomId);

}
