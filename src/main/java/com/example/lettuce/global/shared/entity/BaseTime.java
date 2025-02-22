package com.example.lettuce.global.shared.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime extends BaseCreatedTime {
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "datetime comment '수정일'")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME comment '삭제 일시'")
    protected LocalDateTime deletedAt;
}
