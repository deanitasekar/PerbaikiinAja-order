package service;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.factory.CouponStrategyFactory;
import id.ac.ui.cs.advprog.order.model.Coupon;
import id.ac.ui.cs.advprog.order.repository.CouponRepository;
import id.ac.ui.cs.advprog.order.service.CouponServiceImpl;
import id.ac.ui.cs.advprog.order.strategy.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponServiceImplTest {

    private CouponRepository repo;
    private CouponStrategyFactory factory;
    private CouponServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(CouponRepository.class);
        factory = mock(CouponStrategyFactory.class);
        service = new CouponServiceImpl(repo, factory);
    }

    @Test
    void applyFixedCoupon() {
        Coupon c = new Coupon(CouponType.FIXED, 20000, 5);
        CouponStrategy strategy = new FixedCouponStrategy();
        when(factory.getStrategy(CouponType.FIXED)).thenReturn(strategy);
        assertEquals(80000, service.applyCoupon(c, 100000));
    }

    @Test
    void applyPercentageCoupon() {
        Coupon c = new Coupon(CouponType.PERCENTAGE, 0.25, 2);
        CouponStrategy strategy = new PercentageCouponStrategy();
        when(factory.getStrategy(CouponType.PERCENTAGE)).thenReturn(strategy);
        assertEquals(75000, service.applyCoupon(c, 100000));
    }

    @RepeatedTest(3)
    void applyRandomCouponWithinRange() {
        Coupon c = new Coupon(CouponType.RANDOM, 20000, 4);
        CouponStrategy strategy = new RandomCouponStrategy();
        when(factory.getStrategy(CouponType.RANDOM)).thenReturn(strategy);
        double result = service.applyCoupon(c, 100000);
        assertTrue(result >= 80000 && result <= 100000);
    }

    @Test
    void applyExpiredCouponReturnsOriginalPrice() {
        Coupon c = new Coupon(CouponType.FIXED, 10000, 1);
        c.setEnd_date(LocalDateTime.now().minusDays(1));
        assertEquals(50000, service.applyCoupon(c, 50000));
    }

    @Test
    void applyOverusedCouponReturnsOriginalPrice() {
        Coupon c = new Coupon(CouponType.FIXED, 10000, 0);
        assertEquals(60000, service.applyCoupon(c, 60000));
    }

    @Test
    void applyCouponWithUnknownTypeThrows() {
        Coupon c = mock(Coupon.class);
        when(c.isValid()).thenReturn(true);
        when(c.getCoupon_type()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> service.applyCoupon(c, 10000));
    }

    @Test
    void createCoupon() {
        Coupon c = new Coupon(CouponType.FIXED, 5000, 1);
        when(repo.save(c)).thenReturn(c);
        assertEquals(c, service.create(c));
        verify(repo).save(c);
    }

    @Test
    void createCouponWithEmptyCodeThrows() {
        Coupon c = new Coupon(CouponType.FIXED, 1000, 1);
        c.setCode("");
        assertThrows(IllegalArgumentException.class, () -> service.create(c));
    }

    @Test
    void updateCoupon() {
        Coupon c = new Coupon(CouponType.FIXED, 2000, 1);
        when(repo.findById(c.getId())).thenReturn(c);
        when(repo.save(c)).thenReturn(c);
        assertEquals(c, service.update(c.getId(), c));
    }

    @Test
    void updateNonExistingCouponThrows() {
        Coupon c = new Coupon(CouponType.FIXED, 2000, 1);
        UUID id = c.getId();
        when(repo.findById(id)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> service.update(id, c));
    }

    @Test
    void deleteCoupon() {
        UUID id = UUID.randomUUID();
        doNothing().when(repo).deleteById(id);
        service.delete(id);
        verify(repo).deleteById(id);
    }

    @Test
    void deleteNonExistingCouponNoThrow() {
        UUID id = UUID.randomUUID();
        doNothing().when(repo).deleteById(id);
        assertDoesNotThrow(() -> service.delete(id));
    }

    @Test
    void findByCodeReturnsCorrectCoupon() {
        Coupon c = new Coupon(CouponType.FIXED, 1000, 1);
        when(repo.findByCode("ABC")).thenReturn(c);
        assertEquals(c, service.findByCode("ABC"));
    }

    @Test
    void findAllReturnsListOfCoupons() {
        List<Coupon> list = List.of(new Coupon(CouponType.FIXED, 1, 1));
        when(repo.findAll()).thenReturn(list);
        assertEquals(list, service.findAll());
    }

    @Test
    void findByTypeReturnsCorrectList() {
        List<Coupon> list = List.of(new Coupon(CouponType.RANDOM, 1000, 1));
        when(repo.findByType(CouponType.RANDOM)).thenReturn(list);
        assertEquals(list, service.findByType(CouponType.RANDOM));
    }

    @Test
    void findAllValidReturnsOnlyValidCoupons() {
        Coupon valid = new Coupon(CouponType.PERCENTAGE, 10, 1);
        when(repo.findAllValid()).thenReturn(List.of(valid));
        List<Coupon> out = service.findAllValid();
        assertEquals(1, out.size());
        assertEquals(valid.getId(), out.get(0).getId());
    }
}
