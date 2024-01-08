package com.leondailani.starlingroundup.models;
import com.leondailani.starlingroundup.models.Account;

import java.util.List;

/**
 * Represents the output of an accounts call to the Starling Bank API.
 * This class is used to deserialize account information
 * from JSON responses returned by the API.
 */
public class ClientAccounts {
    private List<Account> accounts;

    // getters and setters
    public List<Account> getAccounts() {
        return accounts;
    }

}
