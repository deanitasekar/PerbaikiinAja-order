package id.ac.ui.cs.advprog.order.repository;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.model.Coupon;

import java.util.*;

public class CouponRepository {

    private final List<Coupon> coupons = new ArrayList<>();

    public void save(Coupon coupon) {
        // TODO: implement save logic
    }

    public Coupon find_by_id(UUID id) {
        // TODO: implement find by id
        return null;
    }

    public List<Coupon> find_all() {
        // TODO: implement find all
        return null;
    }

    public void delete_by_id(UUID id) {
        // TODO: implement delete
    }

    public Coupon find_by_code(String code) {
        // TODO: implement find by code
        return null;
    }

    public List<Coupon> find_all_valid() {
        // TODO: implement filter valid coupons
        return null;
    }

    public List<Coupon> find_all_sorted_by_created_at() {
        // TODO: implement sort by created_at
        return null;
    }

    public List<Coupon> find_by_type(CouponType type) {
        // TODO: implement filter by type
        return null;
    }
}
