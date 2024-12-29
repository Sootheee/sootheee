package com.soothee.reference.service;

import com.soothee.custom.exception.NullValueException;
import com.soothee.reference.domain.Condition;

public interface ConditionService {
    /**
     * 컨디션 일련번호로 컨디션 정보 조회
     *
     * @param condId 해당 컨디션 일련번호
     * @return 해당 컨디션 정보
     */
    Condition getConditionById(String condId) throws NullValueException;
}
