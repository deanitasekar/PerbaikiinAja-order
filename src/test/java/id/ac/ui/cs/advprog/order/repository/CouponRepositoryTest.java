package id.ac.ui.cs.advprog.order.repository;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.model.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CouponRepositoryTest {

    private CouponRepository repository;

    @BeforeEach
    void set_up() {
        repository = new CouponRepository();
    }

    @Test
    void test_save_coupon() {
        Coupon coupon = new Coupon(CouponType.FIXED, 10.0, 5);
        repository.save(coupon);

        Coupon result = repository.find_by_id(coupon.getId());
        assertNotNull(result);
        assertEquals(coupon.getCode(), result.getCode());
    }

    @Test
    void test_find_by_id_exists() {
        Coupon coupon = new Coupon(CouponType.FIXED, 10.0, 5);
        repository.save(coupon);

        Coupon found = repository.find_by_id(coupon.getId());
        assertEquals(coupon.getCode(), found.getCode());
    }

    @Test
    void test_find_by_id_not_exists() {
        UUID fake_id = UUID.randomUUID();
        assertNull(repository.find_by_id(fake_id));
    }

    @Test
    void test_find_all() {
        repository.save(new Coupon(CouponType.FIXED, 10.0, 5));
        repository.save(new Coupon(CouponType.PERCENTAGE, 15.0, 3));

        List<Coupon> coupons = repository.find_all();
        assertEquals(2, coupons.size());
    }

    @Test
    void test_delete_by_id() {
        Coupon coupon = new Coupon(CouponType.RANDOM, 20.0, 4);
        repository.save(coupon);

        repository.delete_by_id(coupon.getId());
        assertNull(repository.find_by_id(coupon.getId()));
    }

    @Test
    void test_delete_by_id_not_exists() {
        assertDoesNotThrow(() -> repository.delete_by_id(UUID.randomUUID()));
    }

    @Test
    void test_update_coupon() {
        Coupon coupon = new Coupon(CouponType.FIXED, 5.0, 2);
        repository.save(coupon);

        coupon.setDiscount_amount(7.0);
        repository.save(coupon);

        Coupon updated = repository.find_by_id(coupon.getId());
        assertEquals(7.0, updated.getDiscount_amount());
    }

    @Test
    void test_find_by_code() {
        Coupon coupon = new Coupon(CouponType.FIXED, 10.0, 5);
        repository.save(coupon);

        Coupon found = repository.find_by_code(coupon.getCode());
        assertNotNull(found);
        assertEquals(coupon.getId(), found.getId());
    }

    @Test
    void test_find_by_code_invalid() {
        assertNull(repository.find_by_code("INVALID-CODE"));
    }

    @Test
    void test_find_all_valid_coupons_only() {
        Coupon valid = new Coupon(CouponType.FIXED, 10.0, 5);
        Coupon expired = new Coupon(CouponType.FIXED, 10.0, 5);
        Coupon deleted = new Coupon(CouponType.FIXED, 10.0, 5);

        expired.setEnd_date(LocalDateTime.now().minusDays(1));
        deleted.setDeleted_at(LocalDateTime.now());

        repository.save(valid);
        repository.save(expired);
        repository.save(deleted);

        List<Coupon> valid_only = repository.find_all_valid();
        assertEquals(1, valid_only.size());
        assertEquals(valid.getId(), valid_only.get(0).getId());
    }

    @Test
    void test_find_all_sorted_by_created_at() {
        Coupon first = new Coupon(CouponType.FIXED, 5.0, 1);
        try { Thread.sleep(2); } catch (InterruptedException ignored) {}
        Coupon second = new Coupon(CouponType.PERCENTAGE, 15.0, 2);

        repository.save(second);
        repository.save(first);

        List<Coupon> sorted = repository.find_all_sorted_by_created_at();
        assertEquals(first.getId(), sorted.get(0).getId());
        assertEquals(second.getId(), sorted.get(1).getId());
    }

    @Test
    void test_soft_delete_hides_coupon() {
        Coupon coupon = new Coupon(CouponType.RANDOM, 10.0, 1);
        repository.save(coupon);

        coupon.setDeleted_at(LocalDateTime.now());
        repository.save(coupon);

        List<Coupon> all_valid = repository.find_all_valid();
        assertTrue(all_valid.stream().noneMatch(c -> c.getId().equals(coupon.getId())));
    }

    @Test
    void test_save_duplicate_code_allowed() {
        Coupon one = new Coupon(CouponType.FIXED, 5.0, 2);
        Coupon two = new Coupon(CouponType.FIXED, 5.0, 2);

        // override their code to be the same (simulate collision)
        two.setCode(one.getCode());

        repository.save(one);
        repository.save(two);

        assertEquals(two.getId(), repository.find_by_code(one.getCode()).getId());
    }

    @Test
    void test_find_by_type() {
        Coupon fixed = new Coupon(CouponType.FIXED, 10.0, 2);
        Coupon percent = new Coupon(CouponType.PERCENTAGE, 20.0, 2);

        repository.save(fixed);
        repository.save(percent);

        List<Coupon> result = repository.find_by_type(CouponType.FIXED);
        assertEquals(1, result.size());
        assertEquals(fixed.getId(), result.get(0).getId());
    }
}
