package com.soothee.dairy.domain;

import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.StringType;
import com.soothee.common.domain.Domain;
import com.soothee.common.domain.TimeEntity;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
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
public class DairyCondition extends TimeEntity implements Domain {
    /** 일기-콘텐츠 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dairyCondId;

    /** 해당 일기 일련번호 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dairy_id", nullable = false)
    private Dairy dairy;

    /** 해당 컨디션 일련번호 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cond_id", nullable = false)
    private Condition condition;

    /** 선택 순서 */
    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    /** 소프트 삭제 */
    @Column(name = "is_delete", nullable = false, length = 1)
    private String isDelete;

    @Builder
    public DairyCondition(Dairy dairy, Condition condition, Integer orderNo) throws IncorrectValueException, NullValueException {
        checkConstructorDairyCondition(dairy, condition, orderNo);
        this.dairy = dairy;
        this.condition = condition;
        this.orderNo = orderNo;
        this.isDelete = "N";
    }

    /** 일기 컨디션 변경 시, 소프트 삭제 된 후, 새로 생성 */
    public void deleteDairyCondition () {
        this.isDelete = "Y";
    }

    @Override
    public Long getId() {
        return dairyCondId;
    }

    /** validation */
    private static void checkConstructorDairyCondition(Dairy dairy, Condition condition, Integer orderNo) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomain(dairy, DomainType.DAIRY);
        SootheeValidation.checkDomain(condition, DomainType.CONDITION);
        SootheeValidation.checkInteger(orderNo, StringType.ORDER_NO);
    }
}
