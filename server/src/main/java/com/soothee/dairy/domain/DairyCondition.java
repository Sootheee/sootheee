package com.soothee.dairy.domain;

import com.soothee.common.constants.*;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "is_delete", nullable = false, length = 1)
    private BooleanYN isDelete;

    @Builder
    public DairyCondition(Dairy dairy, Condition condition, Integer orderNo) {
        this.dairy = dairy;
        this.condition = condition;
        this.orderNo = orderNo;
        this.isDelete = BooleanYN.N;
    }

    /** 일기 컨디션 변경 시, 소프트 삭제 된 후, 새로 생성 */
    public void deleteDairyCondition () {
        this.isDelete = BooleanYN.Y;
    }

    @Override
    public Long getId() {
        return dairyCondId;
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void validNew() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomain(getDairy(), DomainType.DAIRY);
        SootheeValidation.checkReference(getCondition(), ReferenceType.CONDITION);
        SootheeValidation.checkInteger(getOrderNo(), StringType.ORDER_NO);
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void valid() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomainId(getDairyCondId(), DomainType.DAIRY_CONDITION);
        validNew();
        SootheeValidation.checkBoolean(getIsDelete(), BooleanType.DELETE);
    }
}
