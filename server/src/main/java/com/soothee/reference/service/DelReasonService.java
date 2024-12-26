package com.soothee.reference.service;

import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.reference.domain.DelReason;

public interface DelReasonService {
    /**
     * 탈퇴 사유 일련번호로 탈퇴 사유 조회
     * 1. 탈퇴 사유 일련번호로 조회된 탈퇴 사유가 없는 경우 Exception 발생
     *
     * @param reasonId 조회할 탈퇴 사유 일련번호
     * @return 조회된 탈퇴 사유
     */
    DelReason getDelReasonById(String reasonId) throws NullValueException, IncorrectValueException;
}
