package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository  extends JpaRepository<User, Long> {

}
