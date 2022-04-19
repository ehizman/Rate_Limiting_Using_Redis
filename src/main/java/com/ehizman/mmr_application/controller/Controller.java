package com.ehizman.mmr_application.controller;

import com.ehizman.mmr_application.controller.responses.APIResponse;
import com.ehizman.mmr_application.controller.requests.Request;
import com.ehizman.mmr_application.exceptions.APIException;
import com.ehizman.mmr_application.services.PhoneNumberService;
import com.ehizman.mmr_application.services.RedisUtility;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class Controller {
    @Autowired
    RedisUtility redisUtility;
    @Autowired
    PhoneNumberService phoneNumberService;
    @Autowired
    RedisRateLimiter rateLimiter;

    @PostMapping("/inbound/sms/")
    public ResponseEntity<?> inbound(@Valid @NotNull @RequestBody Request request){
        try{
            phoneNumberService.validate(request);
            phoneNumberService.checkText(request);

            phoneNumberService.findByPhoneNumberParameter(request.getTo());
            return new ResponseEntity<>(new APIResponse("inbound sms ok", ""), HttpStatus.OK);
        } catch(Exception exception){
            log.info("Exception --> {}", exception.getMessage());
            return new ResponseEntity<>(new APIResponse("", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/outbound/sms/")
    public ResponseEntity<?> outbound(@Valid @NotNull @RequestBody Request request){
        try{
            phoneNumberService.validate(request);
            String key = String.format("%s:%s", request.getTo(), request.getFrom());
            redisUtility.getValue(key);
            phoneNumberService.findByPhoneNumberParameter(request.getFrom());
            if (rateLimiter.isLimitExceeded(request.getFrom())){
                throw new APIException(String.format("limit reached for from %s", request.getFrom()));
            }
            return new ResponseEntity<>(new APIResponse("outbound sms ok", ""), HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(new APIResponse("", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = "parameter is invalid";
            errors.put(fieldName, errorMessage);
        }));
        List<APIResponse> errorMessages = new ArrayList<>();
        errors.forEach((key, value)-> errorMessages.add(new APIResponse("", String.format("%s %s", key, value))));
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
}
