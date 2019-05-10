package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findById(Long business_id);

    List<Business> findAll();
}
