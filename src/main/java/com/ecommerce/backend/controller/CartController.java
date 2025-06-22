package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    // ✅ Get all cart items for a user
    @GetMapping("/{email}")
    public List<CartItem> getCartItems(@PathVariable String email) {
        return cartItemRepository.findByUserEmail(email);
    }

    // ✅ Add item to cart (avoid duplicates, increase quantity)
    @PostMapping
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem item) {
        List<CartItem> existingItems = cartItemRepository.findByUserEmail(item.getUserEmail());

        for (CartItem existing : existingItems) {
            if (existing.getProductId().equals(item.getProductId())) {
                existing.setQuantity(existing.getQuantity() + 1);
                CartItem updated = cartItemRepository.save(existing);
                return ResponseEntity.ok(updated);
            }
        }

        CartItem saved = cartItemRepository.save(item);
        return ResponseEntity.ok(saved);
    }

    // ✅ Clear cart for a user with debug and error logging
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> clearCart(@PathVariable String email) {
        System.out.println("🧹 Request received to clear cart for: " + email);
        try {
            cartItemRepository.deleteByUserEmail(email);
            System.out.println("✅ Cart cleared for: " + email);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("❌ Error clearing cart for: " + email);
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ Update quantity for a specific cart item
    @PatchMapping("/update/{id}")
    public ResponseEntity<CartItem> updateQuantity(@PathVariable Long id, @RequestParam int quantity) {
        System.out.println("🔄 Request to update quantity for cart item ID " + id + " to " + quantity);
        return cartItemRepository.findById(id)
                .map(item -> {
                    item.setQuantity(quantity);
                    CartItem updated = cartItemRepository.save(item);
                    System.out.println("✅ Quantity updated: " + updated.getProductName() + " → " + updated.getQuantity());
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    System.err.println("❌ Cart item not found for ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }
}
