package banking.decorator;

import banking.service.AccountService;
import banking.service.PaymentService;

public class CashbackDecorator extends PaymentDecorator {
    private final AccountService account;
    private final double percent;

    public CashbackDecorator(PaymentService wrappee, AccountService account, double percent) {
        super(wrappee);
        this.account = account;
        this.percent = percent;
    }

    @Override
    public boolean pay(double amount, String who) {
        boolean ok = super.pay(amount, who);

        if (ok) {
            double cashback = amount * percent;
            account.deposit(cashback);
            System.out.println("Cashback received: " + (int) cashback + " ₸");
        }
        return ok;
    }

    @Override
    public String name() {
        return super.name() + "+Cashback";
    }
}