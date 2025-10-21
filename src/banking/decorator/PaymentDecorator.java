package banking.decorator;

import banking.service.PaymentService;

public abstract class PaymentDecorator implements PaymentService {
    protected final PaymentService wrappee;
    protected PaymentDecorator(PaymentService wrappee) { this.wrappee = wrappee; }
    @Override public boolean pay(double amount, String who) { return wrappee.pay(amount, who); }
    @Override public String name() { return wrappee.name(); }
}