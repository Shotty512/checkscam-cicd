package com.example.checkscam.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaResponse {
    private boolean success;
    @JsonAlias("challenge_ts")
    private String challengeTs;
    private String hostname;
    @JsonAlias("error-codes")
    private List<String> errorCodes;
    // getters & setters
}
