package banking.decorator;

import banking.service.PaymentService;

public class DiscountDecorator extends PaymentDecorator {
    private final double percent;
    public DiscountDecorator(PaymentService w, double percent) { super(w); this.percent = percent; }
    @Override public boolean pay(double amount, String who) {
        double discounted = amount * (1.0 - percent);
        System.out.println("[Discount] -" + (int)(percent * 100) + "% applied → new amount: " + discounted + " ₸");
        return super.pay(discounted, who);
    }
    @Override public String name() { return super.name() + "+Discount"; }
}