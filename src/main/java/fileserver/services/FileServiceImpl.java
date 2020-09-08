package fileserver.services;

import fileserver.dtos.UploadFileResponse;
import fileserver.helpers.S3Manager;
import fileserver.models.ImageDetails;
import fileserver.repositories.FileRepository;
import fileserver.validators.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import static fileserver.helpers.FileManager.*;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileValidator fileValidator;

    @Autowired
    private S3Manager s3Manager;

    @Override
    public UploadFileResponse uploadFile(MultipartFile multipartFile, String description) throws Exception {
        File file = getFile(multipartFile);
        fileValidator.validate(file);
        URL fileUrl = null;
        ImageDetails savedImageDetails = null;
        try {
            fileUrl = s3Manager.uploadFile(file);
            ImageDetails imageDetails = getImageDetails(file, description, fileUrl.toString());
            savedImageDetails = fileRepository.save(imageDetails);
        } catch (Exception e) {
            rollBackUploadAndMetadata(fileUrl, savedImageDetails);
            throw new Exception(e.getMessage());
        }
        removeTempFile(file);
        return new UploadFileResponse(savedImageDetails.getId(), fileUrl.toString());
    }

    public void removeTempFile(File file) {
        file.delete();
    }

    public File getFile(MultipartFile multipartFile) {
        return convertMultiPartFileToFile(multipartFile);
    }

    @Override
    public Page<ImageDetails> getImageDetails(String description, String type, Long size, PageRequest pageReq) {
        return fileRepository.find(description, type, size, pageReq);
    }

    private void rollBackUploadAndMetadata(URL fileUrl, ImageDetails savedImageDetails) throws UnsupportedEncodingException {
        if (fileUrl != null) {
            s3Manager.deleteFile(getFileName(fileUrl));
        }
        if (savedImageDetails != null) {
            fileRepository.delete(savedImageDetails);
        }
    }

    private ImageDetails getImageDetails(File file, String description, String url) throws IOException {
        ImageDetails imageDetails = new ImageDetails();
        imageDetails.setDescription(description);
        imageDetails.setType(getFormatName(file));
        imageDetails.setSize(file.length());
        imageDetails.setUrl(url);
        return imageDetails;
    }

}
