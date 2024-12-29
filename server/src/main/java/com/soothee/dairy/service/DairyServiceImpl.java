package com.soothee.dairy.service;

import com.soothee.custom.exception.*;
import com.soothee.dairy.controller.response.DairyAllResponse;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.controller.response.DairyScoresResponse;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.dairy.service.command.DairyInputInfo;
import com.soothee.dairy.service.command.DairyModify;
import com.soothee.dairy.service.command.DairyRegister;
import com.soothee.member.domain.Member;
import com.soothee.member.service.MemberService;
import com.soothee.reference.domain.Weather;
import com.soothee.reference.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.soothee.custom.valid.SootheeValidation.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DairyServiceImpl implements DairyService {
    private final DairyRepository dairyRepository;
    private final MemberService memberService;
    private final WeatherService weatherService;
    private final DairyConditionService dairyConditionService;

    @Override
    public List<DairyScoresResponse> getAllDairyMonthly(Long memberId, int year, int month) {
        return dairyRepository.findScoreListInMonth(memberId, year, month)
                .orElse(new ArrayList<>());
    }

    @Override
    public DairyAllResponse getDairyByDate(Long memberId, LocalDate date) throws NoDairyResultException, DuplicatedResultException, NotFoundDairyConditionsException {
        List<DairyAllResponse> dairyAllResponse = dairyRepository.findAllDairyInfoByDate(memberId, date)
                .orElseThrow(() -> new NoDairyResultException(memberId, date));
        checkOnlyOneResult(dairyAllResponse.size());
        return getDairyInfoWithCondIdListIfExist(dairyAllResponse);
    }

    @Override
    public DairyAllResponse getDairyByDairyId(Long memberId, Long dairyId) throws NoDairyResultException, DuplicatedResultException, NotFoundDairyConditionsException {
        List<DairyAllResponse> dairyAllResponse = dairyRepository.findAllDairyInfoByDiaryId(memberId, dairyId)
                .orElseThrow(() -> new NoDairyResultException(memberId, dairyId));
        checkOnlyOneResult(dairyAllResponse.size());
        return getDairyInfoWithCondIdListIfExist(dairyAllResponse);
    }

    @Override
    public void registerDairy(DairyRegister dairyInfo) throws DuplicatedResultException, NullValueException, NotExistMemberException {
        checkDuplicateDairy(dairyInfo.getMemberId(), dairyInfo.getDate());

        Member member = memberService.getMemberById(dairyInfo.getMemberId());
        Weather weather = weatherService.getWeatherById(dairyInfo.getWeatherId());
        Dairy newDairy = dairyInfo.toDairy(member, weather);

        dairyRepository.save(newDairy);
        if (isExistSelectedConditions(dairyInfo)) {
            dairyConditionService.saveConditions(dairyInfo.getCondIdList(), newDairy);
        }
    }

    @Override
    public void modifyDairy(Long curDairyId, DairyModify dairyInfo) throws NoDairyResultException, NotMatchedException, NoAuthorizeException, NullValueException, NotFoundDairyConditionsException {
        Dairy dairy = getDairyByDairyId(dairyInfo.getDairyId());
        checkSameDairy(curDairyId, dairy.getDairyId());
        checkSameDate(dairy.getDate(), dairyInfo.getDate());
        checkAuthorizedMember(dairy.getMember().getMemberId(), dairyInfo.getMemberId());

        Weather weather = weatherService.getWeatherById(dairyInfo.getWeatherId());
        dairy.updateDairy(dairyInfo, weather);

        if (isExistSelectedConditions(dairyInfo)) {
            dairyConditionService.updateConditions(dairy, dairyInfo.getCondIdList());
        }
    }

    @Override
    public void deleteDairy(Long memberId, Long dairyId) throws NoDairyResultException, NoAuthorizeException, NotFoundDairyConditionsException {
        Dairy dairy = getDairyByDairyId(dairyId);
        checkAuthorizedMember(dairy.getMember().getMemberId(), memberId);

        if (isExistSelectedConditionInDairy(dairyId)) {
            dairyConditionService.deleteDairyConditionsOfDairy(dairy);
        }
        dairy.deleteDairy();
    }

    /**
     * 현재 로그인한 회원이 해당 일자에 써놓은 일기가 있는지 중복 체크
     *
     * @param memberId 현재 로그인한 회원의 일련번호
     * @param date 조회할 날짜
     */
    private void checkDuplicateDairy(Long memberId, LocalDate date) throws DuplicatedResultException {
        if (dairyRepository.findAllDairyInfoByDate(memberId, date).isPresent()) {
            throw new DuplicatedResultException();
        }
    }

    /**
     * 일기 일련변호로 일기 가져오기
     *
     * @param dairyId 가져올 일기 일련번호
     * @return 가져온 일기
     */
    private Dairy getDairyByDairyId(Long dairyId) throws NoDairyResultException {
        List<Dairy> result = dairyRepository.findDairyByDairyId(dairyId)
                .orElseThrow(() -> new NoDairyResultException(dairyId));
        checkOnlyOneResult(result.size());
        return result.get(0);
    }

    /**
     * 입력 값에 선택한 컨디션 일련번호가 있는지 확인
     *
     * @param inputInfo 입력 값
     * @return 있으면 true / 없으면 false
     */
    private static boolean isExistSelectedConditions(DairyInputInfo inputInfo) {
        return Objects.nonNull(inputInfo.getCondIdList());
    }

    private DairyAllResponse getDairyInfoWithCondIdListIfExist(List<DairyAllResponse> dairyAllResponse) throws NotFoundDairyConditionsException {
        DairyAllResponse result = dairyAllResponse.get(0);
        if (isExistSelectedConditionInDairy(result.getDairyId())) {
            result.setCondIdList(dairyConditionService.getConditionsIdListByDairy(result.getDairyId()));
        }
        return result;
    }

    private boolean isExistSelectedConditionInDairy(Long dairyId) {
        return dairyConditionService.isExistSelectedConditionsInDairy(dairyId);
    }
}
