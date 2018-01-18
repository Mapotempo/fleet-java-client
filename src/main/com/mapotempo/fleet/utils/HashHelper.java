package com.mapotempo.fleet.utils;

import com.mapotempo.fleet.core.exception.CoreException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashHelper {

    public static String emailHasher(String email) throws CoreException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String test = printHexBinary(md.digest(email.getBytes()));
            //return test.toLowerCase();
            return test.toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new CoreException("ERROR in emailHasher");
        }
    }

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    private static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
}
