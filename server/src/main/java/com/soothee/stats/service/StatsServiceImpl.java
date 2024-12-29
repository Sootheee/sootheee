package com.soothee.stats.service;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.custom.exception.*;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.stats.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService{
    private final DairyRepository dairyRepository;
    private final DairyConditionRepository dairyConditionRepository;

    @Override
    public MonthlyStatsDTO getMonthlyStatsInfo(Long memberId, MonthParam monthParam) throws DuplicatedResultException, NullValueException, ErrorToSearchStatsException, NotFoundDetailInfoException, IncorrectValueException {
        /* 월간 일기 작성 갯수와 오늘의 점수 평균 조회
         * - 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생
         * - 통계 결과가 1개 초과로 중복된 경우 Exception 발생
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        MonthlyStatsDTO result = getDairyMonthlyStatsInfoIfEnoughToStats(memberId, monthParam);
        if (result.getCount() < 3) {
            /* 작성한 일기의 수가 통계 가능 최소 일기 개수(3) 보다 적은 경우 더 이상 조회하지 않고, 바로 리턴*/
            return result;
        }

        /* 한 달간 선택한 모든 컨디션의 갯수 조회 */
        Integer condCnt = dairyConditionRepository.getAllDairyConditionCntInMonth(memberId, monthParam)
                /* 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생 */
                .orElseThrow(() -> new ErrorToSearchStatsException(DomainType.DAIRY_CONDITION));
        /* 일기-컨디션이 없는 경우 바로 결과 리턴 */
        if (condCnt < 1) {
            return result;
        }

        /* 선택한 컨디션이 있는 경우 한 달 동안 선택한 모든 컨디션 중 가장 많이 선택한 컨디션과 비율 조회 */
        List<ConditionRatio> mostCondList = dairyConditionRepository.findConditionRatioListInMonth(memberId, monthParam, 1, condCnt)
                /* 선택한 컨디션이 있지만 통계 결과를 불러오지 못한 경우 Exception 발생 */
                .orElseThrow(() -> new NotFoundDetailInfoException(DomainType.DAIRY_CONDITION));
        /* 통계 결과 중복 확인
         * - 통계 결과가 1개 초과로 중복된 경우 Exception 발생 */
        SootheeValidation.checkStatsDuplicate(mostCondList.size(), DomainType.DAIRY_CONDITION);
        ConditionRatio mostCond = mostCondList.get(0);
        result.setMostCondId(mostCond.getCondId());
        result.setMostCondRatio(mostCond.getCondRatio());
        return result;
    }


    @Override
    public MonthlyContentsDTO getMonthlyContents(Long memberId, ContentType type, MonthParam monthParam) throws NotFoundDetailInfoException, DuplicatedResultException, IncorrectValueException, NullValueException, ErrorToSearchStatsException {
        /* 한 달간 작성한 감사한/배운 일 갯수 조회 */
        Integer count = dairyRepository.findDiaryContentCntInMonth(memberId, type, monthParam)
                /* 조회 결과가 아예 나오지 않는 경우 - 0이라도 나와야함 Exception 발생 */
                .orElseThrow(() -> new ErrorToSearchStatsException(type));

        /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        MonthlyContentsDTO result = new MonthlyContentsDTO(count, type);
        if (count < 1) {
            /* 한 달간 작성한 감사한/배운 일이 없는 경우 더 이상 조회하지 않고 리턴 */
            return result;
        }

        /* 감사한/배운 일을 기록한 날 중 가장 높은 점수를 기록한 날의 감사한/배운 일 정보 조회 */
        List<DateContents> highList = dairyRepository.findDiaryContentInMonthHL(memberId, type, monthParam, SortType.HIGH)
                /* 감사한/배운 일을 기록한 날 중 가장 높은 점수를 기록한 날의 감사한/배운 일 정보가 없는 경우 Exception 발생 */
                .orElseThrow(() -> new NotFoundDetailInfoException(type, SortType.HIGH));
        /* 통계 결과 중복 확인
         * - 통계 결과가 1개 초과로 중복된 경우 Exception 발생 */
        SootheeValidation.checkStatsDuplicate(highList.size(), type, SortType.HIGH);
        DateContents high = highList.get(0);

        /* 감사한/배운 일을 기록한 날 중 가장 낮은 점수를 기록한 날의 감사한/배운 일 정보 조회 */
        List<DateContents> lowList = dairyRepository.findDiaryContentInMonthHL(memberId, type, monthParam, SortType.LOW)
                /* 감사한/배운 일을 기록한 날 중 가장 낮은 점수를 기록한 날의 감사한/배운 일 정보가 없는 경우 Exception 발생 */
                .orElseThrow(() -> new NotFoundDetailInfoException(type, SortType.LOW));
        /* 통계 결과 중복 확인
         * - 통계 결과가 1개 초과로 중복된 경우 Exception 발생 */
        SootheeValidation.checkStatsDuplicate(lowList.size(), type, SortType.LOW);
        DateContents low = lowList.get(0);

        result.setHighest(high);
        result.setLowest(low);
        return result;
    }

    @Override
    public WeeklyStatsDTO getWeeklyStatsInfo(Long memberId, WeekParam weekParam) throws DuplicatedResultException, ErrorToSearchStatsException, NotFoundDetailInfoException, IncorrectValueException, NullValueException {
        /* 한 주간 작성한 일기 갯수와 평균 오늘의 점수 조회 */
        List<WeeklyStatsDTO> resultList = dairyRepository.findDiaryStatsInWeekly(memberId, weekParam)
                /* 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생 */
                .orElseThrow(() -> new ErrorToSearchStatsException(DomainType.DAIRY));
        /* 통계 결과 중복 확인
         * - 통계 결과가 1개 초과로 중복된 경우 Exception 발생 */
        SootheeValidation.checkStatsDuplicate(resultList.size(), DomainType.DAIRY);

        WeeklyStatsDTO result = resultList.get(0);
        if (isNoResult(result.getCount())) {
        if (result.getCount() < 1) {
            /* 한 달간 작성한 일기가 없는 경우 더 이상 조회하지 않고 리턴 */
            return result;
        }

        /* 한 주간 작성한 모든 일기(일련번호, 작성 날짜, 오늘의 점수) 정보 리스트 조회 */
        List<DateScore> scoreList = dairyRepository.findDiaryScoresInWeekly(memberId, weekParam)
                /* 한 주간 작성한 일기가 있지만, 일기 세부 정보를 가지고 올 수 없는 경우 */
                .orElseThrow(() -> new NotFoundDetailInfoException(DomainType.DAIRY));
        /* 조회한 모든 컨디션의 갯수와 세부 정보 리스트의 길이가 일치하지 않는 경우 Exception 발생 */
        SootheeValidation.checkListSize(result.getCount(), scoreList.size(), DomainType.DAIRY);
        result.setScoreList(scoreList);
        return result;
    }

    @Override
    public MonthlyConditionsDTO getMonthlyConditionList(Long memberId, MonthParam monthParam) throws ErrorToSearchStatsException, IncorrectValueException, NullValueException, NotFoundDetailInfoException {
        /* 한 달간 선택한 모든 컨디션의 갯수 조회 */
        Integer count = dairyConditionRepository.getAllDairyConditionCntInMonth(memberId, monthParam)
                /* 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생 */
                .orElseThrow(() -> new ErrorToSearchStatsException(DomainType.DAIRY_CONDITION));

        /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        MonthlyConditionsDTO result = new MonthlyConditionsDTO(count);
        if (count < 1) {
            /* 한 달간 선택한 컨디션이 없는 경우 더 이상 조회하지 않고 리턴 */
            return result;
        }

        /* 한 달간 선택한 모든 컨디션의 세부 정보 리스트 조회 */
        List<ConditionRatio> condRatioList = dairyConditionRepository.findConditionRatioListInMonth(memberId, monthParam, 3, count)
                /* 선택한 컨디션이 있지만 통계 결과를 불러오지 못한 경우 Exception 발생 */
                .orElseThrow(() -> new NotFoundDetailInfoException(DomainType.DAIRY_CONDITION));
        /* 조회한 모든 컨디션의 갯수와 세부 정보 리스트의 길이가 일치하지 않는 경우 Exception 발생 */
        SootheeValidation.checkListSize(count, condRatioList.size(), DomainType.DAIRY_CONDITION);
        result.setCondiList(condRatioList);
        return result;
    }

    @Override
    public MonthlyAllContentsDTO getAllContentsInMonth(Long memberId, ContentType type, MonthParam monthParam, SortType orderBy) throws ErrorToSearchStatsException, NotFoundDetailInfoException, IncorrectValueException, NullValueException {
        /* 한 달간 작성한 감사한/배운 일 갯수 */
        Integer count = dairyRepository.findDiaryContentCntInMonth(memberId, type, monthParam)
                /* 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생 */
                .orElseThrow(() -> new ErrorToSearchStatsException(type));

        /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        MonthlyAllContentsDTO result = new MonthlyAllContentsDTO(count, type);
        if (count < 1) {
            /* 한 달간 작성한 감사한/배운 일이 없는 경우 더 이상 조회하지 않고 리턴 */
            return result;
        }

        /* 한 달간 작성한 감사한/배운 일의 세부 정보 리스트 조회 */
        List<DateContents> contList = dairyRepository.findDiaryContentInMonthSort(memberId, type, monthParam, orderBy)
                /* 작성한 감사한/배운 일이 있지만 통계 결과를 불러오지 못한 경우 Exception 발생 */
                .orElseThrow(() -> new NotFoundDetailInfoException(type));
        /* 조회한 감사한/배운 일의 갯수와 세부 정보 리스트의 길이가 일치하지 않는 경우 Exception 발생 */
        SootheeValidation.checkListSize(count, contList.size(), type);
        result.setContentList(contList);
        return result;
    }

    /**
     * 통계 가능한 조건에 부합한다면 월간 일기 요약 정보(작성 갯수 & 오늘의 점수 평균) 조회
     * 통계 가능한지 확인용으로도 사용됨
     * - 삭제한 일기 제외
     * 1. 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생
     * 2. 통계 결과가 1개 초과로 중복된 경우 Exception 발생
     * 3. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     * 4. 작성한 일기 갯수가 통계 가능 최소 일기 작성 갯수(3)보다 적은 경우 Exception 발생
     *
     * @param memberId 현재 로그인한 회원 일련번호
     * @param monthParam 조회할 년도/월 조건
     * @return 한 달간 작성한 일기 갯수와 오늘의 점수 평균 조회
     */
    private MonthlyStatsDTO getDairyMonthlyStatsInfoIfEnoughToStats(Long memberId, MonthParam monthParam) throws ErrorToSearchStatsException, DuplicatedResultException, IncorrectValueException, NullValueException {
        /* 한 달간 작성한 일기 갯수와 평균 오늘의 점수 조회 */
        List<MonthlyStatsDTO> resultList = dairyRepository.findDiaryStatsInMonth(memberId, monthParam)
                /* 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생 */
                .orElseThrow(() -> new ErrorToSearchStatsException(DomainType.DAIRY));
        /* 통계 결과 중복 확인
         * - 통계 결과가 1개 초과로 중복된 경우 Exception 발생 */
        SootheeValidation.checkStatsDuplicate(resultList.size(), DomainType.DAIRY);
        MonthlyStatsDTO result = resultList.get(0);
        return result;
    }
}
