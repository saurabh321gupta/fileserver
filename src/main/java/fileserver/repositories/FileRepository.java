package fileserver.repositories;

import fileserver.models.ImageDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<ImageDetails, Integer>{

}
