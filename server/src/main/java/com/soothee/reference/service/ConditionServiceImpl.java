package com.soothee.reference.service;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.repository.ConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConditionServiceImpl implements ConditionService{
    private final ConditionRepository conditionRepository;

    @Override
    public Condition getConditionById(Long condId) {
        return conditionRepository.findByCondId(condId)
                .orElseThrow(() -> new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.NULL_VALUE));
    }
}
