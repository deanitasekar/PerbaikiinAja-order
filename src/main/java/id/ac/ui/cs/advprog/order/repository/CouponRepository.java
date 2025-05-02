package id.ac.ui.cs.advprog.order.repository;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.model.Coupon;

import java.util.*;
import java.util.stream.Collectors;

public class CouponRepository {

    private final List<Coupon> coupons = new ArrayList<>();

    public Coupon save(Coupon coupon) {
        deleteById(coupon.getId());
        coupons.add(coupon);
        return coupon;
    }


    public Coupon findById(UUID id) {
        return coupons.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Coupon> findAll() {
        return new ArrayList<>(coupons);
    }

    public void deleteById(UUID id) {
        coupons.removeIf(c -> c.getId().equals(id));
    }

    public Coupon findByCode(String code) {
        return coupons.stream()
                .filter(c -> c.getCode().equals(code))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    public List<Coupon> findAllValid() {
        return coupons.stream()
                .filter(Coupon::isValid)
                .collect(Collectors.toList());
    }

    public List<Coupon> findAllSortedByCreatedAt() {
        return coupons.stream()
                .sorted(Comparator.comparing(Coupon::getCreated_at))
                .collect(Collectors.toList());
    }

    public List<Coupon> findByType(CouponType type) {
        return coupons.stream()
                .filter(c -> c.getCoupon_type() == type)
                .collect(Collectors.toList());
    }
}
