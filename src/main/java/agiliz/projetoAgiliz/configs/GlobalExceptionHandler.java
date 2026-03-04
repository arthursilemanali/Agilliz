package agiliz.projetoAgiliz.configs;

import java.security.SignatureException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.services.MensageriaService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<MensageriaService> handleValidationException(ValidationException ex){
        String message = "E-mail já existente";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MensageriaService<>(message, 400));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<MensageriaService> handleNoResourceFoundException(NoResourceFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensageriaService<>("Recurso não encontrado", 404));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MensageriaService> handleValidationException(IllegalArgumentException ex){
        String message = ex.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensageriaService<>(message, 400));
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<MensageriaService> handleMalformedJwtException(MalformedJwtException ex) {
        String message = "Token inválido ou mal formatado";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MensageriaService<>(message, 401));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<MensageriaService> handleSignatureException(SignatureException ex){
        String message = "Token inválido ou mal formatado, tente se logar novamente ou entre em contato com nossa equipe técnica";

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new MensageriaService<>(message, 401));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MensageriaService handleException(Exception ex) {
        return new MensageriaService<>("Erro interno no servidor.", 500);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public MensageriaService handleAccessDenied(AccessDeniedException ex) {
        return new MensageriaService<>("Acesso negado. Você não tem permissão para acessar este recurso.", 403);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MensageriaService> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensageriaService<>(ex.getMessage() + " Tás usando o método HTTP errado endeota", 400));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<MensageriaService> handleSQLIntegrityConstraintViolationException
    (SQLIntegrityConstraintViolationException ex){

        String error = ex.getMessage() + " " + ex.getSQLState();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new MensageriaService<>(error, 400));
    }

    @ExceptionHandler(ResponseEntityException.class)
    public ResponseEntity<MensageriaService> handlResponseEntityException(ResponseEntityException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(ex.getStatusCode().value()))
                .body(new MensageriaService<>(ex.getReason(), ex.getStatusCode().value()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MensageriaService> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        String message = ex.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensageriaService<>(message, 400));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensageriaService<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        var errors = new HashMap<String, String>();
        String operation = determineOperation(request.getMethod());

        for (FieldError e : ex.getBindingResult().getFieldErrors()) {
            errors.put(e.getField(), e.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensageriaService<>(String.format("Ocorreu um erro ao efetuar a operação %s", operation),
                errors, 400));
    }

    private String determineOperation(String httpMethod) {
        return switch (httpMethod) {
            case "POST" -> "cadastro";
            case "PUT", "PATCH" -> "atualização";
            default -> "Operação desconhecida";
        };
    }
}
