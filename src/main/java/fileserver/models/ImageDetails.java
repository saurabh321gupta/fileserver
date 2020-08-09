package fileserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static fileserver.constants.DBConstants.*;

@Entity
@Table(name = IMAGE_DETAILS_ENTITY_NAME)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = IMAGE_ID_FIELD)
    private Integer id;
    @Column(name = IMAGE_DESCRIPTION_FIELD)
    private String description;
    @Column(name = IMAGE_TYPE_FIELD)
    private String type;
    @Column(name = IMAGE_SIZE_FIELD)
    private Long size;
}