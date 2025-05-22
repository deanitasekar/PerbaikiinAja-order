package id.ac.ui.cs.advprog.order.controller;

import id.ac.ui.cs.advprog.order.dto.*;
import id.ac.ui.cs.advprog.order.service.CouponService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<CouponResponseDTO> createCoupon(@Valid @RequestBody CreateCouponRequestDTO request) {
        CouponResponseDTO response = couponService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CouponResponseDTO>> getAllCoupons() {
        List<CouponResponseDTO> response = couponService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponseDTO> getCouponById(@PathVariable UUID id) {
        CouponResponseDTO response = couponService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponseDTO> updateCoupon(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCouponRequestDTO request) {
        CouponResponseDTO response = couponService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteCoupon(@PathVariable UUID id) {
        couponService.delete(id);
        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .success(true)
                        .message("Coupon deleted successfully")
                        .build()
        );
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<ApplyCouponResponseDTO> applyCoupon(
            @PathVariable UUID id,
            @Valid @RequestBody ApplyCouponRequestDTO request) {

        ApplyCouponResponseDTO response = couponService.applyCoupon(id, request.getOriginal_price());
        return ResponseEntity.ok(response);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseDTO> handleIllegalState(Exception ex) {
        return ResponseEntity.badRequest().body(
                ResponseDTO.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseDTO.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .build()
        );
    }
}
