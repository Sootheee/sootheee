package com.application.soothee.common.Entity;

import com.application.soothee.member.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {
    @CreatedBy
    @Column(updatable = false)
    private User user;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime reg_date;
    @LastModifiedDate
    private LocalDateTime mod_date;
}
