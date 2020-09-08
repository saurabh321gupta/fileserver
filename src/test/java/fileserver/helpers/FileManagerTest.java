package fileserver.helpers;

import fileserver.exceptions.InvalidFileException;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileManagerTest {

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void shouldConvertMultiPartFileToFile() throws IOException {
        FileInputStream inputFile = new FileInputStream("src/test/resources/image.jpeg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", inputFile);
        File file = FileManager.convertMultiPartFileToFile(multipartFile);
        assertEquals("NameOfTheFile", file.getName());
    }

    @Test
    public void shouldGetFormatName() throws IOException {
        File file = new File("src/test/resources/image.jpeg");
        assertEquals("JPEG", FileManager.getFormatName(file));
    }

    @Test
    public void shouldGetFileName() throws MalformedURLException, UnsupportedEncodingException {
        URL url = new URL("https://image-bucket-1.s3.us-east-2.amazonaws.com/2020-08-27T21%3A25%3A12.864_image+1.jpeg");
        assertEquals("2020-08-27T21:25:12.864_image 1.jpeg", FileManager.getFileName(url));
    }

    @Test
    public void shouldThrowExceptionForInvalidFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("invalid-file");
        when(multipartFile.getBytes()).thenThrow(IOException.class);
        ex.expect(InvalidFileException.class);
        File file = FileManager.convertMultiPartFileToFile(multipartFile);
    }
}