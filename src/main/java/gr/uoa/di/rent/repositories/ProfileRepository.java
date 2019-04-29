package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<User, Long> {

}
