package banking;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CommercialAccount extends Account {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Set<Person> authorizedUsers = new HashSet<>();

    public CommercialAccount(Company company, Long accountNumber, int pin, double startingDeposit) {
        super(company, accountNumber, pin, startingDeposit);
    }

    /**
     * @param person The person to add to the authorized users list.
     */
    protected void addAuthorizedUser(Person person) {
        rwLock.writeLock().lock();
        try {
            authorizedUsers.add(person);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * @param person The person to check authorization for.
     * @return true if person is authorized to access this account; otherwise, false.
     */
    public boolean isAuthorizedUser(Person person) {
        rwLock.readLock().lock();
        try {
            return authorizedUsers.contains(person);
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
