package id.ac.ui.cs.advprog.order.factory;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.strategy.*;

public class CouponStrategyFactory {

    private final CouponStrategy fixedStrategy = new FixedCouponStrategy();
    private final CouponStrategy percentageStrategy = new PercentageCouponStrategy();
    private final CouponStrategy randomStrategy = new RandomCouponStrategy();

    public CouponStrategy getStrategy(CouponType type) {
        if (type == null) {
            throw new IllegalArgumentException("Coupon type must not be null");
        }

        return switch (type) {
            case FIXED -> fixedStrategy;
            case PERCENTAGE -> percentageStrategy;
            case RANDOM -> randomStrategy;
            default -> throw new IllegalArgumentException("Unsupported coupon type: " + type);
        };
    }
}
