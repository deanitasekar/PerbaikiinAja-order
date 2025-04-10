package id.ac.ui.cs.advprog.order.repository;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.model.Coupon;

import java.util.*;
import java.util.stream.Collectors;

public class CouponRepository {

    private final List<Coupon> coupons = new ArrayList<>();

    public void save(Coupon coupon) {
        delete_by_id(coupon.getId());
        coupons.add(coupon);
    }

    public Coupon find_by_id(UUID id) {
        return coupons.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Coupon> find_all() {
        return new ArrayList<>(coupons);
    }

    public void delete_by_id(UUID id) {
        coupons.removeIf(c -> c.getId().equals(id));
    }

    public Coupon find_by_code(String code) {
        return coupons.stream()
                .filter(c -> c.getCode().equals(code))
                .reduce((first, second) -> second) // return latest if duplicate
                .orElse(null);
    }

    public List<Coupon> find_all_valid() {
        return coupons.stream()
                .filter(Coupon::isValid)
                .collect(Collectors.toList());
    }

    public List<Coupon> find_all_sorted_by_created_at() {
        return coupons.stream()
                .sorted(Comparator.comparing(Coupon::getCreated_at))
                .collect(Collectors.toList());
    }

    public List<Coupon> find_by_type(CouponType type) {
        return coupons.stream()
                .filter(c -> c.getCoupon_type() == type)
                .collect(Collectors.toList());
    }
}
