package com.ll.exam.FinalProject_KimArum.app.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final SecretKey jwtSecretKey;

    public SecretKey getSecretKey() {
        return jwtSecretKey;
    }
}
