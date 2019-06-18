package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findById(Long business_id);

    List<Business> findAll();

    List<Business> findAllByProviderId(Long id);

    Page<Business> findAllByProviderId(Pageable pageable, Long id);
}
