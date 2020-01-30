package com.san.ticket.search.error;

public class SearchAppUnexpectedException extends RuntimeException {

    public SearchAppUnexpectedException() {
    }

    public SearchAppUnexpectedException(String message) {
        super(message);
    }

    public SearchAppUnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchAppUnexpectedException(Throwable cause) {
        super(cause);
    }

    public SearchAppUnexpectedException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
