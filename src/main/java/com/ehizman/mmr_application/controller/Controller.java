package com.ehizman.mmr_application.controller;

import com.ehizman.mmr_application.controller.requests_responses.APIResponse;
import com.ehizman.mmr_application.controller.requests_responses.Request;
import com.ehizman.mmr_application.exceptions.APIException;
import com.ehizman.mmr_application.service.PhoneNumberService;
import com.ehizman.mmr_application.service.RedisUtility;
import com.ehizman.mmr_application.services.RedisRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("API/")
public class Controller {
    @Autowired
    RedisUtility redisUtility;
    @Autowired
    PhoneNumberService phoneNumberService;
    @Autowired
    RedisRateLimiter rateLimiter;

    @PostMapping("/outbound/sms/")
    public APIResponse outbound(@Valid @NotNull @RequestBody Request request) throws APIException {
        try{
            phoneNumberService.validate(request);
            String key = String.format("%s:%s", request.getTo(), request.getFrom());
            redisUtility.getValue(key);
            log.info("from --> {}", request.getFrom());
            phoneNumberService.findByFromParameter(request.getFrom());
            if (rateLimiter.isLimitExceeded(request.getFrom())){
                throw new APIException(String.format("limit reached for from %s", request.getFrom()));
            }
            return new APIResponse("outbound sms ok", "");
        } catch (Exception exception){
            return new APIResponse("", exception.getMessage());
        }
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = "is invalid";
            errors.put(fieldName, errorMessage);
        }));
        List<APIResponse> errorMessages = new ArrayList<>();
        errors.forEach((key, value)-> errorMessages.add(new APIResponse("", String.format("%s %s", key, value))));
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
}
