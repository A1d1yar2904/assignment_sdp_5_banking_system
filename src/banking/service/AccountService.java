package banking.service;

public interface AccountService {
    double getBalance();
    boolean withdraw(double amount);
    void deposit(double amount);
}