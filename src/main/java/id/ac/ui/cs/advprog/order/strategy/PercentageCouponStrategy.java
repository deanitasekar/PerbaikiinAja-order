package id.ac.ui.cs.advprog.order.strategy;

public class PercentageCouponStrategy implements CouponStrategy {

    @Override
    public double apply(double price, double discount) {
        return Math.max(0, price*(1-discount));
    }
}
