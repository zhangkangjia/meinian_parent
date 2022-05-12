package com.chd;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCryptPasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123");
        String encode1 = encoder.encode("321");
        System.out.println(encode.length());
        System.out.println(encode);
        System.out.println(encode1);
    }
}
