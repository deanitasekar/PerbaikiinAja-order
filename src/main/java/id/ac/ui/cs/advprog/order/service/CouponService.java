package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.model.Coupon;

import java.util.List;
import java.util.UUID;

public interface CouponService {
    Coupon create(Coupon coupon);
    Coupon update(UUID id, Coupon coupon);
    void delete(UUID id);
    List<Coupon> findAll();
    List<Coupon> findAllValid();
    List<Coupon> findByType(CouponType type);
    Coupon findByCode(String code);
    double applyCoupon(Coupon coupon, double price);
}
