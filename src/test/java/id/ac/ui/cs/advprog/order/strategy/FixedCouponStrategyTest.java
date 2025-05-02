package id.ac.ui.cs.advprog.order.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FixedCouponStrategyTest {

    @Test
    void testFixedDiscountAppliedCorrectly() {
        FixedCouponStrategy strategy = new FixedCouponStrategy();
        double price = 100.0;
        double discount = 20.0;

        double result = strategy.apply(price, discount);
        assertEquals(80.0, result);
    }

    @Test
    void testFixedDiscountNotNegative() {
        FixedCouponStrategy strategy = new FixedCouponStrategy();
        double result = strategy.apply(10.0, 20.0, 0);
        assertEquals(0.0, result);
    }
}
