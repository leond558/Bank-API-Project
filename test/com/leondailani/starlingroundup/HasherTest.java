package com.leondailani.starlingroundup;

import com.leondailani.starlingroundup.utils.Hasher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the hash storing function.
 */
public class HasherTest {

    private Path tempFilePath;

    @BeforeEach
    public void setUp() throws IOException {
        // Create a temporary file for each test
        tempFilePath = Files.createTempFile("test", ".tmp");
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Delete the temporary file after each test
        Files.deleteIfExists(tempFilePath);
    }

    @Test
    public void testGenerateHash() {
        // Test hash generation
        String hash = Hasher.generateHash("accountUid", ZonedDateTime.now(), ZonedDateTime.now().plusDays(1));
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testStoreAndCheckHash() throws IOException {
        // Test storing a hash and then checking its existence
        String hash = "testHash";
        Hasher.storeHash(hash, tempFilePath.toString());

        assertTrue(Hasher.checkIfHashExists(hash, tempFilePath.toString()));
    }

    @Test
    public void testCheckHashNotExists() throws IOException {
        // Test checking for a hash that does not exist
        String hash = "nonExistentHash";
        assertFalse(Hasher.checkIfHashExists(hash, tempFilePath.toString()));
    }
}
