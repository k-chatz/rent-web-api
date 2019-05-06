package gr.uoa.di.rent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UploadFileException extends RuntimeException {

    public UploadFileException() {
        super();
    }

    public UploadFileException(String message) {
        super(message);
    }

}
