package com.application.soothee.dairy.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "condition_type")
public class ConditionTypeEntity {
    protected ConditionTypeEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cond_type_id", nullable = false)
    private Long id;

    @Column(name = "cond_type_name", nullable = false, length = 10)
    private String condTypeName;

    @Column(name = "cond_type_value", nullable = false)
    private Integer condTypeValue;

}