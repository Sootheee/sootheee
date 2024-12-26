package com.soothee.reference.service;

import com.soothee.common.constants.ReferenceType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.repository.ConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConditionServiceImpl implements ConditionService{
    private final ConditionRepository conditionRepository;

    @Override
    public Condition getConditionById(String condId) throws NullValueException, IncorrectValueException {
        Condition result = conditionRepository.findByCondId(condId)
                .orElseThrow(() -> new NullValueException(condId, ReferenceType.CONDITION));
        result.valid();
        return result;
    }
}
