package banking;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class Account implements AccountInterface {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final AccountHolder accountHolder;
    private final Long accountNumber;
    private final int pin;
    private double balance; // TODO: use BigDecimal

    protected Account(AccountHolder accountHolder, Long accountNumber, int pin,
            double startingDeposit) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = startingDeposit;
    }

    @Override
    public AccountHolder getAccountHolder() {
        return accountHolder;
    }

    @Override
    public Long getAccountNumber() {
        return accountNumber;
    }

    @Override
    public boolean validatePin(int attemptedPin) {
        return pin == attemptedPin;
    }

    @Override
    public double getBalance() {
        rwLock.readLock().lock();
        try {
            return balance;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void creditAccount(double amount) {
        rwLock.writeLock().lock();
        try {
            balance += amount;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public boolean debitAccount(double amount) {
        rwLock.writeLock().lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
