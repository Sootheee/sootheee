package com.soothee.dairy.service;

import com.soothee.common.constants.DomainType;
import com.soothee.custom.exception.*;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.domain.Member;
import com.soothee.member.service.MemberService;
import com.soothee.reference.domain.Weather;
import com.soothee.reference.service.WeatherService;
import lombok.RequiredArgsConstructor;
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
    public List<DairyScoresDTO> getAllDairyMonthly(Long memberId, MonthParam monthParam) throws NotExistDairyException {
        return dairyRepository.findByMemberIdYearMonth(memberId, monthParam)
                .orElseThrow(() -> new NotExistDairyException(memberId, monthParam));
    }

    @Override
    public DairyDTO getDairyByDate(Long memberId, LocalDate date) throws NotExistDairyException, DuplicatedResultException, NoDairyConditionException {
        List<DairyDTO> dairyDTO = dairyRepository.findByDate(memberId, date)
                .orElseThrow(() -> new NotExistDairyException(memberId, date));
        if (dairyDTO.size() > 1) {
            throw new DuplicatedResultException(memberId, date);
        }
        return getDairyDTO(dairyDTO);
    }

    /**
     * 현재 로그인한 회원이 해당 일자에 써놓은 일기가 있는지 중복 체크
     *
     * @param memberId 현재 로그인한 회원의 일련번호
     * @param date 조회할 날짜
     * @return 중복 작성된 일기가 있으면 true / 없으면 false
     */
    private boolean checkDuplicateDairy(Long memberId, LocalDate date)   {
        return dairyRepository.findByDate(memberId, date).isPresent();
    }

    @Override
    public DairyDTO getDairyByDairyId(Long memberId, Long dairyId) throws NotExistDairyException, DuplicatedResultException, NoDairyConditionException {
        List<DairyDTO> dairyDTO = dairyRepository.findByMemberDiaryId(memberId, dairyId)
                .orElseThrow(() -> new NotExistDairyException(memberId, dairyId));
        if (dairyDTO.size() > 1) {
            throw new DuplicatedResultException(memberId, dairyId);
        }
        return getDairyDTO(dairyDTO);
    }

    private DairyDTO getDairyDTO(List<DairyDTO> dairyDTO) throws NoDairyConditionException {
        DairyDTO result = dairyDTO.get(0);
        if (dairyConditionService.isConditionExistInDairy(result.getDairyId())) {
            result.setCondIds(dairyConditionService.getConditionsIdListByDairy(result.getDairyId()));
        }
        return result;
    }

    @Override
    public void registerDairy(Long memberId, DairyRegisterDTO inputInfo) throws NotExistMemberException, NullValueException, IncorrectValueException, DuplicatedResultException {
        /* 이미 등록된 일기가 있으면 Exception 발생 */
        if (checkDuplicateDairy(memberId, inputInfo.getDate())) {
            throw new DuplicatedResultException(memberId, inputInfo.getDate());
        }
        Member member = memberService.getMemberById(memberId);
        Weather weather = weatherService.getWeatherById(inputInfo.getWeatherId());
        Dairy newDairy = Dairy.of(inputInfo, member, weather);
        dairyRepository.save(newDairy);
        if (Objects.nonNull(inputInfo.getCondIdList())) {
            dairyConditionService.saveConditions(inputInfo.getCondIdList(), newDairy);
        }
    }

    @Override
    public void modifyDairy(Long memberId, Long dairyId, DairyDTO inputInfo) throws NotExistDairyException, NullValueException, IncorrectValueException, NotMatchedException {
        Dairy dairy = this.getDairyByDairyId(inputInfo.getDairyId());
        if (!Objects.equals(dairyId, inputInfo.getDairyId())) {
            throw new NotMatchedException(dairyId, dairy.getDairyId(), DomainType.DAIRY);
        }
        if (!dairy.getDate().isEqual(inputInfo.getDate())) {
            throw new NotMatchedException(dairy.getDate(), inputInfo.getDate());
        }
       if (!Objects.equals(dairy.getMember().getMemberId(), memberId)) {
            throw new NotMatchedException(memberId, dairy.getDairyId(), DomainType.MEMBER);
        }
        Weather weather = weatherService.getWeatherById(inputInfo.getWeatherId());
        dairy.updateDairy(inputInfo, weather);
        if (Objects.nonNull(inputInfo.getCondIds())) {
            dairyConditionService.updateConditions(dairy, inputInfo.getCondIds());
        }
    }

    @Override
    public void deleteDairy(Long memberId, Long dairyId) throws NotExistDairyException, NotMatchedException, NoDairyConditionException {
        Dairy dairy = this.getDairyByDairyId(dairyId);
        if (!Objects.equals(dairy.getMember().getMemberId(), memberId)) {
            throw new NotMatchedException(memberId, dairy.getMember().getMemberId(), DomainType.MEMBER);
        }
        if (dairyConditionService.isConditionExistInDairy(dairyId)) {
            dairyConditionService.deleteDairyConditionsOfDairy(dairy);
        }
        dairy.deleteDairy();
    }

    /**
     * 일기 일련변호로 일기 가져오기
     *
     * @param dairyId 가져올 일기 일련번호
     * @return 가져온 일기
     */
    private Dairy getDairyByDairyId(Long dairyId) throws NotExistDairyException {
        return dairyRepository.findByDairyId(dairyId)
                .orElseThrow(() -> new NotExistDairyException(dairyId));
    }
}
