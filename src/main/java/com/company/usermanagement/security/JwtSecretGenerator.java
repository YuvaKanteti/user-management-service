package com.company.usermanagement.security;

import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class JwtSecretGenerator {

    public static void main(String[] args) throws Exception {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(256);

        SecretKey secretKey = keyGenerator.generateKey();

        String encoded = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println(encoded);
    }
}