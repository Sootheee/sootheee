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
import java.util.Optional;

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
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param date      LocalDate : 조회할 날짜
     * @return DairyDTO : 조회한 일기 모든 정보
     */
    @Override
    public DairyDTO getDairyByDate(AuthenticatedUser loginInfo, LocalDate date) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        Optional<List<DairyDTO>> dairyDTO = dairyRepository.findByDate(loginMember.getMemberId(), date);
        DairyDTO result = this.getOneDTOFromOptionalList(dairyDTO);
        result.setCond(dairyConditionService.getConditionsIdListByDairy(result.getDairyId()));
        return result;
    }
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
    /**
    /**
     * Optional로 둘러쌓인 DB 조회 결과 List의 유일한 객체 가져오기</hr>
     * 결과가 없거나, 중복된 결과가 있는 경우 Exception 발생
     * @param dairyDTOList Optional<List<DairyDTO>> : DB 결과 List를 Optional로 감쌈
     * @return DairyDTO : 유일한 객체
     */
    private DairyDTO getOneDTOFromOptionalList (Optional<List<DairyDTO>> dairyDTOList) {
        List<DairyDTO> dairyDTO = dairyDTOList.orElseThrow(() -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NOT_EXIST_DAIRY));
        if (dairyDTO.size() > 1) {
            throw new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.DUPLICATED_DAIRY);
        }
        return dairyDTO.get(0);
    }

    /**
     * 새로운 일기 등록</hr>
     * 해당 일자에 이미 등록된 일기가 있으면 등록 불가
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param inputInfo DairyRegisterDTO : 등록할 일기 정보
     */
    @Override
    public void registerDairy(AuthenticatedUser loginInfo, DairyRegisterDTO inputInfo) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        Weather weather = weatherService.getWeatherById(inputInfo.getWeatherId());
        /* 이미 등록된 일기가 있으면 Exception 발생 */
        if (Objects.nonNull(this.getDairyByDate(loginInfo, inputInfo.getDate()))) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.ALREADY_EXIST_DAIRY);
        }
        Dairy newDairy = Dairy.of(inputInfo,loginMember, weather);
        dairyRepository.save(newDairy);
        dairyConditionService.saveConditions(inputInfo.getCondIdList(), newDairy);
    }
}
