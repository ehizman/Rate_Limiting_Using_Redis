package com.ehizman.mmr_application.controller.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginRequest {
    private String authId;
    private String username;
}
