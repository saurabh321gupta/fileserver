package fileserver.controllers;

import fileserver.services.FileServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FileControllerTest {

    @InjectMocks
    private FileController fileController;

    @Mock
    private FileServiceImpl fileService;


    @Test
    public void shouldUploadFile() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        String description = "description of file";
        fileController.upload(file, description);
        verify(fileService).uploadFile(file, description);
    }

    @Test
    public void shouldSearch() throws Exception {
        String description = "description of file";
        String type = "JPEG";
        Long size = 1024L;

        Integer page = 0;
        Integer pageSize = 20;

        fileController.search(page, pageSize, description, type, size);
        verify(fileService).getImageDetails(eq(description), eq(type), eq(size), Matchers.any(PageRequest.class));
    }

}