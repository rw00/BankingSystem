package banking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SampleTest {
    Bank bank;
    /**
     * The account number for Amelia Pond
     */
    Long ameliaPond;
    /**
     * The account number for Rose Tyler
     */
    Long roseTyler;
    /**
     * The account number for Acme Corp
     */
    Long acmeCorp;
    /**
     * The account number for HackerRank
     */
    Long hackerRank;

    @Before
    public void setUp() {
        bank = new Bank();
        Person person1ameliaPond = new Person("Amelia", "Pond", 1);
        Person person2roseTyler = new Person("Rose", "Tyler", 2);
        Company company1acmeCorp = new Company("Acme Corp", 1);
        Company company2hackerRank = new Company("HackerRank", 2);
        ameliaPond = bank.openConsumerAccount(person1ameliaPond, 1111, 0.0);
        roseTyler = bank.openConsumerAccount(person2roseTyler, 2222, 456.78);
        acmeCorp = bank.openCommercialAccount(company1acmeCorp, 1111, 0.0);
        hackerRank = bank.openCommercialAccount(company2hackerRank, 2222, 9876543.21);
    }

    @After
    public void tearDown() {
        bank = null;
        ameliaPond = null;
        roseTyler = null;
        acmeCorp = null;
        hackerRank = null;
    }

    @Test
    public void invalidAccountNumberTest() {
        assertEquals("1st and 2nd accounts were not assigned sequential account numbers.",
                ameliaPond + 1, (long) roseTyler);
        assertEquals("2nd and 3rd accounts were not assigned sequential account numbers.",
                roseTyler + 1, (long) acmeCorp);
        assertEquals("3rd and 4th accounts were not assigned sequential account numbers.",
                acmeCorp + 1, (long) hackerRank);

        assertEquals(0.0, bank.getBalance(ameliaPond), 0);
        assertEquals(456.78, bank.getBalance(roseTyler), 0);
        assertEquals(0.0, bank.getBalance(acmeCorp), 0);
        assertEquals(9876543.21, bank.getBalance(hackerRank), 0);
        assertNotEquals(bank.getBalance(ameliaPond), bank.getBalance(roseTyler));
        assertNotEquals(bank.getBalance(acmeCorp), bank.getBalance(hackerRank));
    }

    @Test
    public void debitTest() {
        double amount = 20.0;
        assertFalse("Account " + ameliaPond + " should have insufficient funds.",
                bank.debit(ameliaPond, amount));
        assertTrue("Account " + roseTyler + " should have sufficient funds.",
                bank.debit(roseTyler, amount));
        assertFalse("Account " + acmeCorp + " should have insufficient funds.",
                bank.debit(acmeCorp, amount));
        assertTrue("Account " + hackerRank + " should have sufficient funds.",
                bank.debit(hackerRank, amount));
    }

    @Test
    public void creditTest() {
        double amount = 23.45;
        double beforeDeposit1 = bank.getBalance(ameliaPond);
        double beforeDeposit2 = bank.getBalance(roseTyler);
        double beforeDeposit3 = bank.getBalance(acmeCorp);
        double beforeDeposit4 = bank.getBalance(hackerRank);
        bank.credit(ameliaPond, amount);
        bank.credit(roseTyler, amount);
        bank.credit(acmeCorp, amount);
        bank.credit(hackerRank, amount);
        assertEquals(beforeDeposit1 + amount, bank.getBalance(ameliaPond), 0);
        assertEquals(beforeDeposit2 + amount, bank.getBalance(roseTyler), 0);
        assertEquals(beforeDeposit3 + amount, bank.getBalance(acmeCorp), 0);
        assertEquals(beforeDeposit4 + amount, bank.getBalance(hackerRank), 0);
    }

    @Test(expected = Exception.class)
    public void invalidPinTransaction() throws Exception {
        new Transaction(bank, ameliaPond, 1234);
    }

    @Test
    public void transactionTest() throws Exception {
        Transaction transaction1 = new Transaction(bank, ameliaPond, 1111);
        double beforeDeposit1 = transaction1.getBalance();
        double amount = 23452.43;
        transaction1.credit(amount);
        assertEquals(beforeDeposit1 + amount, transaction1.getBalance(), 0);
        assertTrue("Debit was unsuccessful.", transaction1.debit(amount));
        assertFalse("This transaction should have overdrawn the account.",
                transaction1.debit(amount));
        assertEquals(beforeDeposit1, transaction1.getBalance(), 0);
        assertEquals(transaction1.getBalance(), bank.getBalance(ameliaPond), 0);
    }
}
