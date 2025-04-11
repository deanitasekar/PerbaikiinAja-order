package id.ac.ui.cs.advprog.order.strategy;

import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

class RandomCouponStrategyTest {

    @RepeatedTest(3)
    void testRandomDiscountWithinExpectedRange() {
        RandomCouponStrategy strategy = new RandomCouponStrategy();
        double price = 100_000;
        double maxDiscount = 20_000;

        double result = strategy.apply(price, maxDiscount, 0);

        assertTrue(result >= 80_000 && result <= 100_000,
                "Result should be between 80k and 100k but was " + result);
    }
}
