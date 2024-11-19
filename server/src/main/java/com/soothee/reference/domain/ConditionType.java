package com.soothee.reference.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Reference Entity
 * 컨디션 타입 조회만 가능 (수정/삭제 불가)
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "condition_type")
public class ConditionType {
    /** 컨디션 타입 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** 컨디션 타입 */
    @Column(name = "cond_type_name", nullable = false, length = 10)
    private String condTypeName;

    /** 컨디션 타입 부여 점수 */
    @Column(name = "cond_type_value", nullable = false)
    private Integer condTypeValue;
}