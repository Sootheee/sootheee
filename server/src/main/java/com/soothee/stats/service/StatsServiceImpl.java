package com.soothee.stats.service;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.SortType;
import com.soothee.custom.exception.*;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.stats.controller.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.soothee.custom.valid.SootheeValidation.checkOnlyOneStats;
import static com.soothee.custom.valid.SootheeValidation.checkSameResultCount;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService{
    private final DairyRepository dairyRepository;
    private final DairyConditionRepository dairyConditionRepository;

    @Override
    public MonthlyDairyStats getMonthlyDairyStats(Long memberId, int year, int month) throws ErrorToSearchStatsException, DuplicatedResultException, NotFoundDetailInfoException {
        List<MonthlyDairyStats> resultList = dairyRepository.findDairyStatsInMonth(memberId, year, month)
                .orElseThrow(() -> new ErrorToSearchStatsException(DomainType.DAIRY));
        checkOnlyOneStats(resultList.size());

        MonthlyDairyStats result = resultList.get(0);
        if (isNotEnoughForStats(result)) {
            return result;
        }

        Integer condCnt = getSelectedConditionsCountInMonth(memberId, year, month);
        if (isNoResult(condCnt)) {
            return result;
        }

        List<ConditionRatio> mostCondList = getConditionRatioInMonth(memberId, year, month, 1, condCnt);
        checkOnlyOneStats(mostCondList.size());

        ConditionRatio mostCond = mostCondList.get(0);
        result.setMostCondId(mostCond.getCondId());
        result.setMostCondRatio(mostCond.getCondRatio());
        return result;
    }

    @Override
    public MonthlyContentStats getMonthlyContentStats(Long memberId, ContentType type, int year, int month) throws DuplicatedResultException, NotFoundDetailInfoException, ErrorToSearchStatsException {
        Integer count = getWrittenContentsCountInMonth(memberId, type, year, month);
        MonthlyContentStats result = new MonthlyContentStats(count);
        if (isNoResult(count)) {
            return result;
        }

        List<DateContents> highList = dairyRepository.findOneContentByHighestOrLowestScoreInMonth(memberId, type, year, month, SortType.HIGH)
                .orElseThrow(() -> new NotFoundDetailInfoException(type, SortType.HIGH));
        checkOnlyOneStats(highList.size(), SortType.HIGH);
        DateContents high = highList.get(0);

        List<DateContents> lowList = dairyRepository.findOneContentByHighestOrLowestScoreInMonth(memberId, type, year, month, SortType.LOW)
                .orElseThrow(() -> new NotFoundDetailInfoException(type, SortType.LOW));
        checkOnlyOneStats(lowList.size(), SortType.LOW);
        DateContents low = lowList.get(0);

        result.setHighest(high);
        result.setLowest(low);
        return result;
    }

    @Override
    public WeeklyDairyStats getWeeklyDairyStats(Long memberId, int year, int week) throws DuplicatedResultException, NotFoundDetailInfoException, ErrorToSearchStatsException {
        List<WeeklyDairyStats> resultList = dairyRepository.findDairyStatsInWeek(memberId, year, week)
                .orElseThrow(() -> new ErrorToSearchStatsException(DomainType.DAIRY));
        checkOnlyOneStats(resultList.size());

        WeeklyDairyStats result = resultList.get(0);
        if (isNoResult(result.getCount())) {
            return result;
        }

        List<DateScore> scoreList = dairyRepository.findDiaryScoreInWeek(memberId, year, week)
                .orElseThrow(() -> new NotFoundDetailInfoException(DomainType.DAIRY));
        checkSameResultCount(result.getCount(), scoreList.size(), DomainType.DAIRY);
        result.setScoreList(scoreList);
        return result;
    }

    @Override
    public MonthlyConditionsStats getMonthlyConditionStats(Long memberId, int year, int month) throws ErrorToSearchStatsException, NotFoundDetailInfoException {
        Integer count = getSelectedConditionsCountInMonth(memberId, year, month);
        MonthlyConditionsStats result = new MonthlyConditionsStats(count);
        if (isNoResult(count)) {
            return result;
        }

        List<ConditionRatio> condRatioList = getConditionRatioInMonth(memberId, year, month, 3, count);
        checkSameResultCount(count, condRatioList.size(), DomainType.DAIRY_CONDITION);
        result.setCondiList(condRatioList);
        return result;
    }

    @Override
    public MonthlyContentDetail getMonthlyContentDetail(Long memberId, ContentType type, int year, int month, SortType orderBy) throws ErrorToSearchStatsException, NotFoundDetailInfoException {
        Integer count = getWrittenContentsCountInMonth(memberId, type, year, month);
        MonthlyContentDetail result = new MonthlyContentDetail(count);
        if (isNoResult(count)) {
            return result;
        }

        List<DateContents> contList = dairyRepository.findSortedContentDetailInMonth(memberId, type, year, month, orderBy)
                .orElseThrow(() -> new NotFoundDetailInfoException(type));
        checkSameResultCount(count, contList.size(), type);
        result.setContentList(contList);
        return result;
    }

    private static boolean isNotEnoughForStats(MonthlyDairyStats result) {
        return result.getCount() < 3;
    }

    private static boolean isNoResult(Integer count) {
        return count < 1;
    }

    private Integer getWrittenContentsCountInMonth(Long memberId, ContentType type, int year, int month) throws ErrorToSearchStatsException {
        return dairyRepository.getMonthlyContentsCount(memberId, type, year, month)
                .orElseThrow(() -> new ErrorToSearchStatsException(type));
    }

    private Integer getSelectedConditionsCountInMonth(Long memberId, int year, int month) throws ErrorToSearchStatsException {
        return dairyConditionRepository.getSelectedConditionsCountInMonth(memberId, year, month)
                .orElseThrow(() -> new ErrorToSearchStatsException(DomainType.DAIRY_CONDITION));
    }

    private List<ConditionRatio> getConditionRatioInMonth(Long memberId, int year, int month, int limit, int condCnt) throws NotFoundDetailInfoException {
        return dairyConditionRepository.findConditionRatioInMonth(memberId, year, month, limit, condCnt)
                .orElseThrow(() -> new NotFoundDetailInfoException(DomainType.DAIRY_CONDITION));
    }
}
