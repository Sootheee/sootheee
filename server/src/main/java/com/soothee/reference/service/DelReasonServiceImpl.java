package com.soothee.reference.service;

import com.soothee.common.constants.ReferenceType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.reference.domain.DelReason;
import com.soothee.reference.repository.DelReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DelReasonServiceImpl implements DelReasonService {
    private final DelReasonRepository delReasonRepository;

    @Override
    public DelReason getDelReasonById(String reasonId) throws NullValueException, IncorrectValueException {
        /* 탈퇴 사유 일련번호로 탈퇴 사유 조회 */
        DelReason result =  delReasonRepository.findByReasonId(reasonId)
                /* 탈퇴 사유 일련번호로 조회된 탈퇴 사유가 없는 경우 Exception 발생 */
                .orElseThrow(() -> new NullValueException(reasonId, ReferenceType.DEL_REASON));
        result.valid();
        return result;
    }
}
