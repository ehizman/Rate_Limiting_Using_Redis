package com.ehizman.mmr_application.services;

import com.ehizman.mmr_application.controller.requests.Request;
import com.ehizman.mmr_application.exceptions.APIException;

public interface PhoneNumberService {
    void findByPhoneNumberParameter(String fromParameter) throws APIException;
    void validate(Request request) throws APIException;
    void checkText(Request request);
}
