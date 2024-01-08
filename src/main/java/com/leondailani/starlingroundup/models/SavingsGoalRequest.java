package com.leondailani.starlingroundup.models;

/*
This class is used to define a Savings Goal that is to be newly created.
It is constructed by passing a name for the savings goal, the currency that the goal is
to be defined in and lastly, the target amount for the goal.
This object is used to provide the requisite information for making the put call to the
Starling Bank API in order to create the Savings Goal.
 */
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
