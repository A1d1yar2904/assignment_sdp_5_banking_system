package banking.decorator;

import banking.service.PaymentService;

public class LoggingDecorator extends PaymentDecorator {
    public LoggingDecorator(PaymentService w) { super(w); }

    @Override
    public boolean pay(double amount, String who) {
        boolean ok = super.pay(amount, who);
        return ok;
    }

    @Override
    public String name() { return super.name(); }
}