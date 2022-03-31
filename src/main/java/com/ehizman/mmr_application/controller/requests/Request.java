package com.ehizman.mmr_application.controller.requests;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.Size;

@Data
@RedisHash("user_request")
@AllArgsConstructor
@Builder
public class Request {
    @Size(min = 6, max = 16)
    private String from;
    @Size(min = 6, max = 16)
    private String to;
    @Size(min = 1, max = 120)
    private String text;

    @Override
    public String toString() {
        return "{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
