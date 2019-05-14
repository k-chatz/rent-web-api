package gr.uoa.di.rent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfilePhotoException extends RuntimeException {

    public ProfilePhotoException() {
        super();
    }

    public ProfilePhotoException(String message) {
        super(message);
    }

}
