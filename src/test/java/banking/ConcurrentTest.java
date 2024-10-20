package banking;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ConcurrentTest {

    private ExecutorService service;

    @Before
    public void setUp() {
        service = Executors.newFixedThreadPool(8);
    }

    @Test
    public void concurrentDebitForCharity() throws InterruptedException {
        int amountOfCharities = 8000;
        int amount = 10;

        Bank bank = new Bank();
        int pin = 12345;
        Person person = new Person("Charitable", "Guy", pin);
        long accountNumber = bank.openConsumerAccount(person, pin, amount * amountOfCharities);

        CountDownLatch latch = new CountDownLatch(amountOfCharities);

        for (int i = 0; i < amountOfCharities; i++) {
            service.submit(() -> {
                if (bank.authenticateUser(accountNumber, pin)) {
                    bank.debit(accountNumber, amount);
                }

                latch.countDown();
            });
        }

        assertTrue("Timed out waiting for debits!", latch.await(5, TimeUnit.SECONDS));

        double balance = bank.getBalance(accountNumber);
        assertEquals((person.getFirstName() + " " + person.getLastName())
                     + " does not have an empty account after giving "
                     + amountOfCharities + " donations of " + amount + "EUR!", 0d, balance, 0.0);
    }

    @Test
    public void concurrentConsumerAccounts() {
        Bank bank = new Bank();
        int maxAccounts = 10_000;

        List<AccountIdentity> accounts = IntStream.range(0, maxAccounts).parallel().mapToObj(identifier -> {
            Person accountHolder = new Person("person-" + identifier, "test", identifier);
            return new AccountIdentity(identifier, bank.openConsumerAccount(accountHolder, identifier, identifier));
        }).collect(Collectors.toList());

        for (long i = 1; i <= maxAccounts; i++) {
            long lookFor = i;
            assertTrue("Account id " + i + " was not found!",
                       accounts.stream().anyMatch(ident -> ident.accountId == lookFor));
        }

        for (AccountIdentity ident : accounts) {
            // make a transaction on every account
            Transaction transaction = null;
            try {
                transaction = new Transaction(bank, ident.accountId, ident.identifier);
            } catch (Exception e) {
                fail("Could not authenticate account " + ident.accountId + " with pin: " + ident.identifier);
            }

            // remove balance
            assertTrue(transaction.debit(ident.identifier));

            double balance = bank.getBalance(ident.accountId);
            assertEquals(0d, balance, 0.0);
        }
    }

    @After
    public void tearDown() throws InterruptedException {
        service.shutdown();
        if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
            service.shutdownNow();
        }
    }

    private static class AccountIdentity {
        final Integer identifier;
        final Long accountId;

        AccountIdentity(Integer identifier, Long accountId) {
            this.identifier = identifier;
            this.accountId = accountId;
        }
    }
}
