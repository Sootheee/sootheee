package com.application.soothee.user.entity;

import com.application.soothee.common.entity.TimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user")
public class UserEntity extends TimeEntity {
    protected UserEntity() {}

    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "isDelete", nullable = false, length = 1)
    private String isDelete;

    @Column(name = "snsType", nullable = false)
    private String snsType;
}