package fileserver.exceptions;

import com.amazonaws.AmazonServiceException;
import fileserver.constants.ErrorMessages;
import fileserver.models.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = FileTypeNotSupportedException.class)
    public ResponseEntity<Object> handleUnsupportedException(FileTypeNotSupportedException exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidFileException.class)
    public ResponseEntity<Object> handleInvalidFileException(InvalidFileException exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false)), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = AmazonServiceException.class)
    public ResponseEntity<Object> handleAmazonServiceException(AmazonServiceException exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false)), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleOtherExceptions(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
