package id.ac.ui.cs.advprog.order.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PercentageCouponStrategyTest {

    @Test
    void testPercentageDiscountAppliedCorrectly() {
        PercentageCouponStrategy strategy = new PercentageCouponStrategy();
        double result = strategy.apply(200.0, 25);
        assertEquals(150.0, result);
    }

    @Test
    void testZeroPercentDiscount() {
        PercentageCouponStrategy strategy = new PercentageCouponStrategy();
        double result = strategy.apply(200.0, 0.0);
        assertEquals(200.0, result);
    }
}
