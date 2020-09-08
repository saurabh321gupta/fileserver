package fileserver.repositories;

import fileserver.models.ImageDetails;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void shouldfindByFileDescription() {
        ImageDetails imageDetails = new ImageDetails();
        imageDetails.setUrl("https://www.aws.com/image.jpeg");
        imageDetails.setDescription("description of the image");
        imageDetails.setType("JPEG");
        imageDetails.setSize(102400L);
        ImageDetails savedImageDetails = entityManager.persist(imageDetails);
        List<ImageDetails> resultSet = fileRepository.find("description of the image", null, null, PageRequest.of(0, 20)).getContent();
        ImageDetails result = resultSet.get(0);
        Assert.assertEquals(1, resultSet.size());
        Assert.assertEquals(savedImageDetails, result);
    }

    @Test
    public void shouldfindByFileSize() {
        ImageDetails imageDetails = new ImageDetails();
        imageDetails.setUrl("https://www.aws.com/image.jpeg");
        imageDetails.setDescription("description of the image");
        imageDetails.setType("JPEG");
        imageDetails.setSize(102400L);
        ImageDetails savedImageDetails = entityManager.persist(imageDetails);
        List<ImageDetails> resultSet = fileRepository.find(null, null, 102400L, PageRequest.of(0, 20)).getContent();
        ImageDetails result = resultSet.get(0);
        Assert.assertEquals(1, resultSet.size());
        Assert.assertEquals(savedImageDetails, result);
    }

    @Test
    public void shouldfindByFileType() {
        ImageDetails imageDetails = new ImageDetails();
        imageDetails.setUrl("https://www.aws.com/image.jpeg");
        imageDetails.setDescription("description of the image");
        imageDetails.setType("JPEG");
        imageDetails.setSize(102400L);
        ImageDetails savedImageDetails = entityManager.persist(imageDetails);
        ImageDetails result = fileRepository.find(null, "JPEG", null, PageRequest.of(0, 20)).getContent().get(16);
        Assert.assertEquals(savedImageDetails, result);
    }

}