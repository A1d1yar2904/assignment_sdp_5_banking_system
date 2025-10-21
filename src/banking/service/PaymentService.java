package banking.service;

public interface PaymentService {
    boolean pay(double amount, String who);
    String name();
}