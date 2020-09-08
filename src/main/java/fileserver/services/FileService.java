package fileserver.services;

import fileserver.dtos.UploadFileResponse;
import fileserver.models.ImageDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    UploadFileResponse uploadFile(MultipartFile file, String description) throws Exception;

    Page<ImageDetails> getImageDetails(String description, String type, Long size, PageRequest pageReq);

}
