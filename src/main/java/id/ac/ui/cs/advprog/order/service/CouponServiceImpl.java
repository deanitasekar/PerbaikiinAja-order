package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.factory.CouponStrategyFactory;
import id.ac.ui.cs.advprog.order.model.Coupon;
import id.ac.ui.cs.advprog.order.repository.CouponRepository;
import id.ac.ui.cs.advprog.order.strategy.*;

import java.util.List;
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
        return null;
    }

    @Override
    public Coupon update(UUID id, Coupon coupon) {
        return null;
    }

    @Override
    public void delete(UUID id) {
    }

    @Override
    public List<Coupon> findAll() {
        return null;
    }

    @Override
    public List<Coupon> findAllValid() {
        return null;
    }

    @Override
    public List<Coupon> findByType(CouponType type) {
        return null;
    }

    @Override
    public Coupon findByCode(String code) {
        return null;
    }

    @Override
    public double applyCoupon(Coupon coupon, double price) {
        return 0;
    }
}
