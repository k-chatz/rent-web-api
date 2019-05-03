package gr.uoa.di.rent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NO_CONTENT)
public class ProfilePhotoException extends RuntimeException {

    public ProfilePhotoException() {
        super();
    }

    public ProfilePhotoException(String message) {
        super(message);
    }

}
