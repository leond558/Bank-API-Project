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

    /**
     * Generates a SHA-256 hash of the concatenation of accountUid and the start and end of the week dates.
     *
     * @param accountUid   The account UID.
     * @param startOfWeek  The start of the week as a ZonedDateTime.
     * @param endOfWeek    The end of the week as a ZonedDateTime.
     * @return A string representing the generated hash.
     * @throws RuntimeException If the SHA-256 algorithm is not available.
     */
    public static String generateHash(String accountUid, ZonedDateTime startOfWeek, ZonedDateTime endOfWeek) {
        try {
            // Combine accountUid with formatted start and end week dates.
            String dataToHash = accountUid + startOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE) + endOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE);

            // Create a MessageDigest instance for SHA-256.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hash computation.
            byte[] encodedhash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hex string.
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
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

    /**
     * Stores a given hash in a file at the specified file path.
     *
     * @param hash     The hash to be stored.
     * @param filePath The file path where the hash will be stored.
     * @throws IOException If there is an error writing to the file.
     */
    public static void storeHash(String hash, String filePath) throws IOException {
        // Write the hash to the file, appending it if the file already exists.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(hash);
            writer.newLine();
        }
    }

    /**
     * Checks if a given hash exists in a file at the specified path.
     *
     * @param hash     The hash to check for.
     * @param filePath The file path where to look for the hash.
     * @return True if the hash exists in the file, false otherwise.
     * @throws IOException If there is an error reading the file.
     */
    public static boolean checkIfHashExists(String hash, String filePath) throws IOException {
        // Check if file exists before reading.
        if (!Files.exists(Paths.get(filePath))) {
            return false;
        }

        // Read all lines from the file and check if the hash is contained.
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return lines.contains(hash);
    }

}