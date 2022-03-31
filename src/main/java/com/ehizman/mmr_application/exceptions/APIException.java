package com.ehizman.mmr_application.exceptions;

import com.ehizman.mmr_application.controller.requests_responses.APIResponse;

public class APIException extends Exception {

    public APIException(String message) {
        super(message);
    }
}
