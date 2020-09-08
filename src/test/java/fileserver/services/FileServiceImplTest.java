package fileserver.services;


import fileserver.dtos.UploadFileResponse;
import fileserver.helpers.S3Manager;
import fileserver.models.ImageDetails;
import fileserver.repositories.FileRepository;
import fileserver.validators.FileValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private S3Manager s3Manager;

    @Mock
    private FileRepository fileRepository;

    @Captor
    private ArgumentCaptor<ImageDetails> imageDetailsArgumentCaptor;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void shouldUploadFileResponse() throws Exception {
        FileInputStream inputFile = new FileInputStream("src/test/resources/image.jpeg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", inputFile);
        String description = "description of the file";
        fileService = spy(fileService);
        File file = new File("src/test/resources/image.jpeg");
        URL url = new URL("https://image-bucket-1.s3.us-east-2.amazonaws.com/image.jpeg");
        doReturn(file).when(fileService).getFile(multipartFile);
        ImageDetails savedImageDetails = new ImageDetails();
        savedImageDetails.setId(1);
        when(fileRepository.save(any(ImageDetails.class))).thenReturn(savedImageDetails);
        when(s3Manager.uploadFile(file)).thenReturn(url);
        doNothing().when(fileService).removeTempFile(file);
        UploadFileResponse uploadFileResponse = fileService.uploadFile(multipartFile, description);
        verify(fileValidator).validate(file);
        verify(fileRepository).save(imageDetailsArgumentCaptor.capture());
        verify(fileService).removeTempFile(file);
        ImageDetails imageDetails = imageDetailsArgumentCaptor.getValue();
        assertEquals(description, imageDetails.getDescription());
        assertEquals(new Long(43592), imageDetails.getSize());
        assertEquals("JPEG", imageDetails.getType());
        assertEquals(url.toString(), imageDetails.getUrl());
        assertEquals(1, uploadFileResponse.getId());
        assertEquals(url.toString(), uploadFileResponse.getUrl());

    }

    @Test
    public void shouldRollbackIfAnyExceptionOccurs() throws Exception {
        ex.expect(Exception.class);
        FileInputStream inputFile = new FileInputStream("src/test/resources/image.jpeg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", inputFile);
        String description = "description of the file";
        fileService = spy(fileService);
        File file = new File("src/test/resources/image.jpeg");
        URL url = new URL("https://image-bucket-1.s3.us-east-2.amazonaws.com/image.jpeg");
        ImageDetails savedImageDetails = new ImageDetails();
        savedImageDetails.setId(1);
        doReturn(file).when(fileService).getFile(multipartFile);
        when(fileRepository.save(any())).thenThrow(NullPointerException.class);
        when(s3Manager.uploadFile(file)).thenReturn(url);
        fileService.uploadFile(multipartFile, description);
        verify(fileValidator).validate(file);
        verify(fileRepository).save(imageDetailsArgumentCaptor.capture());
        verify(s3Manager).deleteFile("image.jpeg");
        verify(fileRepository).delete(savedImageDetails);
        ImageDetails imageDetails = imageDetailsArgumentCaptor.getValue();
        assertEquals(description, imageDetails.getDescription());
        assertEquals(new Long(43592), imageDetails.getSize());
        assertEquals("JPEG", imageDetails.getType());
        assertEquals(url.toString(), imageDetails.getUrl());
    }

    @Test
    public void shouldRollbackIfAnyExceptionOccursWhenUrlIsNull() throws Exception {
        ex.expect(Exception.class);
        FileInputStream inputFile = new FileInputStream("src/test/resources/image.jpeg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", inputFile);
        String description = "description of the file";
        fileService = spy(fileService);
        File file = new File("src/test/resources/image.jpeg");
        URL url = null;
        ImageDetails savedImageDetails = new ImageDetails();
        savedImageDetails.setId(1);
        doReturn(file).when(fileService).getFile(multipartFile);
        when(fileRepository.save(any())).thenThrow(NullPointerException.class);
        when(s3Manager.uploadFile(file)).thenReturn(url);
        fileService.uploadFile(multipartFile, description);
        verify(fileValidator).validate(file);
        verify(fileRepository).save(imageDetailsArgumentCaptor.capture());
        verify(s3Manager, times(0)).deleteFile("image.jpeg");
        verify(fileRepository).delete(savedImageDetails);
        ImageDetails imageDetails = imageDetailsArgumentCaptor.getValue();
        assertEquals(description, imageDetails.getDescription());
        assertEquals(new Long(43592), imageDetails.getSize());
        assertEquals("JPEG", imageDetails.getType());
    }

    @Test
    public void shouldRemoveTempFile() {
        File file = mock(File.class);
        fileService.removeTempFile(file);
        verify(file).delete();
    }

    @Test
    public void shouldGetImageDetails() {
        String description = "description";
        String type = "JPEG";
        Long size = 1024L;
        PageRequest pr = PageRequest.of(0, 20);
        fileService.getImageDetails(description, type, size, pr);
        verify(fileRepository).find(description, type, size, pr);
    }

    @Test
    public void shouldGetFile() throws IOException {
        FileInputStream inputFile = new FileInputStream("src/test/resources/image.jpeg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", inputFile);
        File file = fileService.getFile(multipartFile);
        assertTrue(file.canRead());
        assertEquals(43592, file.length());
        assertEquals("NameOfTheFile", file.getName());
    }
}