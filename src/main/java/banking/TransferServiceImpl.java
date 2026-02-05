package banking;

/**
 * Default implementation of {@link TransferService} backed by {@link Bank}.
 * <p>
 * This class delegates authentication and balance updates to {@code Bank},
 * which already provides the necessary concurrency guarantees on individual
 * accounts.
 */
public class TransferServiceImpl implements TransferService {

    private final Bank bank;

    /**
     * Creates a new transfer service using the given bank.
     *
     * @param bank the bank instance to use; must not be {@code null}
     */
    public TransferServiceImpl(Bank bank) {
        if (bank == null) {
            throw new IllegalArgumentException("bank must not be null");
        }
        this.bank = bank;
    }

    @Override
    public boolean transfer(long fromAccount, long toAccount, int pin, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        return bank.performTransfer(fromAccount, toAccount, pin, amount);
    }
}

