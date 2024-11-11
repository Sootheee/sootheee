package com.application.soothee.dairy.domain;

import com.application.soothee.common.Entity.BaseEntity;
import com.application.soothee.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dairy")
public class Dairy extends BaseEntity {
    @Id
    @Column(name = "dairy_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "score", nullable = false)
    private Float score;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cond_id", nullable = false)
    private Condition cond;

    @Column(name = "content", length = 600)
    private String content;

    @Column(name = "hope")
    private String hope;

    @Column(name = "thank")
    private String thank;

    @Column(name = "learn")
    private String learn;
}