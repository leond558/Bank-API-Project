package com.leondailani.starlingroundup.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Hasher {

    public static String generateHash(String accountUid, ZonedDateTime startOfWeek, ZonedDateTime endOfWeek) {
        try {
            String dataToHash = accountUid + startOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE) + endOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not generate hash", e);
        }
    }

    public static void storeHash(String hash, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(hash);
            writer.newLine();
        }
    }

    public static boolean checkIfHashExists(String hash, String filePath) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            return false;
        }

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return lines.contains(hash);
    }

}
