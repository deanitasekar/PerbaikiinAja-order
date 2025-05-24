package id.ac.ui.cs.advprog.order.strategy;

import java.util.Random;

public class RandomCouponStrategy implements CouponStrategy {

    private final Random random = new Random();

    @Override
    public double apply(double price, double maxDiscount) {
        double discount = random.nextDouble() * maxDiscount;
        return Math.max(0, price - discount);
    }
}
