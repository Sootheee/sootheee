package com.soothee.reference.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Reference Entity
 * 컨디션 조회만 가능 (수정/삭제 불가)
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "conditions")
public class Condition {
    /** 컨디션 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** 컨디션 */
    @Column(name = "cond_name", nullable = false, length = 10)
    private String condName;

    /** 컨디션 타입(긍정/보통/부정) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cond_type_id", nullable = false)
    private ConditionType condType;

    /** 컨디션 부여 점수 */
    @Column(name = "cond_value", nullable = false)
    private Integer condValue;
}