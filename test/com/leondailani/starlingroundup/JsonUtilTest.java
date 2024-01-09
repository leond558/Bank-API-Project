package com.leondailani.starlingroundup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leondailani.starlingroundup.models.SavingsGoalRequest;
import com.leondailani.starlingroundup.utils.JsonUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilTest {

    @Test
    public void testGoalRequestToString() throws JsonProcessingException {
        // Test converting SavingsGoalRequest to JSON string
        SavingsGoalRequest goalRequest = new SavingsGoalRequest("Goal", "GBP", 1000);
        String jsonString = JsonUtil.goalRequestToString(goalRequest);

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"name\":\"Goal\""));
        assertTrue(jsonString.contains("\"currency\":\"GBP\""));
        assertTrue(jsonString.contains("\"minorUnits\":1000"));
    }

    @Test
    public void testAmountRequestToString() throws JsonProcessingException {
        // Test converting a Map to JSON string
        Map<String, Object> amountMap = new HashMap<>();
        amountMap.put("currency", "GBP");
        amountMap.put("minorUnits", 1000);
        String jsonString = JsonUtil.amountRequestToString(amountMap);

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"currency\":\"GBP\""));
        assertTrue(jsonString.contains("\"minorUnits\":1000"));
    }
}
