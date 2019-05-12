package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Reservation;
import gr.uoa.di.rent.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
