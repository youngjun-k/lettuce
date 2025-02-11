package com.example.lettuce.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.lettuce.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAndDeletedAtIsNull(String email);

    User findByEmailAndDeletedAtIsNull(String email);

    // * When Spring Securiy is used, the user is not fetched with profiles.
    // * we need to fetch the user with profiles.
    // * The downfall of this is that every time the user is fetched with profiles, even if the profile is not needed, the query is executed.
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.clientProfile cp LEFT JOIN FETCH u.partnerProfile pp LEFT JOIN FETCH u.farmerProfile fp WHERE u.email = :email AND u.deletedAt IS NULL")
    User findByEmailAndDeletedAtIsNullWithProfiles(@Param("email") String email);

}
