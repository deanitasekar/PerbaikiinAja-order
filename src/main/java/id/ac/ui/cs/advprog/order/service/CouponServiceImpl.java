package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.factory.CouponStrategyFactory;
import id.ac.ui.cs.advprog.order.model.Coupon;
import id.ac.ui.cs.advprog.order.repository.CouponRepository;
import id.ac.ui.cs.advprog.order.strategy.CouponStrategy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class CouponServiceImpl implements CouponService {

    private final CouponRepository repo;
    private final CouponStrategyFactory strategyFactory;

    public CouponServiceImpl(CouponRepository repo, CouponStrategyFactory strategyFactory) {
        this.repo = repo;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public Coupon create(Coupon coupon) {
        if (coupon.getCode() == null || coupon.getCode().isBlank()) {
            throw new IllegalArgumentException("Coupon code must not be empty");
        }
        return repo.save(coupon);
    }

    @Override
    public Coupon update(UUID id, Coupon coupon) {
        if (repo.findById(id) == null) {
            throw new NoSuchElementException("Coupon not found for update");
        }
        return repo.save(coupon);
    }

    @Override
    public void delete(UUID id) {
        repo.deleteById(id);
    }

    @Override
    public List<Coupon> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Coupon> findAllValid() {
        return repo.findAllValid();
    }

    @Override
    public List<Coupon> findByType(CouponType type) {
        return repo.findByType(type);
    }

    @Override
    public Coupon findByCode(String code) {
        return repo.findByCode(code);
    }

    @Override
    public double applyCoupon(Coupon coupon, double price) {
        if (!coupon.isValid()) return price;

        CouponType type = coupon.getCoupon_type();
        if (type == null) throw new IllegalArgumentException("Coupon type is null");

        CouponStrategy strategy = strategyFactory.getStrategy(type);
        return strategy.apply(price, coupon.getDiscount_amount());
    }
}
