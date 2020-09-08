package fileserver.exceptions;

public class FileTypeNotSupportedException extends RuntimeException {

    private static final long serialVersionUID = -3972002117625741728L;

    public FileTypeNotSupportedException(String message) {
        super(message);
    }
}
