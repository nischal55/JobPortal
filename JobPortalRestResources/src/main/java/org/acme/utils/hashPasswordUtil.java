package org.acme.utils;

import java.security.MessageDigest;
import java.util.Base64;

public class hashPasswordUtil {
    public static String hash(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}
