package fileserver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Generated
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}
