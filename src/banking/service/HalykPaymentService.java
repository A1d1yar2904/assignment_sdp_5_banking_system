package banking.service;

public class HalykPaymentService implements PaymentService {
    @Override
    public boolean pay(double amount, String who) {
        System.out.println("Payment processed successfully: " + amount + " ₸");
        return true;
    }

    @Override
    public String name() {
        return "HalykPayment";
    }
}