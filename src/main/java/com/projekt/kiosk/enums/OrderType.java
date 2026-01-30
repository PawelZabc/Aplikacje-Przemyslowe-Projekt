package com.projekt.kiosk.enums;

/**
 * Enum representing order types for the kiosk system.
 * DINE_IN: Customer eats at the restaurant (no packaging fee)
 * TAKEAWAY: Customer takes food to go (includes packaging fee)
 */
public enum OrderType {
    DINE_IN(0),
    TAKEAWAY(100); // 100 cents (1.00) packaging fee

    private final int packagingFeeCents;

    OrderType(int packagingFeeCents) {
        this.packagingFeeCents = packagingFeeCents;
    }

    public int getPackagingFeeCents() {
        return packagingFeeCents;
    }

    /**
     * Parse order type from string, defaulting to DINE_IN if invalid.
     */
    public static OrderType fromString(String type) {
        if (type == null) {
            return DINE_IN;
        }
        try {
            return OrderType.valueOf(type.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return DINE_IN;
        }
    }
}
