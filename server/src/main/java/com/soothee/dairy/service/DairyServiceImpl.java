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
import com.soothee.reference.domain.Weather;
import com.soothee.reference.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public List<DairyScoresDTO> getAllDairyMonthly(Long memberId, Integer year, Integer month) {
        return dairyRepository.findByMemberIdYearMonth(memberId, year, month).orElse(new ArrayList<>());
    }

    @Override
    public DairyDTO getDairyByDate(Long memberId, LocalDate date) {
        DairyDTO dairyDTO = dairyRepository.findByDate(memberId, date).orElse(new DairyDTO());
        if (Objects.nonNull(dairyDTO.getDairyId())) {
            dairyDTO.setCondIds(dairyConditionService.getConditionsIdListByDairy(dairyDTO.getDairyId()));
        }
        return dairyDTO;
    }

    @Override
    public DairyDTO getDairyByDairyId(Long memberId, Long dairyId) {
        DairyDTO dairyDTO = dairyRepository.findByMemberDiaryId(memberId, dairyId).orElse(new DairyDTO());
        if (Objects.nonNull(dairyDTO.getDairyId())) {
            dairyDTO.setCondIds(dairyConditionService.getConditionsIdListByDairy(dairyDTO.getDairyId()));
        }
        return dairyDTO;
    }

    @Override
    public void registerDairy(Long memberId, DairyRegisterDTO inputInfo) {
        /* 이미 등록된 일기가 있으면 Exception 발생 */
        if (Objects.nonNull(this.getDairyByDate(memberId, inputInfo.getDate()).getDairyId())) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.ALREADY_EXIST_DAIRY);
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
    public void modifyDairy(Long memberId, Long dairyId, DairyDTO inputInfo) {
        Dairy dairy = this.getDairyByDairyId(inputInfo.getDairyId());
        if (!Objects.equals(dairyId, inputInfo.getDairyId())
            || !dairy.getDate().isEqual(inputInfo.getDate())
            || !Objects.equals(dairy.getMember().getMemberId(), memberId)) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        Weather weather = weatherService.getWeatherById(inputInfo.getWeatherId());
        dairy.updateDairy(inputInfo, weather);
        if (Objects.nonNull(inputInfo.getCondIds())) {
            dairyConditionService.updateConditions(dairy, inputInfo.getCondIds());
        }
    }

    @Override
    public void deleteDairy(Long memberId, Long dairyId) {
        Dairy dairy = this.getDairyByDairyId(dairyId);
        if (!Objects.equals(dairy.getMember().getMemberId(), memberId)) {
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
