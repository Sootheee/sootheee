package com.soothee.reference.domain;

import com.soothee.common.domain.Reference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Reference Entity
 * 날씨 조회만 가능 (수정/삭제 불가)
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "weather")
public class Weather implements Reference {
    /** 날씨 일련번호 */
    @Id
    private String weatherId;

    /** 날씨 이름 */
    @Column(name = "weather_name", nullable = false, length = 10)
    private String weatherName;

    @Override
    public String getId() {
        return weatherId;
    }
}