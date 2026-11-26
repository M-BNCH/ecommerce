package fr.boutique.ecommerce.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class ApiException extends ResponseStatusException {
    private ApiException(HttpStatus status, String code, String detail) {
        super(status, detail);
        getBody().setProperty("code", code);
    }

    public static ApiException notFound(String resource, UUID id) {
        return new ApiException(HttpStatus.NOT_FOUND, "resource_not_found", resource + " introuvable (" + id + ").");
    }

    public static ApiException badRequest(String code, String detail) {
        return new ApiException(HttpStatus.BAD_REQUEST, code, detail);
    }

    public static ApiException conflict(String code, String detail) {
        return new ApiException(HttpStatus.CONFLICT, code, detail);
    }
}
