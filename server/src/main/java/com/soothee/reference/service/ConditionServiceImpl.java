package com.soothee.reference.service;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.repository.ConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConditionServiceImpl implements ConditionService{
    private final ConditionRepository conditionRepository;
    /**
     * 컨디션 일련번호로 컨디션 정보 조회
     *
     * @param condId Log : 해당 컨디션 일련번호
     * @return Condition : 해당 컨디션 정보
     */
    @Override
    public Condition getConditionById(Long condId) {
        Optional<Condition> optional = conditionRepository.findByCondId(condId);
        return optional.orElseThrow(() -> new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.NULL_VALUE));
    }
}
