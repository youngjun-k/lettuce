package com.example.lettuce.domain.reward.aggregate;

/**
 * Enum representing the types of reward transactions.
 */
public enum TransactionType {
    /**
     * Credit transaction - points added to the wallet
     */
    CREDIT,
    
    /**
     * Debit transaction - points deducted from the wallet
     */
    DEBIT
} 