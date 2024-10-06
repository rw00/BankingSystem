package banking;

/**
 * Abstract bank account class.<br>
 * <br>
 * <p>
 * Private variables:<br>
 * {@link #accountHolder}: AccountHolder<br>
 * {@link #accountNumber}: Long<br>
 * {@link #pin}: int<br>
 * {@link #balance}: double
 */
public abstract class Account implements AccountInterface {
    private final AccountHolder accountHolder;
    private final Long accountNumber;
    private final int pin;
    private volatile double balance; // TODO : consider using ReadWriteLock

    protected Account(AccountHolder accountHolder, Long accountNumber, int pin, double startingDeposit) {
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
    public synchronized double getBalance() {
        return balance;
    }

    @Override
    public synchronized void creditAccount(double amount) {
        balance += amount;
    }

    @Override
    public synchronized boolean debitAccount(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
