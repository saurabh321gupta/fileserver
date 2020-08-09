package fileserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class UploadFileResponse {
    private int id;
    private String description;
    private String type;
    private long size;
}