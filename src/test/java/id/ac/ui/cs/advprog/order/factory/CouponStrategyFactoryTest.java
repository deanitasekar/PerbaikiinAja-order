package id.ac.ui.cs.advprog.order.strategy;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponStrategyFactoryTest {

    private CouponStrategyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new CouponStrategyFactory();
    }

    @Test
    void testGetFixedStrategy() {
        CouponStrategy strategy = factory.getStrategy(CouponType.FIXED);
        assertTrue(strategy instanceof FixedCouponStrategy);
    }

    @Test
    void testGetPercentageStrategy() {
        CouponStrategy strategy = factory.getStrategy(CouponType.PERCENTAGE);
        assertTrue(strategy instanceof PercentageCouponStrategy);
    }

    @Test
    void testGetRandomStrategy() {
        CouponStrategy strategy = factory.getStrategy(CouponType.RANDOM);
        assertTrue(strategy instanceof RandomCouponStrategy);
    }

    @Test
    void testUnknownTypeThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.getStrategy(null);
        });
    }
}
