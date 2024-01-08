package com.leondailani.starlingroundup;

public class SavingsGoalRequest {
    private String name;
    private String currency;
    private Target target;
    private String base64EncodedPhoto;

    public SavingsGoalRequest(String name, String currency, int targetAmount) {
        this.name = name;
        this.currency = currency;
        this.target = new Target(currency, targetAmount);
        this.base64EncodedPhoto = null; // or some valid base64 string
    }

    // Public getters (and setters if needed)
    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public Target getTarget() {
        return target;
    }

    public String getBase64EncodedPhoto() {
        return base64EncodedPhoto;
    }

    // Nested class for 'target'
    public static class Target {
        private String currency;
        private int minorUnits;

        public Target(String currency, int minorUnits) {
            this.currency = currency;
            this.minorUnits = minorUnits;
        }

        // Public getters
        public String getCurrency() {
            return currency;
        }

        public int getMinorUnits() {
            return minorUnits;
        }
    }
}
