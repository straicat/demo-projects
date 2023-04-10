package com.example.tinyurl.util;

public class Base62Encoder {
    public static String encode(long number) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(characters.charAt((int)(number % characters.length())));
            number /= characters.length();
        } while (number > 0);
        return sb.reverse().toString();
    }
}
