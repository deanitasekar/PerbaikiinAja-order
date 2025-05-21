package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.*;
import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.factory.CouponStrategyFactory;
import id.ac.ui.cs.advprog.order.model.Coupon;
import id.ac.ui.cs.advprog.order.repository.CouponRepository;
import id.ac.ui.cs.advprog.order.strategy.CouponStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository repo;
    private final CouponStrategyFactory strategyFactory;

    public CouponServiceImpl(CouponRepository repo, CouponStrategyFactory strategyFactory) {
        this.repo = repo;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public CouponResponseDTO create(CreateCouponRequestDTO request) {
        CouponType type;
        try {
            type = CouponType.valueOf(request.getCoupon_type());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid coupon type: " + request.getCoupon_type());
        }

        Coupon coupon = new Coupon(type, request.getDiscount_amount(), request.getMax_usage());

        if (request.getStart_date() != null) {
            coupon.setStart_date(request.getStart_date());
        } else {
            coupon.setStart_date(coupon.getCreated_at());
        }

        coupon.setEnd_date(request.getEnd_date());

        return toResponse(repo.save(coupon));
    }

    @Override
    public CouponResponseDTO update(UUID id, UpdateCouponRequestDTO request) {
        Coupon coupon = repo.findById(id);

        if (coupon == null) throw new EntityNotFoundException("Coupon not found");


        coupon.setDiscount_amount(request.getDiscount_amount());
        coupon.setMax_usage(request.getMax_usage());
        coupon.setStart_date(request.getStart_date());
        coupon.setEnd_date(request.getEnd_date());

        return toResponse(repo.save(coupon));
    }

    @Override
    public void delete(UUID id) {
        Coupon coupon = repo.findById(id);

        if (coupon == null) throw new EntityNotFoundException("Coupon not found");

        repo.deleteById(coupon.getId());
    }

    @Override
    public List<CouponResponseDTO> findAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponResponseDTO> findAllValid() {
        return repo.findAllValid().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponResponseDTO> findByType(CouponType type) {
        return repo.findByType(type).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CouponResponseDTO findByCode(String code) {
        Coupon coupon = repo.findByCode(code);
        return toResponse(coupon);
    }

    @Override
    public double applyCoupon(UUID id, double price) {
        Coupon coupon = repo.findById(id);

        if (coupon == null) throw new EntityNotFoundException("Coupon not found");

        if (!coupon.isValid()) return price;

        CouponType type = coupon.getCoupon_type();
        if (type == null) throw new IllegalArgumentException("Coupon type is null");

        CouponStrategy strategy = strategyFactory.getStrategy(type);
        double discountedPrice = strategy.apply(price, coupon.getDiscount_amount());

        coupon.incrementUsage();
        repo.save(coupon);

        return discountedPrice;
    }


    @Override
    public CouponResponseDTO findById(UUID id) {
        Coupon coupon = repo.findById(id);
        if (coupon == null) throw new EntityNotFoundException("Coupon not found");
        return toResponse(coupon);
    }


    private CouponResponseDTO toResponse(Coupon coupon) {
        return CouponResponseDTO.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .coupon_type(coupon.getCoupon_type().name())
                .discount_amount(coupon.getDiscount_amount())
                .max_usage(coupon.getMax_usage())
                .start_date(coupon.getStart_date())
                .end_date(coupon.getEnd_date())
                .build();
    }
}
