package com.ecommerce.backend.dto;

public record UserResponseDto(Long userId, String username, String email, String createdAt) {}
