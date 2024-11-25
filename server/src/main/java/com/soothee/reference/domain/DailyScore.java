package com.soothee.reference.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Reference Entity
 * 오늘의 점수 조회만 가능 (수정/삭제 불가)
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_score")
public class DailyScore {
    /** 점수 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** 오늘의 점수 */
    @Column(name = "daily_score", nullable = false)
    private Integer dailyScore;

    /** 오늘의 점수 아이콘 Path */
    @Column(name = "daily_score_icon", nullable = false)
    private String dailyScoreIcon;
}