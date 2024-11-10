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
@Table(name = "weather")
public class Weather {
    @Id
    @Column(name = "weather_id", nullable = false)
    private Integer id;

    @Column(name = "weather_name", nullable = false, length = 10)
    private String weatherName;

    @Column(name = "weather_icon", nullable = false)
    private String weatherIcon;

}