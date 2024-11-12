package com.application.soothee.dairy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "daily_score")
public class DailyScoreEntity {
    protected DailyScoreEntity() {}

    @Id
    @Column(name = "daily_score_no", nullable = false)
    private Long id;

    @Column(name = "daily_score_icon", nullable = false)
    private String dailyScoreIcon;
}