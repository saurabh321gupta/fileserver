package fileserver.helpers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class S3ManagerTest {

    @InjectMocks
    private S3Manager s3Manager;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Captor
    ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor;

    @Captor
    ArgumentCaptor<DeleteObjectRequest> deleteObjectRequestArgumentCaptor;

    @Mock
    private AmazonS3 amazonS3;

    @Test
    public void shouldUploadFile() throws MalformedURLException {
        File file = new File("image.jpeg");
        URL expectedUrl = new URL("https://image-bucket-1.s3.us-east-2.amazonaws.com/image.jpeg");
        when(amazonS3.getUrl(eq(bucketName), anyString())).thenReturn(expectedUrl);
        assertEquals(expectedUrl, s3Manager.uploadFile(file));
        verify(amazonS3).putObject(putObjectRequestArgumentCaptor.capture());
        PutObjectRequest putObjectRequest = putObjectRequestArgumentCaptor.getValue();
        assertEquals(bucketName, putObjectRequest.getBucketName());
        assertEquals(file, putObjectRequest.getFile());
        Assert.assertTrue(putObjectRequest.getKey().contains(file.getName()));
    }

    @Test
    public void shouldDeleteFile() {
        String fileName = "image.jpeg";
        s3Manager.deleteFile(fileName);
        verify(amazonS3).deleteObject(deleteObjectRequestArgumentCaptor.capture());
        DeleteObjectRequest deleteObjectRequest = deleteObjectRequestArgumentCaptor.getValue();
        assertEquals(bucketName, deleteObjectRequest.getBucketName());
        assertEquals(fileName, deleteObjectRequest.getKey());
    }

}