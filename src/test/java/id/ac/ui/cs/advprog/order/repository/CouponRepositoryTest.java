package id.ac.ui.cs.advprog.order.repository;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.model.Coupon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CouponRepositoryTest {

    @Autowired
    private CouponRepository repository;

    @Test
    void testSaveCoupon() {
        Coupon coupon = new Coupon(CouponType.FIXED, 10.0, 5);
        repository.save(coupon);

        Coupon result = repository.findById(coupon.getId()).orElse(null);
        assertNotNull(result);
        assertEquals(coupon.getCode(), result.getCode());
    }

    @Test
    void testFindByIdExists() {
        Coupon coupon = new Coupon(CouponType.FIXED, 10.0, 5);
        repository.save(coupon);

        Coupon found = repository.findById(coupon.getId()).orElse(null);
        assertEquals(coupon.getCode(), found.getCode());
    }

    @Test
    void testFindByIdNotExists() {
        UUID fakeId = UUID.randomUUID();
        assertTrue(repository.findById(fakeId).isEmpty());
    }

    @Test
    void testFindAll() {
        repository.save(new Coupon(CouponType.FIXED, 10.0, 5));
        repository.save(new Coupon(CouponType.PERCENTAGE, 15.0, 3));

        List<Coupon> coupons = repository.findAll();
        assertEquals(2, coupons.size());
    }

    @Test
    void testDeleteById() {
        Coupon coupon = new Coupon(CouponType.RANDOM, 20.0, 4);
        repository.save(coupon);

        repository.deleteById(coupon.getId());
        assertTrue(repository.findById(coupon.getId()).isEmpty());
    }

    @Test
    void testDeleteByIdNotExists() {
        assertDoesNotThrow(() -> repository.deleteById(UUID.randomUUID()));
    }

    @Test
    void testUpdateCoupon() {
        Coupon coupon = new Coupon(CouponType.FIXED, 5.0, 2);
        repository.save(coupon);

        coupon.setDiscount_amount(7.0);
        repository.save(coupon);

        Coupon updated = repository.findById(coupon.getId()).orElse(null);
        assertEquals(7.0, updated.getDiscount_amount());
    }

    @Test
    void testFindByCode() {
        Coupon coupon = new Coupon(CouponType.FIXED, 10.0, 5);
        repository.save(coupon);

        Coupon found = repository.findByCode(coupon.getCode());
        assertNotNull(found);
        assertEquals(coupon.getId(), found.getId());
    }

    @Test
    void testFindByCodeInvalid() {
        assertNull(repository.findByCode("INVALID-CODE"));
    }

    @Test
    void testFindAllValidCouponsOnly() {
        Coupon valid = new Coupon(CouponType.FIXED, 10.0, 5);
        Coupon expired = new Coupon(CouponType.FIXED, 10.0, 5);
        Coupon deleted = new Coupon(CouponType.FIXED, 10.0, 5);

        expired.setEnd_date(LocalDateTime.now().minusDays(1));
        deleted.setDeleted_at(LocalDateTime.now());

        repository.save(valid);
        repository.save(expired);
        repository.save(deleted);

        List<Coupon> validOnly = repository.findAll().stream()
                .filter(Coupon::isValid)
                .toList();

        assertEquals(1, validOnly.size());
        assertEquals(valid.getId(), validOnly.get(0).getId());
    }

    @Test
    void testFindAllSortedByCreatedAt() {
        Coupon first = new Coupon(CouponType.FIXED, 5.0, 1);
        try { Thread.sleep(2); } catch (InterruptedException ignored) {}
        Coupon second = new Coupon(CouponType.PERCENTAGE, 15.0, 2);

        repository.save(second);
        repository.save(first);

        List<Coupon> sorted = repository.findAll().stream()
                .sorted((a, b) -> a.getCreated_at().compareTo(b.getCreated_at()))
                .toList();

        assertEquals(first.getId(), sorted.get(0).getId());
        assertEquals(second.getId(), sorted.get(1).getId());
    }

    @Test
    void testSoftDeleteHidesCoupon() {
        Coupon coupon = new Coupon(CouponType.RANDOM, 10.0, 1);
        repository.save(coupon);

        coupon.setDeleted_at(LocalDateTime.now());
        repository.save(coupon);

        List<Coupon> allValid = repository.findAll().stream()
                .filter(Coupon::isValid)
                .toList();

        assertTrue(allValid.stream().noneMatch(c -> c.getId().equals(coupon.getId())));
    }

    @Test
    void testSaveDuplicateCodeShouldFail() {
        Coupon one = new Coupon(CouponType.FIXED, 5.0, 2);
        repository.save(one);

        Coupon two = new Coupon(CouponType.FIXED, 5.0, 2);
        two.setCode(one.getCode());

        assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(two),
                "Duplicate code should not be allowed");
    }

    @Test
    void testFindByType() {
        Coupon fixed = new Coupon(CouponType.FIXED, 10.0, 2);
        Coupon percent = new Coupon(CouponType.PERCENTAGE, 20.0, 2);

        repository.save(fixed);
        repository.save(percent);

        List<Coupon> result = repository.findByCouponType(CouponType.FIXED);
        assertEquals(1, result.size());
        assertEquals(fixed.getId(), result.get(0).getId());
    }
}
