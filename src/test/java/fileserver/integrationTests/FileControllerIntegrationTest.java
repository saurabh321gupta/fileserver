package fileserver.integrationTests;

import fileserver.controllers.FileController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@SpringBootTest
public class FileControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //Case 1 : Search by Size
    @Test
    public void shouldSearchBySize() throws Exception {
        String searchUrl = new StringBuilder()
                .append(FileController.ENDPOINT)
                .append("/search").toString();

        mockMvc.perform(get(searchUrl, 1).param("page","0").param("pageSize","20").param("size","5615"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value("This image contains text : image 1"))
                .andExpect(jsonPath("$.content[0].url").value("https://image-bucket-1.s3.us-east-2.amazonaws.com/2020-08-27T21%3A25%3A12.864_image%201.jpeg"))
                .andExpect(jsonPath("$.content[0].type").value("JPEG"))
                .andExpect(jsonPath("$.content[0].size").value(5615))
                .andDo(print())
                .andReturn();
    }

    //Case 2 : Search by Description
    @Test
    public void shouldSearchByDescription() throws Exception {
        String searchUrl = new StringBuilder()
                .append(FileController.ENDPOINT)
                .append("/search").toString();

        mockMvc.perform(get(searchUrl, 1).param("page","0").param("pageSize","20").param("description","This image contains text : image 24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value("This image contains text : image 24"))
                .andExpect(jsonPath("$.content[0].url").value("https://image-bucket-1.s3.us-east-2.amazonaws.com/2020-08-27T21%3A27%3A27.460_image%2024.png"))
                .andExpect(jsonPath("$.content[0].type").value("PNG"))
                .andExpect(jsonPath("$.content[0].size").value(1195))
                .andDo(print())
                .andReturn();
    }

    //Case 3 : Search by Type
    @Test
    public void shouldSearchByType() throws Exception {
        String searchUrl = new StringBuilder()
                .append(FileController.ENDPOINT)
                .append("/search").toString();

        mockMvc.perform(get(searchUrl, 1).param("page","0").param("pageSize","20").param("type","PNG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value("This image contains text : image 16"))
                .andExpect(jsonPath("$.content[0].url").value("https://image-bucket-1.s3.us-east-2.amazonaws.com/2020-08-27T21%3A27%3A09.016_image%2016.png"))
                .andExpect(jsonPath("$.content[0].type").value("PNG"))
                .andExpect(jsonPath("$.content[0].size").value(1201))
                .andDo(print())
                .andReturn();
    }

    //Case 4 : Invalid search
    @Test
    public void shouldThrowError() throws Exception {
        String searchUrl = new StringBuilder()
                .append(FileController.ENDPOINT)
                .append("/search").toString();

        mockMvc.perform(get(searchUrl, 1).param("page","0").param("pageSize","20").param("size","Invalid-Search"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    //Case 5 : Upload JPEG
    @Test
    public void shouldUploadJPEGImage() throws Exception {
        String uploadUrl = new StringBuilder()
                .append(FileController.ENDPOINT)
                .append("/upload").toString();

        FileInputStream inputFile = new FileInputStream("src/test/resources/image.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "image.jpeg", "multipart/form-data", inputFile);

        mockMvc.perform(multipart(uploadUrl).file(file).param("description","Image Description"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    //Case 6 : Upload PNG
    @Test
    public void shouldUploadPNGImage() throws Exception {
        String uploadUrl = new StringBuilder()
                .append(FileController.ENDPOINT)
                .append("/upload").toString();

        FileInputStream inputFile = new FileInputStream("src/test/resources/image.png");
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "multipart/form-data", inputFile);

        mockMvc.perform(multipart(uploadUrl).file(file).param("description","Image Description"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    //Case 6 : Upload file with unsupported format
    @Test
    public void shouldThrowErrorWhenTryingToUploadUnsupportedFormat() throws Exception {
        String uploadUrl = new StringBuilder()
                .append(FileController.ENDPOINT)
                .append("/upload").toString();

        FileInputStream inputFile = new FileInputStream("src/test/resources/image.webp");
        MockMultipartFile file = new MockMultipartFile("file", "image.webp", "multipart/form-data", inputFile);

        mockMvc.perform(multipart(uploadUrl).file(file).param("description","Image Description"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }
}
