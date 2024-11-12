package com.application.soothee.dairy.entity;

import com.application.soothee.common.entity.TimeEntity;
import com.application.soothee.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "dairy")
public class DairyEntity extends TimeEntity {
    protected DairyEntity() {}

    @Id
    @Column(name = "dairy_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "score", nullable = false)
    private Float score;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cond_id", nullable = false)
    private ConditionEntity cond;

    @Column(name = "content", length = 600)
    private String content;

    @Column(name = "hope")
    private String hope;

    @Column(name = "thank")
    private String thank;

    @Column(name = "learn")
    private String learn;
}