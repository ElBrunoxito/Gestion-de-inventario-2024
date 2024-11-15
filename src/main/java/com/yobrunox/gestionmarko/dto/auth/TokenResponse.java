package com.yobrunox.gestionmarko.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String access_token;
    private String refresh_token;
    private Date expires_in;
}
