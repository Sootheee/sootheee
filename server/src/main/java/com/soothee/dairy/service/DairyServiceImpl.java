package com.soothee.dairy.service;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.MonthlyDairyScoreDTO;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.MemberDTO;
import com.soothee.member.service.MemberService;
import com.soothee.oauth2.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DairyServiceImpl implements DairyService {
    private final DairyRepository dairyRepository;
    private final MemberService memberService;
    /**
     * 지정한 달에 작성한 모든 일기의 오늘의 점수 리스트 조회</hr>
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param year      Integer : 지정한 년도
     * @param month     Integer : 지정한 달
     * @return List<MonthlyDairyScoreDTO> : 일기 일련번호와 날짜, 오늘의 점수 리스트
     */
    @Override
    public List<MonthlyDairyScoreDTO> getAllDairyMonthly(AuthenticatedUser loginInfo, Integer year, Integer month) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        Optional<List<MonthlyDairyScoreDTO>> optional = dairyRepository.findByMemberIdAndDate(loginMember.getId(), year, month);
        return optional.orElseThrow(() -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NOT_EXIST_DAIRY));
    }

}
