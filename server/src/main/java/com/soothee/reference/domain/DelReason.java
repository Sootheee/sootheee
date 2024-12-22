package com.soothee.reference.domain;

import com.soothee.common.domain.Domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Reference Entity
 * 탈퇴 사유 조회만 가능 (수정/삭제 불가)
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "del_reason")
public class DelReason implements Domain {
    /** 탈퇴 사유 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reasonId;

    /** 탈퇴 사유 */
    @Column(name = "content", nullable = false)
    private String content;

    @Override
    public Long getId() {
        return reasonId;
    }
}
