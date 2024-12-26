package com.soothee.reference.domain;

import com.soothee.common.constants.ReferenceType;
import com.soothee.common.domain.Reference;
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
public class Condition implements Reference {
    /** 컨디션 일련번호 */
    @Id
    private String condId;

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

    @Override
    public String getId() {
        return condId;
    }
}