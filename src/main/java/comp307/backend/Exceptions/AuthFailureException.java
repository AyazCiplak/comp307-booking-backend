package comp307.backend.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthFailureException extends RuntimeException{
    public AuthFailureException() {
        super("Invalid token");
    }
}
