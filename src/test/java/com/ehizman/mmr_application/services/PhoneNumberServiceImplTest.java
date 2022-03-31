package com.ehizman.mmr_application.services;

import com.ehizman.mmr_application.controller.requests.Request;
import com.ehizman.mmr_application.exceptions.APIException;
import com.ehizman.mmr_application.repositories.PhoneNumberRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhoneNumberServiceImplTest {

    @Mock
    PhoneNumberRepository phoneNumberRepository;

    @Captor
    ArgumentCaptor<String> redisKeyArgumentCaptor;

    @InjectMocks
    PhoneNumberServiceImpl phoneNumberService;

    @Mock
    RedisUtility redisUtility;

    @Test
    void findByPhoneNumberParameter_assertThrowsExceptionWhenPhoneNumberIsNotInDatabase() {
        when(phoneNumberRepository.findByNumber("4646767747774"))
                .thenReturn(Optional.empty());
        assertThrows(APIException.class,
                ()-> phoneNumberService.findByPhoneNumberParameter("4646767747774"),
                "from parameter not found");
    }

    @Test
    void validate_assertThrowsExceptionWhenFromFieldInRequestObjectIsNotAvailable() {
        Request request = Request.builder()
                                .from("")
                                .to("4924195509198")
                                .text("Hello")
                                .build();
        assertThrows(APIException.class, ()->phoneNumberService.validate(request), "from parameter is missing");
    }

    @Test
    void validate_asserThrowsExceptionWhenToFieldInRequestObjectIsNotAvailable() {
        Request request = Request.builder()
                .from("4924195509198")
                .to("")
                .text("Hello")
                .build();
        assertThrows(APIException.class, ()->phoneNumberService.validate(request), "to parameter is missing");
    }

    @Test
    void validate_asserThrowsExceptionWhenTextFieldInRequestObjectIsNotAvailable() {
        Request request = Request.builder()
                .from("4924195509198")
                .to("49241955091")
                .text("")
                .build();
        assertThrows(APIException.class, ()->phoneNumberService.validate(request), "text parameter is missing");
    }

    @Test
    void checkText(){
        Request request = Request.builder()
                .from("4924195509198")
                .to("49241955091")
                .text("STOP\r ")
                .build();
        doNothing().when(redisUtility).setValue(redisKeyArgumentCaptor.capture());
        phoneNumberService.checkText(request);
        String key = redisKeyArgumentCaptor.getValue();
        assertEquals("49241955091:4924195509198", key);
    }
}