package com.application.soothee.dairy.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "condition_type")
public class ConditionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cond_type_id", nullable = false)
    private Long id;

    @Column(name = "cond_type_name", nullable = false, length = 10)
    private String condTypeName;

    @Column(name = "cond_type_value", nullable = false)
    private Integer condTypeValue;

}