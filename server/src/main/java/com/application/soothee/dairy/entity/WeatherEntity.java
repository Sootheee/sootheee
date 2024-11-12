package com.application.soothee.dairy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "weather")
public class WeatherEntity {
    protected WeatherEntity() {}

    @Id
    @Column(name = "weather_id", nullable = false)
    private Long id;

    @Column(name = "weather_name", nullable = false, length = 10)
    private String weatherName;

    @Column(name = "weather_icon", nullable = false)
    private String weatherIcon;
}