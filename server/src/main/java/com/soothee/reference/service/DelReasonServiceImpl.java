package com.soothee.reference.service;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.reference.domain.DelReason;
import com.soothee.reference.repository.DelReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DelReasonServiceImpl implements DelReasonService {
    private final DelReasonRepository delReasonRepository;

    @Override
    public DelReason getDelReasonById(Long reasonId) {
        return delReasonRepository.findByReasonId(reasonId)
                .orElseThrow(() -> new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.NULL_VALUE));
    }
}
