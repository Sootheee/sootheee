package com.soothee.stats.service;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.custom.exception.*;
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
    public MonthlyStatsDTO getMonthlyStatsInfo(Long memberId, MonthParam monthParam) throws NotExistDairyException, DuplicatedResultException, NoStatsResultException {
        List<MonthlyStatsDTO> resultList = dairyRepository.findDiaryStatsInMonth(memberId, monthParam)
                .orElseThrow(() -> new NotExistDairyException(memberId, monthParam));
        if (resultList.size() > 1) {
            throw new DuplicatedResultException(memberId, monthParam);
        }
        MonthlyStatsDTO result = resultList.get(0);
        Integer condCnt = dairyConditionRepository.getAllDairyConditionCntInMonth(memberId, monthParam);
        if (condCnt > 1) {
            ConditionRatio mostCond = dairyConditionRepository.findConditionRatioListInMonth(memberId, monthParam, 1, condCnt)
                    .orElseThrow(() -> new NoStatsResultException(memberId, monthParam, condCnt)).get(0);
            result.setMostCondId(mostCond.getCondId());
            result.setMostCondRatio(mostCond.getCondRatio());
        }
        return result;
    }

    @Override
    public MonthlyContentsDTO getMonthlyContents(Long memberId, ContentType type, MonthParam monthParam) throws DuplicatedResultException, NoContentsException, IncorrectValueException, NullValueException {
        Integer count = dairyRepository.findDiaryContentCntInMonth(memberId, type, monthParam);
        if (count < 1) {
            throw new NoContentsException(memberId, type, monthParam);
        }
        List<DateContents> highList = dairyRepository.findDiaryContentInMonthHL(memberId, type, monthParam, SortType.HIGH)
                .orElseThrow(() -> new NoContentsException(memberId, type, monthParam, SortType.HIGH));
        if (highList.size() > 1) {
            throw new DuplicatedResultException(memberId, type, monthParam, SortType.HIGH);
        }
        List<DateContents> lowList = dairyRepository.findDiaryContentInMonthHL(memberId, type, monthParam, SortType.LOW)
                .orElseThrow(() -> new NoContentsException(memberId, type, monthParam, SortType.LOW));
        if (lowList.size() > 1) {
            throw new DuplicatedResultException(memberId, type, monthParam, SortType.LOW);
        }
        return MonthlyContentsDTO.builder()
                .count(count)
                .highest(highList.get(0))
                .lowest(lowList.get(0))
                .build();
    }

    @Override
    public WeeklyStatsDTO getWeeklyStatsInfo(Long memberId, WeekParam weekParam) throws NoStatsResultException, DuplicatedResultException {
        List<WeeklyStatsDTO> resultList = dairyRepository.findDiaryStatsInWeekly(memberId, weekParam)
                .orElseThrow(() -> new NoStatsResultException(memberId, weekParam));
        if (resultList.size() > 1) {
            throw new DuplicatedResultException(memberId, weekParam);
        }
        WeeklyStatsDTO result = resultList.get(0);
        if (result.getCount() > 0) {
            result.setScoreList(dairyRepository.findDiaryScoresInWeekly(memberId, weekParam)
                    .orElseThrow(() -> new NoStatsResultException(memberId, weekParam, result.getCount())));
        }
        return result;
    }

    @Override
    public MonthlyConditionsDTO getMonthlyConditionList(Long memberId, MonthParam monthParam) throws IncorrectValueException, NullValueException, NoStatsResultException {
        dairyRepository.findDiaryStatsInMonth(memberId, monthParam).orElseThrow(
                () -> new NoStatsResultException(memberId, monthParam)
        );
        Integer count = dairyConditionRepository.getAllDairyConditionCntInMonth(memberId, monthParam);
        if (count < 1) {
            throw new NoStatsResultException(memberId, monthParam, count);
        }
        MonthlyConditionsDTO result = new MonthlyConditionsDTO(count);
        List<ConditionRatio> condRatioList = dairyConditionRepository.findConditionRatioListInMonth(memberId, monthParam, 3, count).orElseThrow(
                () -> new NoStatsResultException(memberId, monthParam, count)
        );
        result.setCondiList(condRatioList);
        return result;
    }

    @Override
    public MonthlyAllContentsDTO getAllContentsInMonth(Long memberId, ContentType type, MonthParam monthParam, SortType orderBy) throws NoContentsException, IncorrectValueException, NullValueException {
        Integer count = dairyRepository.findDiaryContentCntInMonth(memberId, type, monthParam);
        if (count < 1) {
            throw new NoContentsException(memberId, type, monthParam);
        }
        List<DateContents> result = dairyRepository.findDiaryContentInMonthSort(memberId, type, monthParam, orderBy)
                .orElseThrow(() -> new NoContentsException(memberId, type, monthParam, orderBy));
        return MonthlyAllContentsDTO.builder()
                .count(count)
                .contentList(result)
                .build();
    }
}
