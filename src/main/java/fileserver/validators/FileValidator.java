package fileserver.validators;

import fileserver.constants.ErrorMessages;
import fileserver.exceptions.FileTypeNotSupportedException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import static fileserver.constants.Constants.INVALID_FORMAT;
import static fileserver.helpers.FileManager.getFormatName;

@Component
public class FileValidator {

    public static final String FILE_TYPE_JPEG = "JPEG";
    public static final String FILE_TYPE_PNG = "PNG";

    public void validate(File file) throws IOException {
        String formatName;
        try {
            formatName = getFormatName(file);
        } catch (NoSuchElementException e) {
            formatName = FilenameUtils.getExtension(file.getName());
        }
        if (!(formatName.equalsIgnoreCase(FILE_TYPE_JPEG) || formatName.equalsIgnoreCase(FILE_TYPE_PNG))) {
            throw new FileTypeNotSupportedException(String.format(ErrorMessages.PROVIDED_FILE_TYPE_NOT_SUPPORTED, formatName));
        }
    }
}
