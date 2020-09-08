package fileserver.exceptions;

public class InvalidFileException extends RuntimeException {

    private static final long serialVersionUID = -3972002117625741728L;

    public InvalidFileException(String message) {
        super(message);
    }
}
