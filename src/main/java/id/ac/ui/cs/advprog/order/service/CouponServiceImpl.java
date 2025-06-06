package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.*;
import id.ac.ui.cs.advprog.order.enums.CouponType;
import id.ac.ui.cs.advprog.order.factory.CouponStrategyFactory;
import id.ac.ui.cs.advprog.order.model.Coupon;
import id.ac.ui.cs.advprog.order.repository.CouponRepository;
import id.ac.ui.cs.advprog.order.strategy.CouponStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
            type = CouponType.valueOf(request.getCouponType());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid coupon type: " + request.getCouponType());
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
        Coupon coupon = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Coupon not found"));


        coupon.setDiscount_amount(request.getDiscount_amount());
        coupon.setMax_usage(request.getMax_usage());
        coupon.setStart_date(request.getStart_date());
        coupon.setEnd_date(request.getEnd_date());

        return toResponse(repo.save(coupon));
    }

    @Override
    public void delete(UUID id) {
        Coupon coupon = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
        coupon.setDeleted_at(LocalDateTime.now());
        repo.save(coupon);
    }

    @Override
    public CouponListResponseDTO findAll() {
        List<CouponResponseDTO> list = repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return CouponListResponseDTO.builder()
                .coupons(list)
                .total(list.size())
                .build();
    }


    @Override
    public CouponListResponseDTO findAllValid() {
        List<CouponResponseDTO> validList = repo.findAll().stream()
                .filter(Coupon::isValid)
                .map(this::toResponse)
                .collect(Collectors.toList());

        return CouponListResponseDTO.builder()
                .coupons(validList)
                .total(validList.size())
                .build();
    }



    @Override
    public CouponListResponseDTO findByCouponType(CouponType type) {
        List<CouponResponseDTO> filtered = repo.findByCouponType(type).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return CouponListResponseDTO.builder()
                .coupons(filtered)
                .total(filtered.size())
                .build();
    }



    @Override
    public CouponResponseDTO findByCode(String code) {
        Coupon coupon = repo.findByCode(code);
        return toResponse(coupon);
    }


    @Override
    public CouponResponseDTO findById(UUID id) {
        Coupon coupon = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
        return toResponse(coupon);
    }


    private CouponResponseDTO toResponse(Coupon coupon) {
        return CouponResponseDTO.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .couponType(coupon.getCouponType().name())
                .discount_amount(coupon.getDiscount_amount())
                .max_usage(coupon.getMax_usage())
                .start_date(coupon.getStart_date())
                .end_date(coupon.getEnd_date())
                .build();
    }

    @Async("asyncExecutor")
    @Override
    public CompletableFuture<ApplyCouponResponseDTO> applyCoupon(UUID id, double price){
        return CompletableFuture.completedFuture(calc(id,price,true));
    }

    @Async("asyncExecutor")
    @Override
    public CompletableFuture<ApplyCouponResponseDTO> previewCoupon(UUID id,double price){
        return CompletableFuture.completedFuture(calc(id,price,false));
    }

    private ApplyCouponResponseDTO calc(UUID id, double price, boolean increment) {
        Coupon coupon = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));

        if (!coupon.isValid()) {
            return ApplyCouponResponseDTO.builder()
                    .id(coupon.getId())
                    .original_price(price)
                    .discounted_price(price)
                    .coupon_code(coupon.getCode())
                    .valid(false)
                    .build();
        }

        CouponStrategy strategy = strategyFactory.getStrategy(coupon.getCouponType());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown coupon type: " + coupon.getCouponType());
        }

        double discounted = strategy.apply(price, coupon.getDiscount_amount());

        if (increment) {
            coupon.incrementUsage();
            repo.save(coupon);
        }

        return ApplyCouponResponseDTO.builder()
                .id(coupon.getId())
                .original_price(price)
                .discounted_price(discounted)
                .coupon_code(coupon.getCode())
                .valid(true)
                .build();
    }



}
