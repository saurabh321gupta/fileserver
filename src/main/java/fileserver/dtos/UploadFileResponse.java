package fileserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UploadFileResponse {
    private int id;
    private String url;
}