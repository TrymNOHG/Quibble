package edu.ntnu.idatt2105.backend.exception;

import edu.ntnu.idatt2105.backend.exception.exists.ExistsException;
import edu.ntnu.idatt2105.backend.exception.notfound.NotFoundException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  In Spring controllers, the ControllerAdvisor acts as a global handler for exceptions. The exceptions are handled
 *  and a ResponseEntity containing the details surrounding the error is returned.
 *
 * @author Trym Hamer Gudvangen
 */
@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistsException.class)
    public ResponseEntity<Object> existsAction(ExistsException e, WebRequest webRequest) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Message: ", e.getMessage());
        body.put("Time of error: ", LocalDateTime.now());
        return ResponseEntity.badRequest().body(body);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> unauthorizedAction(UnauthorizedException e, WebRequest webRequest) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Time of error: ", LocalDateTime.now());
        body.put("Message:", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundAction(NotFoundException e, WebRequest webRequest){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Message: ", e.getMessage());
        body.put("Time of error: ", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * This method handles all other non-specified exceptions.
     *
     * @param e          The actual exception being thrown
     * @param webRequest The web request
     * @return           ResponseEntity with message and status code
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionAction(Exception e, WebRequest webRequest) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Message: ", e.getMessage());
        body.put("Time of error: ", LocalDateTime.now());
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * This method handles global RuntimeExceptions. It then creates a Response Entity with a message of the error and
     * the 500 HTTP error status code.
     *
     * @param e          The actual RuntimeException being thrown
     * @param webRequest The web request
     * @return           ResponseEntity with message and status code
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionAction(RuntimeException e, WebRequest webRequest) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Message: ", e.getMessage());
        body.put("Time of error: ", LocalDateTime.now());
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * This method handles exceptions that occur due to problems with Jwt. The response status code is 400 Bad
     * Request.
     *
     * @param e          The actual Jwt Exception being thrown
     * @param webRequest The web request
     * @return           ResponseEntity with message and status code
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> jwtExceptionAction(ServletException e, WebRequest webRequest) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Message: ", e.getMessage());
        body.put("Time of error: ", LocalDateTime.now());
        return ResponseEntity.badRequest().body(body);
    }

}
