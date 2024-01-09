package com.leondailani.starlingroundup;

import com.leondailani.starlingroundup.exceptions.BalanceZeroException;
import com.leondailani.starlingroundup.models.Balance;
import com.leondailani.starlingroundup.utils.SufficientFundChecker;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SufficientFundCheckerTest {

    @Test
    public void testSufficientFunds() throws IOException, InterruptedException, BalanceZeroException {
        // Create a balance with sufficient funds
        Balance balance = new Balance();
        Balance.Amount amount = new Balance.Amount();
        amount.setMinorUnits(100); // Set balance to 100 units
        balance.setAmount(amount);

        // Test for sufficient funds
        SufficientFundChecker fundCheck = new SufficientFundChecker(balance);
        int result = fundCheck.calculateFunds( 50);
        assertEquals(50, result);
    }

    @Test
    public void testInsufficientFunds() throws IOException, InterruptedException, BalanceZeroException {
        // Create a balance with insufficient funds
        Balance balance = new Balance();
        Balance.Amount amount = new Balance.Amount();
        amount.setMinorUnits(30); // Set balance to 30 units
        balance.setAmount(amount);

        // Test for insufficient funds
        SufficientFundChecker fundCheck = new SufficientFundChecker(balance);
        int result = fundCheck.calculateFunds( 50);
        assertEquals(30, result);
    }

    @Test
    public void testZeroBalance() throws IOException, InterruptedException {
        // Create a balance with zero funds
        Balance balance = new Balance();
        Balance.Amount amount = new Balance.Amount();
        amount.setMinorUnits(0); // Set balance to 0 units
        balance.setAmount(amount);

        // Test for zero balance
        SufficientFundChecker fundCheck = new SufficientFundChecker(balance);
        assertThrows(BalanceZeroException.class, () ->
                fundCheck.calculateFunds( 50));
    }
}

