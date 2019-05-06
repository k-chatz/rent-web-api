package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAll();
    File findById(long id);
}
