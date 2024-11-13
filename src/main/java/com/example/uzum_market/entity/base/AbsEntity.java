package com.example.uzum_market.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

public class AbsEntity {
    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @LastModifiedBy
    private Long updatedBy;

    @UpdateTimestamp
    private Timestamp updatedAt;

    private boolean deleted = false;
}
