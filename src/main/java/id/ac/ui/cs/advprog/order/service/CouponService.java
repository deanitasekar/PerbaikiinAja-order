package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.*;
import id.ac.ui.cs.advprog.order.enums.CouponType;
import java.util.List;
import java.util.UUID;

public interface CouponService {
    CouponResponseDTO create(CreateCouponRequestDTO request);
    CouponResponseDTO update(UUID id, UpdateCouponRequestDTO request);
    void delete(UUID id);
    List<CouponResponseDTO> findAll();
    List<CouponResponseDTO> findAllValid();
    List<CouponResponseDTO> findByType(CouponType type);
    CouponResponseDTO findByCode(String code);
    ApplyCouponResponseDTO applyCoupon(UUID id, double price);
    CouponResponseDTO findById(UUID id);

}
