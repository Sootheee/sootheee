package com.soothee.member.service;

import com.soothee.member.domain.Member;
import com.soothee.member.domain.MemberDelReason;
import com.soothee.member.repository.MemberDelReasonRepository;
import com.soothee.reference.domain.DelReason;
import com.soothee.reference.service.DelReasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberDelReasonServiceImpl implements MemberDelReasonService {
    private final MemberDelReasonRepository memberDelReasonRepository;
    private final DelReasonService delReasonService;

    @Override
    public void saveDeleteReasons(Member loginMember, List<Long> delReasonList) {
        for (Long delReasonId : delReasonList) {
            DelReason delReason = delReasonService.getDelReasonById(delReasonId);
            memberDelReasonRepository.save(MemberDelReason.builder()
                                                            .member(loginMember)
                                                            .delReason(delReason)
                                                            .build());
        }
    }
}
