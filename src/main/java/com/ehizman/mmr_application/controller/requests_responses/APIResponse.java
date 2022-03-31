package com.ehizman.mmr_application.controller.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class APIResponse implements Serializable {
    private String message;
    private String error;

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
