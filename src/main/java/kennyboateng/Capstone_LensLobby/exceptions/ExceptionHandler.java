package kennyboateng.Capstone_LensLobby.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponseDTO handleBadRequest(BadRequestException e) {
        return new ErrorsResponseDTO(e.getMessage(), LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsResponseDTO handleNotFound(NotFoundException e) {
        return new ErrorsResponseDTO(e.getMessage(), LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsResponseDTO handleGenericErrors(Exception e) {
        e.printStackTrace();
        return new ErrorsResponseDTO("Problema lato server, lo risolveremo al più presto!", LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsResponseDTO handleNotFound(UnauthorizedException e) {
        return new ErrorsResponseDTO(e.getMessage(), LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsResponseDTO handleForbidden(AuthorizationDeniedException ex) {
        return new ErrorsResponseDTO("Non hai i permessi per accedere", LocalDateTime.now());
    }
}