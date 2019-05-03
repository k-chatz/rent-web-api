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

@Repository
public interface ProfileRepository extends JpaRepository<User, Long> {

    // Update the "picture" of a user.
    @Transactional
    @Modifying
    @Query(value="UPDATE profiles SET name = :name, surname = :surname, birthday = :birthday, photo_url = :photo_url WHERE owner = :owner_id", nativeQuery = true)
    int updateProfile(@Param("owner_id") Long owner_id, @Param("name") String name, @Param("surname") String surname, @Param("birthday") Date birthday, @Param("photo_url") String photo_url);
    // Returns the numOfRows affected.. so either 1 or 0.


    // Update the "picture" of a user.
    @Transactional
    @Modifying
    @Query(value="UPDATE profiles SET photo_url = :profile_url WHERE owner = :owner_id", nativeQuery = true)
    int updatePictureById(@Param("owner_id") Long owner_id, @Param("profile_url") String profile_url); // Returns the numOfRows affected.. so either 1 or 0.


    // Get the picture of a user.
    @Query(value="SELECT photo_url FROM profiles WHERE owner = :owner_id", nativeQuery = true)
    List<String> getPictureById(@Param("owner_id") Long owner_id); // Returns the numOfRows affected.. so either 1 or 0.
}
