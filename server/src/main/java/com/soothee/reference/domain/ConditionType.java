package com.soothee.reference.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "condition_type")
public class ConditionType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cond_type_name", nullable = false, length = 10)
    private String condTypeName;

    @Column(name = "cond_type_value", nullable = false)
    private Integer condTypeValue;
}