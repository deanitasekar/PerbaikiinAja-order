package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.*;
import id.ac.ui.cs.advprog.order.enums.CouponType;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface CouponService {
    CouponResponseDTO create(CreateCouponRequestDTO request);
    CouponResponseDTO update(UUID id, UpdateCouponRequestDTO request);
    void delete(UUID id);
    CouponListResponseDTO findAll();
    CouponListResponseDTO findAllValid();
    CouponListResponseDTO findByCouponType(CouponType type);
    CouponResponseDTO findByCode(String code);
    CompletableFuture<ApplyCouponResponseDTO> applyCoupon(UUID id, double price);
    CouponResponseDTO findById(UUID id);
    CompletableFuture<ApplyCouponResponseDTO> previewCoupon(UUID id,double price);

}
