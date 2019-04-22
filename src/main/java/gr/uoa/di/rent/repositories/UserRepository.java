package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Returns a user with a specific email
    Optional<User> findByEmail(String email);


    // Returns a user with a specific username
    Optional<User> findByUsername(String username);


    // Update the user-data. Returns the numOfRows affected.. so either 1 or 0.
    @Transactional
    @Modifying
    @Query(value="UPDATE users SET username = :username, password = :password, email = :email, name = :name, surname = :surname, birthday = :birthday, photo_profile = :photo_profile WHERE id = :user_id", nativeQuery = true)
    int updateUserData(@Param("user_id") Long user_id, @Param("username") String username, @Param("password") String password, @Param("email") String email, @Param("name") String name, @Param("surname") String surname, @Param("birthday") Date birthday, @Param("photo_profile") String photo_profile);


    // Lock the users of the given list.
    @Transactional
    @Modifying
    @Query(value="UPDATE users SET locked = true WHERE id IN :userIDs", nativeQuery = true)
    int lockUsers(@Param("userIDs") List<Long> userIDs);


    // Unlock the users of the given list.
    @Transactional
    @Modifying
    @Query(value="UPDATE users SET locked = false WHERE id IN :userIDs", nativeQuery = true)
    int unlockUsers(@Param("userIDs") List<Long> userIDs);
}
