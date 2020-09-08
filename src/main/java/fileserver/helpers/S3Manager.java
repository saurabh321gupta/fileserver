package fileserver.helpers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;

@Component
public class S3Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Manager.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public URL uploadFile(final File file) {
        LOGGER.info("File upload in progress.");
        try {
            URL url = uploadFileToS3Bucket(bucketName, file);
            LOGGER.info("File upload is completed.");
            return url;
        } catch (final AmazonServiceException ex) {
            LOGGER.info("File upload is failed.");
            LOGGER.error("Error = {} while uploading file.", ex.getMessage());
            throw new AmazonServiceException(ex.getErrorMessage());
        }
    }

    private URL uploadFileToS3Bucket(final String bucketName, final File file) {
        final String fileName = LocalDateTime.now() + "_" + file.getName();
        LOGGER.info("Uploading file with name= " + fileName);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
        amazonS3.putObject(putObjectRequest);
        return amazonS3.getUrl(bucketName, fileName);
    }

    public void deleteFile(final String keyName) {
        LOGGER.info("Deleting file with name= " + keyName);
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, keyName);
        amazonS3.deleteObject(deleteObjectRequest);
        LOGGER.info("File deleted successfully.");
    }
}
