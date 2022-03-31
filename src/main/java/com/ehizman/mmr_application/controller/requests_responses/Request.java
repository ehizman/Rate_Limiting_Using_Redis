package com.ehizman.mmr_application.controller.requests_responses;



import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.Size;

@Data
@RedisHash("user_request")
public class Request {
    @Size(min = 6, max = 16)
    private String from;
    @Size(min = 6, max = 16)
    private String to;
    @Size(min = 1, max = 120)
    private String text;
}
