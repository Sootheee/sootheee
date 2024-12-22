package com.soothee.reference.service;

import com.soothee.reference.domain.DelReason;

public interface DelReasonService {
    /**
     * 탈퇴 사유 일련번호로 탈퇴 사유 조회
     *
     * @param reasonId 조회할 탈퇴 사유 일련번호
     * @return 조회된 탈퇴 사유
     */
    DelReason getDelReasonById(Long reasonId);
}
