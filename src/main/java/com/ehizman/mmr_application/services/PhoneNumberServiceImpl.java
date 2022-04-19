package com.ehizman.mmr_application.services;

import com.ehizman.mmr_application.controller.requests.Request;
import com.ehizman.mmr_application.exceptions.APIException;
import com.ehizman.mmr_application.models.PhoneNumber;
import com.ehizman.mmr_application.repositories.PhoneNumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PhoneNumberServiceImpl implements PhoneNumberService{
    @Autowired
    PhoneNumberRepository phoneNumberRepository;
    @Autowired
    RedisUtility redisUtility;

    @Override
    public void findByPhoneNumberParameter(String fromParameter) throws APIException {
        final Optional<PhoneNumber> phoneNumber = phoneNumberRepository.findByNumber(fromParameter);
        if (phoneNumber.isEmpty()){
            throw new APIException("from parameter not found");
        }
    }

    @Override
    public void validate(Request request) throws APIException {
        if (request.getFrom()==null || request.getFrom().trim().equals("")){
            throw new APIException("from parameter is missing");
        }
        if (request.getTo()==null || request.getTo().trim().equals("")){
            throw new APIException("to parameter is missing");
        }
        if (request.getText()==null || request.getText().trim().equals("")){
            throw new APIException("text parameter is missing");
        }
    }

    @Override
    public void checkText(Request request) {
        if (request.getText().toUpperCase().trim().equals("STOP")){
            String key = String.format("%s:%s", request.getTo(), request.getFrom());
            log.info("Passed this point");

            redisUtility.setValue(key);
            log.info(String.format("Set %s into cache", key));
        }
    }
}
