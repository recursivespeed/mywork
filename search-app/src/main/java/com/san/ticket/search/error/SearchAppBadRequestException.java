package com.san.ticket.search.error;

public class SearchAppBadRequestException extends RuntimeException {

    public SearchAppBadRequestException() {
    }

    public SearchAppBadRequestException(String message) {
        super(message);
    }

    public SearchAppBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchAppBadRequestException(Throwable cause) {
        super(cause);
    }

    public SearchAppBadRequestException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
