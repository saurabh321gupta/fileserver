package fileserver.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import fileserver.dtos.UploadFileResponse;
import fileserver.helpers.IAMDatabaseAuthenticationTester;
import fileserver.models.ImageDetails;
import fileserver.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public UploadFileResponse uploadFile(MultipartFile multipartFile, String description) throws Exception {
        //IAMDatabaseAuthenticationTester im = new IAMDatabaseAuthenticationTester();
        //im.testConnection();
        ImageDetails imageDetails = new ImageDetails();
        imageDetails.setDescription(description);
        imageDetails.setSize(multipartFile.getSize());
        imageDetails.setType(multipartFile.getContentType());
        ImageDetails savedImageDetails = fileRepository.save(imageDetails);
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            System.out.println("Error converting the multi-part file to file= ");
        }
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .build();
        /*BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIASVXLZU3GIXDI7UVL", "kFFFUIPHcYrLfRK//hc4P/yj6Gl+0GF+Re5mUN9s");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();*/
        s3Client.putObject(new PutObjectRequest("imagekibucket","myfile",file));
        /*return new UploadFileResponse(savedImageDetails.getId(), savedImageDetails.getDescription(),
                savedImageDetails.getType(), savedImageDetails.getSize());*/
        return null;
    }
}
