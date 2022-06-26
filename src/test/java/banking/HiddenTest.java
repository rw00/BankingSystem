package banking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class HiddenTest {
    Bank bank;
    /**
     * The account number for John.
     */
    Long john;
    /**
     * The account number for Julia.
     */
    Long julia;

    /**
     * The account number for Daniel.
     */
    Long daniel;
    /**
     * The account number for company Bob.
     */
    Long bob;
    /**
     * The account number for company Smith.
     */
    Long smith;

    @Before
    public void setUp() {
        bank = new Bank();
        Person person1John = new Person("john", "ken", 1);
        Person person2Julia = new Person("julia", "chi", 2);
        Person person3Daniel = new Person("daniel", "rad", 3);
        Company company1Bob = new Company("bob", 1);
        Company company2Smith = new Company("smith", 2);
        john = bank.openConsumerAccount(person1John, 1111, 0.0);
        julia = bank.openConsumerAccount(person2Julia, 2222, 456.78);
        daniel = bank.openConsumerAccount(person3Daniel, 3333, 500.00);
        bob = bank.openCommercialAccount(company1Bob, 1111, 0.0);
        smith = bank.openCommercialAccount(company2Smith, 2222, 123456789.00);
    }

    @After
    public void tearDown() {
        bank = null;
        john = null;
        julia = null;
        daniel = null;
        bob = null;
        smith = null;
    }

    @Test
    public void invalidAccountNumberTest() {
        assertEquals("1st and 2nd accounts were not assigned sequential account numbers.", john + 1,
                (long) julia);
        assertEquals("2nd and 3rd accounts were not assigned sequential account numbers.",
                julia + 1, (long) daniel);
        assertEquals("3rd and 4th accounts were not assigned sequential account numbers.",
                daniel + 1, (long) bob);
        assertEquals("4rd and 5th accounts were not assigned sequential account numbers.", bob + 1,
                (long) smith);

        assertEquals(0.0, bank.getBalance(john), 0);
        assertEquals(456.78, bank.getBalance(julia), 0);
        assertEquals(500.00, bank.getBalance(daniel), 0);
        assertEquals(0.0, bank.getBalance(bob), 0);
        assertEquals(123456789.00, bank.getBalance(smith), 0);
        assertNotEquals(bank.getBalance(john), bank.getBalance(julia));
        assertNotEquals(bank.getBalance(john), bank.getBalance(daniel));
        assertNotEquals(bank.getBalance(bob), bank.getBalance(smith));
    }

    @Test
    public void debitTest() {
        double amount = 200.0;
        assertFalse("Account " + john + " should have insufficient funds.",
                bank.debit(john, amount));
        assertTrue("Account " + julia + " should have sufficient funds.",
                bank.debit(julia, amount));
        assertTrue("Account " + daniel + " should have sufficient funds.",
                bank.debit(daniel, amount));
        assertFalse("Account " + bob + " should have insufficient funds.", bank.debit(bob, amount));
        assertTrue("Account " + smith + " should have sufficient funds.",
                bank.debit(smith, amount));
    }

    @Test
    public void creditTest() {
        double amount = 500.00;
        double beforeDeposit1 = bank.getBalance(john);
        double beforeDeposit2 = bank.getBalance(julia);
        double beforeDeposit3 = bank.getBalance(daniel);
        double beforeDeposit4 = bank.getBalance(bob);
        double beforeDeposit5 = bank.getBalance(smith);
        bank.credit(john, amount);
        bank.credit(julia, amount);
        bank.credit(daniel, amount);
        bank.credit(bob, amount);
        bank.credit(smith, amount);
        assertEquals(beforeDeposit1 + amount, bank.getBalance(john), 0);
        assertEquals(beforeDeposit2 + amount, bank.getBalance(julia), 0);
        assertEquals(beforeDeposit3 + amount, bank.getBalance(daniel), 0);
        assertEquals(beforeDeposit4 + amount, bank.getBalance(bob), 0);
        assertEquals(beforeDeposit5 + amount, bank.getBalance(smith), 0);
    }

    @Test(expected = Exception.class)
    public void invalidPinTransaction() throws Exception {
        new Transaction(bank, john, 9999);
    }

    @Test
    public void transactionTest1() throws Exception {
        Transaction transaction1 = new Transaction(bank, daniel, 3333);
        double beforeDeposit1 = transaction1.getBalance();
        double amount = 1000000.23;
        transaction1.credit(amount);
        assertEquals(beforeDeposit1 + amount, transaction1.getBalance(), 0);
        assertTrue("Debit was unexpectedly unsuccessful.", transaction1.debit(amount));
        assertFalse("This transaction should have overdrawn the account.",
                transaction1.debit(amount));
        assertEquals(beforeDeposit1, transaction1.getBalance(), 0);
        assertEquals(transaction1.getBalance(), bank.getBalance(daniel), 0);
    }

    @Test
    public void transactionTest2() throws Exception {
        Transaction t2 = new Transaction(bank, john, 1111);
        double beforeDeposit2 = t2.getBalance();
        double amount = 19239.34;
        t2.credit(amount);
        assertEquals(beforeDeposit2 + amount, t2.getBalance(), 0);
        assertTrue("Debit was unexpectedly unsuccessful.", t2.debit(amount));
        assertEquals(beforeDeposit2, t2.getBalance(), 0);
        assertEquals(t2.getBalance(), bank.getBalance(john), 0);
    }
}
