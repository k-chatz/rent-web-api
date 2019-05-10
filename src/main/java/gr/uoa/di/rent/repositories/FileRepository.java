package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.File;
import gr.uoa.di.rent.models.Hotel;
import gr.uoa.di.rent.models.Room;
import gr.uoa.di.rent.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAll();


    List<File> findAllByUploader(User user);

    void deleteAllByUploader(User user);


    List<File> findAllByHotel(Hotel hotel);

    void deleteAllByHotel(Hotel hotel);


    List<File> findAllByRoom(Room room);

    void deleteAllByRoom(Room room);
}
