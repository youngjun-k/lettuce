package com.example.lettuce.domain.reward.specification;

/**
 * Generic Specification interface for implementing the Specification pattern.
 * 
 * @param <T> The type of object that the specification is checking
 */
public interface Specification<T> {
    
    /**
     * Checks if the given candidate satisfies the specification.
     * 
     * @param candidate The object to check
     * @return true if the candidate satisfies the specification, false otherwise
     */
    boolean isSatisfiedBy(T candidate);
    
    /**
     * Combines this specification with another using logical AND.
     * 
     * @param other The other specification to combine with
     * @return A new specification that is the logical AND of this and the other specification
     */
    default Specification<T> and(Specification<T> other) {
        return candidate -> isSatisfiedBy(candidate) && other.isSatisfiedBy(candidate);
    }
    
    /**
     * Combines this specification with another using logical OR.
     * 
     * @param other The other specification to combine with
     * @return A new specification that is the logical OR of this and the other specification
     */
    default Specification<T> or(Specification<T> other) {
        return candidate -> isSatisfiedBy(candidate) || other.isSatisfiedBy(candidate);
    }
    
    /**
     * Creates a new specification that is the logical NOT of this specification.
     * 
     * @return A new specification that is the logical NOT of this specification
     */
    default Specification<T> not() {
        return candidate -> !isSatisfiedBy(candidate);
    }
} 