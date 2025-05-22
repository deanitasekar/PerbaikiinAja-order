package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.*;
import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.factory.CouponStrategyFactory;
import id.ac.ui.cs.advprog.order.model.Coupon;
import id.ac.ui.cs.advprog.order.repository.CouponRepository;
import id.ac.ui.cs.advprog.order.strategy.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock private CouponRepository repo;
    @Mock private CouponStrategyFactory factory;
    @InjectMocks private CouponServiceImpl service;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new Coupon(CouponType.FIXED, 20000, 5);
        coupon.setId(UUID.randomUUID());
        coupon.setStart_date(LocalDateTime.now().minusDays(1));
        coupon.setEnd_date(LocalDateTime.now().plusDays(5));
        String suffix = coupon.getId().toString().replace("-", "").substring(0, 3).toUpperCase();
        coupon.setCode(coupon.getCouponType().name() + "-" + suffix);    }

    @Test
    void createCoupon() {
        CreateCouponRequestDTO dto = CreateCouponRequestDTO.builder()
                .couponType("FIXED")
                .discount_amount(20000)
                .max_usage(5)
                .start_date(coupon.getStart_date())
                .end_date(coupon.getEnd_date())
                .build();

        when(repo.save(any(Coupon.class))).thenReturn(coupon);

        CouponResponseDTO res = service.create(dto);

        assertNotNull(res);
        assertEquals(20000, res.getDiscount_amount());
    }

    @Test
    void createCouponInvalidTypeThrows() {
        CreateCouponRequestDTO dto = CreateCouponRequestDTO.builder()
                .couponType("INVALID")
                .discount_amount(1000)
                .max_usage(1)
                .start_date(LocalDateTime.now())
                .end_date(LocalDateTime.now().plusDays(1))
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.create(dto));
    }

    @Test
    void updateCoupon() {
        UpdateCouponRequestDTO dto = UpdateCouponRequestDTO.builder()
                .discount_amount(7500)
                .max_usage(3)
                .start_date(LocalDateTime.now())
                .end_date(LocalDateTime.now().plusDays(4))
                .build();

        when(repo.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(repo.save(any(Coupon.class))).thenReturn(coupon);

        CouponResponseDTO res = service.update(coupon.getId(), dto);
        assertEquals(7500, res.getDiscount_amount());
    }

    @Test
    void updateNonExistingThrows() {
        UpdateCouponRequestDTO dto = UpdateCouponRequestDTO.builder()
                .discount_amount(7500)
                .max_usage(3)
                .start_date(LocalDateTime.now())
                .end_date(LocalDateTime.now().plusDays(4))
                .build();

        when(repo.findById(coupon.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(coupon.getId(), dto));
    }

    @Test
    void deleteCoupon() {
        when(repo.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        doNothing().when(repo).deleteById(coupon.getId());

        assertDoesNotThrow(() -> service.delete(coupon.getId()));
        verify(repo).deleteById(coupon.getId());
    }

    @Test
    void deleteNonExistingThrows() {
        when(repo.findById(coupon.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.delete(coupon.getId()));
    }

    @Test
    void applyFixedCoupon() {
        when(repo.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(factory.getStrategy(CouponType.FIXED)).thenReturn(new FixedCouponStrategy());

        ApplyCouponResponseDTO res = service.applyCoupon(coupon.getId(), 100000);

        assertEquals(80000, res.getDiscounted_price());
        assertEquals(coupon.getCode(), res.getCoupon_code());
        assertTrue(res.isValid());
    }


    @Test
    void applyPercentageCoupon() {
        coupon.setCouponType(CouponType.PERCENTAGE);
        coupon.setDiscount_amount(25);
        when(repo.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(factory.getStrategy(CouponType.PERCENTAGE)).thenReturn(new PercentageCouponStrategy());

        ApplyCouponResponseDTO res = service.applyCoupon(coupon.getId(), 100000);

        assertEquals(75000, res.getDiscounted_price());
        assertEquals(coupon.getCode(), res.getCoupon_code());
        assertTrue(res.isValid());
    }


    @Test
    void applyRandomCouponWithinRange() {
        coupon.setCouponType(CouponType.RANDOM);
        coupon.setDiscount_amount(20000);
        when(repo.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(factory.getStrategy(CouponType.RANDOM)).thenReturn(new RandomCouponStrategy());

        ApplyCouponResponseDTO res = service.applyCoupon(coupon.getId(), 100000);

        assertTrue(res.getDiscounted_price() >= 80000 && res.getDiscounted_price() <= 100000);
        assertTrue(res.isValid());
    }


    @Test
    void applyExpiredCouponReturnsOriginal() {
        coupon.setEnd_date(LocalDateTime.now().minusDays(1));
        when(repo.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        ApplyCouponResponseDTO res = service.applyCoupon(coupon.getId(), 50000);

        assertEquals(50000, res.getDiscounted_price());
        assertFalse(res.isValid());
    }


    @Test
    void applyOverusedCouponReturnsOriginal() {
        coupon.setMax_usage(0);
        when(repo.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        ApplyCouponResponseDTO res = service.applyCoupon(coupon.getId(), 60000);

        assertEquals(60000, res.getDiscounted_price());
        assertFalse(res.isValid());
    }


    @Test
    void applyUnknownTypeThrows() {
        Coupon mockCoupon = mock(Coupon.class);
        when(repo.findById(any())).thenReturn(Optional.of(mockCoupon));
        when(mockCoupon.isValid()).thenReturn(true);
        when(mockCoupon.getCouponType()).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.applyCoupon(UUID.randomUUID(), 10000));
    }


    @Test
    void applyCouponNotFoundThrows() {
        when(repo.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.applyCoupon(UUID.randomUUID(), 10000));
    }

    @Test
    void findByCode() {
        when(repo.findByCode("CODE")).thenReturn(coupon);

        CouponResponseDTO res = service.findByCode("CODE");
        assertEquals(coupon.getId(), res.getId());
    }

    @Test
    void findAll() {
        when(repo.findAll()).thenReturn(List.of(coupon));

        CouponListResponseDTO res = service.findAll();
        assertEquals(1, res.getTotal());
        assertEquals(coupon.getId(), res.getCoupons().get(0).getId());
    }

    @Test
    void findAllValid() {
        when(repo.findAll()).thenReturn(List.of(coupon));

        CouponListResponseDTO res = service.findAllValid();

        assertEquals(1, res.getTotal());
        assertEquals(coupon.getId(), res.getCoupons().get(0).getId());
    }

    @Test
    void findByCouponType() {
        when(repo.findByCouponType(CouponType.FIXED)).thenReturn(List.of(coupon));

        CouponListResponseDTO res = service.findByCouponType(CouponType.FIXED);
        assertEquals(1, res.getTotal());
        assertEquals("FIXED", res.getCoupons().get(0).getCouponType());
    }


    @Test
    void getById_success() {
        when(repo.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        CouponResponseDTO result = service.findById(coupon.getId());
        assertEquals(coupon.getId(), result.getId());
    }

    @Test
    void findById_notFoundThrows() {
        when(repo.findById(coupon.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(coupon.getId()));
    }

    @Test
    void previewCoupon_shouldReturnDiscountedPriceWithoutIncrementingUsage() {
        when(repo.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(factory.getStrategy(CouponType.FIXED)).thenReturn(new FixedCouponStrategy());

        int usageBefore = coupon.getCurrent_usage();

        ApplyCouponResponseDTO result = service.previewCoupon(coupon.getId(), 100000);

        assertEquals(80000, result.getDiscounted_price());
        assertEquals(100000, result.getOriginal_price());
        assertEquals(coupon.getCode(), result.getCoupon_code());
        assertTrue(result.isValid());
        assertEquals(usageBefore, coupon.getCurrent_usage());
    }

}
