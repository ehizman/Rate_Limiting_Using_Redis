package com.ehizman.mmr_application.service;

import com.ehizman.mmr_application.controller.requests_responses.Request;
import com.ehizman.mmr_application.exceptions.APIException;

public interface PhoneNumberService {
    void findByPhoneNumberParameter(String fromParameter) throws APIException;
    void validate(Request request) throws APIException;
    void checkText(Request request);
}
