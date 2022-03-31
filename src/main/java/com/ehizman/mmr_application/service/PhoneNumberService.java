package com.ehizman.mmr_application.service;

import com.ehizman.mmr_application.controller.requests_responses.Request;
import com.ehizman.mmr_application.exceptions.APIException;
import com.ehizman.mmr_application.models.PhoneNumber;

import java.util.Optional;

public interface PhoneNumberService {
    void findByFromParameter(String fromParameter) throws APIException;
    void validate(Request request) throws APIException;
}
