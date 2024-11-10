package com.application.soothee.dairy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "daily_score")
public class DailyScore {
    @Id
    @Column(name = "daily_score_no", nullable = false)
    private Integer id;

    @Column(name = "daily_score_icon", nullable = false)
    private String dailyScoreIcon;

}