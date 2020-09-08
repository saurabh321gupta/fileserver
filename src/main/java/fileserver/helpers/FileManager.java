package fileserver.helpers;

import fileserver.exceptions.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Iterator;

public class FileManager {

    public static File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            throw new InvalidFileException(ex.getMessage());
        }
        return file;
    }

    public static String getFormatName(File file) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(file);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        return readers.next().getFormatName().toUpperCase();
    }

    public static String getFileName(URL url) throws UnsupportedEncodingException {
        return URLDecoder.decode(Paths.get(url.getPath()).getFileName().toString(), StandardCharsets.UTF_8.name());
    }

}
