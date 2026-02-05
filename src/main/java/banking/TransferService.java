package banking;

/**
 * Service responsible for transferring funds between accounts.
 */
public interface TransferService {

    /**
     * Transfers the given {@code amount} from one account to another.
     *
     * @param fromAccount the source account number (debited)
     * @param toAccount   the destination account number (credited)
     * @param pin         the PIN for the {@code fromAccount}
     * @param amount      the amount to transfer; must be positive
     * @return {@code true} if the transfer was completed successfully,
     *         {@code false} if authentication failed or the source account had
     *         insufficient funds
     * @throws IllegalArgumentException if {@code amount} is not positive
     */
    boolean transfer(long fromAccount, long toAccount, int pin, double amount);
}

