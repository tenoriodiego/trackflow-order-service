package br.com.trackflow.order.service.order.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class TrackingCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final int CODE_LENGTH = 12;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generate() {

        StringBuilder code = new StringBuilder("TF-");

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
}