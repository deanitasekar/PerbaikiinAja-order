package id.ac.ui.cs.advprog.order.strategy;

public class FixedCouponStrategy implements CouponStrategy {

    @Override
    public double apply(double price, double discount) {
        return Math.max(0, price - discount);
    }
}
