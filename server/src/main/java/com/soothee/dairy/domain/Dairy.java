package com.soothee.dairy.domain;

import com.soothee.common.domain.TimeEntity;
import com.soothee.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dairy")
public class Dairy extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

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