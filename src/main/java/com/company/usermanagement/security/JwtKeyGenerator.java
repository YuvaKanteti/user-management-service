package com.company.usermanagement.security;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;

public class JwtKeyGenerator {

    public static void main(String[] args) {

        String key = Encoders.BASE64.encode(
                Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256)
                        .getEncoded());

        System.out.println(key);
    }
}
