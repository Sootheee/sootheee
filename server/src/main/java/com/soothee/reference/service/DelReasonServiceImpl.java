package com.soothee.reference.service;

import com.soothee.common.constants.ReferenceType;
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
    public DelReason getDelReasonById(String reasonId) throws NullValueException {
        return delReasonRepository.findByReasonId(reasonId)
                .orElseThrow(() -> new NullValueException(reasonId, ReferenceType.DEL_REASON));
    }
}
