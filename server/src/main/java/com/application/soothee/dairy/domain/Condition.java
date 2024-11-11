package com.application.soothee.dairy.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "conditions")
public class Condition {
    @Id
    @Column(name = "cond_id", nullable = false)
    private Long id;

    @Column(name = "cond_name", nullable = false, length = 10)
    private String condName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cond_type_id", nullable = false)
    private ConditionType condType;

    @Column(name = "cond_value", nullable = false)
    private Integer condValue;
}