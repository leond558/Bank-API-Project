package com.leondailani.starlingroundup.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leondailani.starlingroundup.models.SavingsGoalRequest;

import java.util.Map;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJson(String json, Class<T> objectType) throws JsonProcessingException {
        return objectMapper.readValue(json, objectType);
    }

    public static String goalRequestToString(SavingsGoalRequest goal) throws JsonProcessingException {
        return objectMapper.writeValueAsString(goal);
    }

    public static String amountRequestToString(Map<String,Object> amount) throws JsonProcessingException {
        return objectMapper.writeValueAsString(amount);
    }
}
