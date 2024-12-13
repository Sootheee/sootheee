package com.soothee.reference.service;

import com.soothee.reference.domain.DelReason;

public interface DelReasonService {
    /**
     * 탈퇴 사유 일련번호로 탈퇴 사유 조회</hr>
     *
     * @param reasonId Long : 조회할 탈퇴 사유 일련번호
     * @return DelReason : 조회된 탈퇴 사유
     */
    DelReason getDelReasonById(Long reasonId);
}
