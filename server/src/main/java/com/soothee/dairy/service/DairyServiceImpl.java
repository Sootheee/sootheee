package com.soothee.dairy.service;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.domain.Member;
import com.soothee.member.service.MemberService;
import com.soothee.oauth2.domain.AuthenticatedUser;
import com.soothee.reference.domain.Weather;
import com.soothee.reference.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DairyServiceImpl implements DairyService {
    private final DairyRepository dairyRepository;
    private final MemberService memberService;
    private final WeatherService weatherService;
    private final DairyConditionService dairyConditionService;

    /**
     * 지정한 달에 작성한 모든 일기의 오늘의 점수 리스트 조회</hr>
     * 삭제된 일기 제외
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param year      Integer : 지정한 년도
     * @param month     Integer : 지정한 달
     * @return List<DairyScoresDTO> : 일기 일련번호와 날짜, 오늘의 점수 리스트
     */
    @Override
    public List<DairyScoresDTO> getAllDairyMonthly(AuthenticatedUser loginInfo, Integer year, Integer month) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        return dairyRepository.findByMemberIdYearMonth(loginMember.getMemberId(), year, month)
                .orElseThrow(() -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NOT_EXIST_DAIRY));
    }

    /**
     * 해당 날짜 일기 조회</hr>
     * 삭제된 일기 제외
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param date      LocalDate : 조회할 날짜
     * @return DairyDTO : 조회한 일기 모든 정보
     */
    @Override
    public DairyDTO getDairyByDate(AuthenticatedUser loginInfo, LocalDate date) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        DairyDTO dairyDTO = dairyRepository.findByDate(loginMember.getMemberId(), date)
                .orElseThrow(() -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NOT_EXIST_DAIRY));
        dairyDTO.setCondIds(dairyConditionService.getConditionsIdListByDairy(dairyDTO.getDairyId()));
        return dairyDTO;
    }

    /**
     * 해당 일기 일련번호 일기 조회</hr>
     * 삭제된 일기 제외
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param dairyId   Long : 조회할 일기 일련번호
     * @return DairyDTO : 조회한 일기 모든 정보
     */
    @Override
    public DairyDTO getDairyByDairyId(AuthenticatedUser loginInfo, Long dairyId) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        DairyDTO dairyDTO = dairyRepository.findByDiaryId(loginMember.getMemberId(), dairyId)
                .orElseThrow(() -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NOT_EXIST_DAIRY));
        dairyDTO.setCondIds(dairyConditionService.getConditionsIdListByDairy(dairyDTO.getDairyId()));
        return dairyDTO;
    }

    /**
     * 새로운 일기 등록</hr>
     * 해당 일자에 이미 등록된 일기가 있으면 등록 불가
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param inputInfo DairyRegisterDTO : 등록할 일기 정보
     */
    @Override
    public void registerDairy(AuthenticatedUser loginInfo, DairyRegisterDTO inputInfo) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        /* 이미 등록된 일기가 있으면 Exception 발생 */
        if (Objects.nonNull(this.getDairyByDate(loginInfo, inputInfo.getDate()))) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.ALREADY_EXIST_DAIRY);
        }
        Weather weather = weatherService.getWeatherById(inputInfo.getWeatherId());
        Dairy newDairy = Dairy.of(inputInfo,loginMember, weather);
        dairyRepository.save(newDairy);
        dairyConditionService.saveConditions(inputInfo.getCondIdList(), newDairy);
    }

    /**
     * 기존 일기 수정</hr>
     * 1. path의 일련번호와 query의 일련번호가 다르면 수정 불가
     * 2. 기존 dairy의 date와 query의 date가 다르면 수정 불가
     * 3. 현재 로그인한 계정이 기존 dairy의 등록자가 아닌경우 수정 불가
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param dairyId   Long : 수정할 일기 일련번호
     * @param inputInfo DairyDTO : 수정할 일기 정보
     */
    @Override
    public void modifyDairy(AuthenticatedUser loginInfo, Long dairyId, DairyDTO inputInfo) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        Dairy dairy = dairyRepository.findByDairyId(inputInfo.getDairyId())
                .orElseThrow(() -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NOT_EXIST_DAIRY));
        if (!Objects.equals(dairyId, inputInfo.getDairyId())
            || !dairy.getDate().isEqual(inputInfo.getDate())
            || !Objects.equals(dairy.getMember().getMemberId(), loginMember.getMemberId())) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        Weather weather = weatherService.getWeatherById(inputInfo.getWeatherId());
        dairy.updateDairy(inputInfo, weather);
        dairyConditionService.updateConditions(dairy, inputInfo.getCondIds());
    }

    /**
     * 작성된 일기 삭제</hr>
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param dairyId   Long : 삭제할 일기 일련번호
     */
    @Override
    public void deleteDairy(AuthenticatedUser loginInfo, Long dairyId) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        Dairy dairy = dairyRepository.findByDairyId(dairyId)
                .orElseThrow(() -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NOT_EXIST_DAIRY));
        if (!Objects.equals(dairy.getMember().getMemberId(), loginMember.getMemberId())) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        dairyConditionService.deleteDairyConditionsOfDairy(dairy);
        dairy.deleteDairy();
    }
}
