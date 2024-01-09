package com.leondailani.starlingroundup;

import com.leondailani.starlingroundup.models.Account;
import com.leondailani.starlingroundup.models.ClientAccounts;
import com.leondailani.starlingroundup.utils.AccountSelector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class AccountSelectorTest {

    @Test
    public void testSelectValidAccount() {
        // Setup phase: creating a ClientAccounts instance with two accounts.
        // Each account is given a unique accountUid for identification.
        ClientAccounts clientAccounts = new ClientAccounts();
        Account account1 = new Account();
        account1.setAccountUid("uid1");
        Account account2 = new Account();
        account2.setAccountUid("uid2");
        clientAccounts.setAccounts(Arrays.asList(account1, account2)); // Add both accounts to ClientAccounts

        // Execute phase: calling the selectAccount method with a valid index (1).
        Account selectedAccount = AccountSelector.selectAccount(clientAccounts, 1);

        // Assert phase: verifying that the method returns the account with accountUid "uid2".
        assertEquals("uid2", selectedAccount.getAccountUid());
    }

    @Test
    public void testSelectInvalidAccount() {
        // Setup phase: similar setup as the previous test, but will test invalid index scenario.
        ClientAccounts clientAccounts = new ClientAccounts();
        Account account1 = new Account();
        account1.setAccountUid("uid1");
        Account account2 = new Account();
        account2.setAccountUid("uid2");
        clientAccounts.setAccounts(Arrays.asList(account1, account2));

        // Execute phase: calling the selectAccount method with an invalid index (10).
        // This index is out of bounds for the accounts list.
        Account selectedAccount = AccountSelector.selectAccount(clientAccounts, 10);

        // Assert phase: verifying that the method returns the first account (accountUid "uid1")
        // as per the catch block in the AccountSelector class.
        assertEquals("uid1", selectedAccount.getAccountUid());
    }
}
