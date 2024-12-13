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
import com.soothee.stats.dto.MonthlyStatsDTO;
import com.soothee.stats.dto.WeeklyStatsDTO;
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

    @Override
    public List<DairyScoresDTO> getAllDairyMonthly(AuthenticatedUser loginInfo, Integer year, Integer month) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        return dairyRepository.findByMemberIdYearMonth(loginMember.getMemberId(), year, month).orElse(new ArrayList<>());
    }

    @Override
    public DairyDTO getDairyByDate(AuthenticatedUser loginInfo, LocalDate date) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        DairyDTO dairyDTO = dairyRepository.findByDate(loginMember.getMemberId(), date).orElse(new DairyDTO());
        if (Objects.nonNull(dairyDTO.getDairyId())) {
            dairyDTO.setCondIds(dairyConditionService.getConditionsIdListByDairy(dairyDTO.getDairyId()));
        }
        return dairyDTO;
    }

    @Override
    public DairyDTO getDairyByDairyId(AuthenticatedUser loginInfo, Long dairyId) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        DairyDTO dairyDTO = dairyRepository.findByDiaryId(loginMember.getMemberId(), dairyId).orElse(new DairyDTO());
        if (Objects.nonNull(dairyDTO.getDairyId())) {
            dairyDTO.setCondIds(dairyConditionService.getConditionsIdListByDairy(dairyDTO.getDairyId()));
        }
        return dairyDTO;
    }

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

    @Override
    public void modifyDairy(AuthenticatedUser loginInfo, Long dairyId, DairyDTO inputInfo) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        Dairy dairy = this.getDairyByDairyId(inputInfo.getDairyId());
        if (!Objects.equals(dairyId, inputInfo.getDairyId())
            || !dairy.getDate().isEqual(inputInfo.getDate())
            || !Objects.equals(dairy.getMember().getMemberId(), loginMember.getMemberId())) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        Weather weather = weatherService.getWeatherById(inputInfo.getWeatherId());
        dairy.updateDairy(inputInfo, weather);
        dairyConditionService.updateConditions(dairy, inputInfo.getCondIds());
    }

    @Override
    public void deleteDairy(AuthenticatedUser loginInfo, Long dairyId) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        Dairy dairy = this.getDairyByDairyId(dairyId);
        if (!Objects.equals(dairy.getMember().getMemberId(), loginMember.getMemberId())) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        dairyConditionService.deleteDairyConditionsOfDairy(dairy);
        dairy.deleteDairy();
    }

    /**
     * 일기 일련변호로 일기 가져오기</hr>
     *
     * @param dairyId Long : 가져올 일기 일련번호
     * @return Dairy : 가져온 일기
     */
    private Dairy getDairyByDairyId(Long dairyId) {
        return dairyRepository.findByDairyId(dairyId)
                .orElseThrow(() -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NOT_EXIST_DAIRY));
    }
}
