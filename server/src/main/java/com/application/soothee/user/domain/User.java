package com.application.soothee.user.domain;

import com.application.soothee.common.Entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "isDelete", nullable = false, length = 1)
    private String isDelete;

    @Column(name = "snsType", nullable = false)
    private String snsType;
}