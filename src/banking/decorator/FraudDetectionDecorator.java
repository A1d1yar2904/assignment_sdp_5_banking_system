package banking.decorator;

import banking.service.PaymentService;

public class FraudDetectionDecorator extends PaymentDecorator {
    public FraudDetectionDecorator(PaymentService w) { super(w); }
    @Override public boolean pay(double amount, String who) {
        if (amount > 100000) {
            System.out.println("[FraudDetection] Suspicious transaction detected for " + who + " amount=" + amount);
        }
        return super.pay(amount, who);
    }
    @Override public String name() { return super.name() + "+Fraud"; }
}