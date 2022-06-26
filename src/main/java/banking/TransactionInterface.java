package banking;

public interface TransactionInterface {

    /**
     * @return The balance for the account {@link Transaction#accountNumber}
     */
    double getBalance();

    /**
     * @param amount The amount to credit/deposit into account {@link Transaction#accountNumber}
     */
    void credit(double amount);

    /**
     * @param amount The amount to debit/withdraw from account {@link Transaction#accountNumber}
     * @return true if amount could be withdrawn; otherwise, return false
     */
    boolean debit(double amount);
}
