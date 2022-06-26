package banking;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Bank implements BankInterface {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ConcurrentHashMap<Long, Account> accounts;

    public Bank() {
        accounts = new ConcurrentHashMap<>();
    }

    @Override
    public Long openCommercialAccount(Company company, int pin, double startingDeposit) {
        long accountNumber = generateAccountNumber();
        CommercialAccount account =
                new CommercialAccount(company, accountNumber, pin, startingDeposit);
        accounts.put(accountNumber, account);
        return accountNumber;
    }

    @Override
    public Long openConsumerAccount(Person person, int pin, double startingDeposit) {
        long accountNumber = generateAccountNumber();
        ConsumerAccount account = new ConsumerAccount(person, accountNumber, pin, startingDeposit);
        accounts.put(accountNumber, account);
        return accountNumber;
    }

    @Override
    public boolean authenticateUser(Long accountNumber, int pin) {
        Account account = getAccount(accountNumber);
        return account.validatePin(pin);
    }

    @Override
    public double getBalance(Long accountNumber) {
        Account account = getAccount(accountNumber);
        return account.getBalance();
    }

    @Override
    public void credit(Long accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        account.creditAccount(amount);
    }

    @Override
    public boolean debit(Long accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        return account.debitAccount(amount);
    }

    private Account getAccount(Long accountNumber) {
        if (accountNumber == null) {
            throw new IllegalArgumentException("accountNumber cannot be null");
        }
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new NoSuchElementException(
                    String.format("Account with accountNumber=%d not found", accountNumber));
        }
        return account;
    }

    private long generateAccountNumber() {
        return idGenerator.getAndIncrement();
    }
}
