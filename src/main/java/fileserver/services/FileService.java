package fileserver.services;

import fileserver.dtos.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    UploadFileResponse uploadFile(MultipartFile file, String description) throws Exception;
}
