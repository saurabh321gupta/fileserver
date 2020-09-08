package fileserver.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static fileserver.constants.Constants.*;

@Entity
@Table(name = IMAGE_DETAILS_ENTITY_NAME)
@Getter
@Setter
@NoArgsConstructor
@Generated
public class ImageDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = IMAGE_ID_FIELD)
    private Integer id;
    @NotNull
    @Column(name = IMAGE_DESCRIPTION_FIELD)
    private String description;
    @NotNull
    @Column(name = IMAGE_TYPE_FIELD)
    private String type;
    @NotNull
    @Column(name = IMAGE_SIZE_FIELD)
    private Long size;
    @NotNull
    @Column(name = IMAGE_URL_FIELD)
    private String url;
}