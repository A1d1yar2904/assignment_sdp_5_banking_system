package banking.service;

public class KaspiAccountService implements AccountService {
    private double balance;
    public KaspiAccountService(double start) { this.balance = start; }
    @Override public double getBalance() { return balance; }
    @Override public boolean withdraw(double amount) {
        if (amount <= balance) { balance -= amount; return true; }
        return false;
    }
    @Override public void deposit(double amount) { balance += amount; }
}