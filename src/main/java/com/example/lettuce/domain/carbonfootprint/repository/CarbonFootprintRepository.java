package com.example.lettuce.domain.carbonfootprint.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.lettuce.domain.carbonfootprint.aggregate.CarbonFootprint;
import com.example.lettuce.domain.user.aggregate.User;

/**
 * Repository for accessing CarbonFootprint entities.
 * Uses B+Tree indexing strategy for optimized queries.
 */
@Repository
public interface CarbonFootprintRepository extends JpaRepository<CarbonFootprint, Long> {

    /**
     * Finds a carbon footprint by its aggregate ID.
     * 
     * @param aggregateId The aggregate ID
     * @return The carbon footprint, if found
     */
    Optional<CarbonFootprint> findByAggregateId(String aggregateId);

    /**
     * Finds all carbon footprints for a specific user.
     * Uses EntityGraph to avoid N+1 query problems with lazy loading.
     * 
     * @param user     The user
     * @param pageable Pagination information
     * @return A page of carbon footprints
     */
    @EntityGraph(attributePaths = { "user" })
    Page<CarbonFootprint> findByUser(User user, Pageable pageable);

    /**
     * Counts the number of carbon footprints for a user within a time range.
     * 
     * @param user  The user
     * @param start The start time
     * @param end   The end time
     * @return The count of carbon footprints
     */
    long countByUserAndCalculatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

    /**
     * Finds all carbon footprints for a user within a time range.
     * 
     * @param user     The user
     * @param start    The start time
     * @param end      The end time
     * @param pageable Pagination information
     * @return A page of carbon footprints
     */
    @EntityGraph(attributePaths = { "user" })
    Page<CarbonFootprint> findByUserAndCalculatedAtBetween(
            User user, LocalDateTime start, LocalDateTime end, Pageable pageable);

    /**
     * Calculates the total carbon reduction for a user.
     * 
     * @param userId The user ID
     * @return The total carbon reduction
     */
    @Query("SELECT SUM(cf.carbonReduction) FROM CarbonFootprint cf WHERE cf.user.id = :userId")
    Optional<Double> calculateTotalCarbonReductionByUserId(@Param("userId") Long userId);

    /**
     * Finds all carbon footprints for a user within a time range.
     * 
     * @param user  The user
     * @param start The start time
     * @param end   The end time
     * @return A list of carbon footprints
     */
    @Query("SELECT cf FROM CarbonFootprint cf WHERE cf.calculatedAt BETWEEN :start AND :end")
    List<CarbonFootprint> findByCalculatedAtBetween(
            @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * Finds all carbon footprints for a user within a time range.
     * 
     * @param userId The user ID
     * @param start  The start time
     * @param end    The end time
     * @return A list of carbon footprints
     */
    @Query("SELECT cf FROM CarbonFootprint cf WHERE cf.user.id = :userId AND cf.calculatedAt BETWEEN :start AND :end")
    List<CarbonFootprint> findByUserIdAndCalculatedAtBetween(
            @Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}