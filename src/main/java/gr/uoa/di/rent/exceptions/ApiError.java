package gr.uoa.di.rent.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiError {

    private HttpStatus status;

    private String localizedMessage;

    private List<String> errors;


    public ApiError() {
    }

    public ApiError(HttpStatus badRequest, String localizedMessage, List<String> errors) {
        this.status = badRequest;
        this.localizedMessage = localizedMessage;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
