package banking;

public class Transaction implements TransactionInterface {
    private final Long accountNumber;
    private final Bank bank;

    /**
     * @param bank The bank where the account is housed.
     * @param accountNumber The customer's account number.
     * @param attemptedPin The PIN entered by the customer.
     * @throws Exception if account authentication fails.
     */
    public Transaction(Bank bank, Long accountNumber, int attemptedPin) throws Exception {
        this.bank = bank;
        this.accountNumber = accountNumber;
        if (!bank.authenticateUser(accountNumber, attemptedPin)) {
            throw new IllegalArgumentException("Invalid account number or PIN");
        }
    }

    @Override
    public double getBalance() {
        return bank.getBalance(accountNumber);
    }

    @Override
    public void credit(double amount) {
        bank.credit(accountNumber, amount);
    }

    @Override
    public boolean debit(double amount) {
        return bank.debit(accountNumber, amount);
    }
}
