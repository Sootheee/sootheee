package com.soothee.dairy.domain;

import com.soothee.common.domain.TimeEntity;
import com.soothee.reference.domain.Condition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dairy_condition")
public class DairyCondition extends TimeEntity {
    /** 다이어리-콘텐츠 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dairyCondId;

    /** 해당 다이어리 일련번호 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dairy_id")
    private Dairy dairy;

    /** 해당 컨디션 일련번호 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cond_id")
    private Condition condition;

    /** 소프트 삭제 */
    @Column(name = "is_delete")
    private String isDelete;

    @Builder
    public DairyCondition(Dairy dairy, Condition condition) {
        this.dairy = dairy;
        this.condition = condition;
        this.isDelete = "N";
    }

    /** 다이어리 컨디션 변경 시, 소프트 삭제 된 후, 새로 생성 */
    public void deleteDairyCondition () {
        this.isDelete = "Y";
    }

    public static DairyCondition of(Dairy dairy, Condition condition) {
        return DairyCondition.builder()
                .dairy(dairy)
                .condition(condition)
                .build();
    }
}
