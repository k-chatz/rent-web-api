package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Returns a user with a specific email
    Optional<User> findByEmail(String email);

    // Returns a user with a specific username
    Optional<User> findByUsername(String username);

    // Lock the users of the given list.
    @Transactional
    @Modifying
    @Query(value="UPDATE users SET locked = true WHERE id IN :userIDs", nativeQuery = true)
    int lockUsers(@Param("userIDs") List<Long> userIDs);
}
