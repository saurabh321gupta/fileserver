package fileserver.repositories;

import fileserver.models.ImageDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<ImageDetails, Integer>, PagingAndSortingRepository<ImageDetails, Integer> {

    @Query("select u from ImageDetails u where " +
            "u.description = IFNull(:description, description) AND " +
            "u.type = IFNull(:type, type) AND " +
            "u.size = IFNull(:size, size)")
    Page<ImageDetails> find(@Param("description") String description,
                            @Param("type") String type,
                            @Param("size") Long size, Pageable pageReq);


}
