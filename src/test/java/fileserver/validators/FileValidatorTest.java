package fileserver.validators;

import fileserver.exceptions.FileTypeNotSupportedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static fileserver.constants.ErrorMessages.PROVIDED_FILE_TYPE_NOT_SUPPORTED;

@RunWith(MockitoJUnitRunner.class)
public class FileValidatorTest {

    @InjectMocks
    private FileValidator fileValidator;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void shouldValidateJPEGFile() throws IOException {
        File file = new File("src/test/resources/image.jpeg");
        fileValidator.validate(file);
    }

    @Test
    public void shouldValidatePNGFile() throws IOException {
        File file = new File("src/test/resources/image.png");
        fileValidator.validate(file);
    }

    @Test
    public void shouldThrowExceptionForInvalidFileFormat() throws IOException {
        ex.expect(FileTypeNotSupportedException.class);
        ex.expectMessage(String.format(PROVIDED_FILE_TYPE_NOT_SUPPORTED, "webp"));
        File file = new File("src/test/resources/image.webp");
        fileValidator.validate(file);
    }
}