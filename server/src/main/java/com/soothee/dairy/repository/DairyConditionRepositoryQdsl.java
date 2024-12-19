package com.soothee.dairy.repository;

import com.soothee.common.requestParam.MonthParam;
import com.soothee.stats.dto.ConditionRatio;
import com.soothee.stats.dto.MonthlyConditionsDTO;

import java.util.List;
import java.util.Optional;

public interface DairyConditionRepositoryQdsl {
    /**
     * 현재 로그인한 계정이 지정한 년도/달에 가장 많이 선택한 컨디션 상위 limit 개 리스트</hr>
     * [컨디션 선택 비율이 같은 경우]</br>
     * (1) 먼저 선택한 순</br>
     * (2) 긍정 > 보통 > 부정 카테고리 순</br>
     * (3) 카테고리별 먼저 등록된 순
     *
     * @param memberId      Long : 로그인한 계정 일련번호
     * @param monthParam    MonthParam : 지정한 년도/달
     * @param limit         Integer : 찾을 컨디션 개수
     * @param count         Double : 지정한 년도/달에 선택한 컨디션의 총 개수
     * @return Optional<List<ConditionRatio>> : 지정한 달에 가장 많이 선택된 최대 3개의 컨디션-비율 리스트 (null 가능)
     */
    Optional<List<ConditionRatio>> findConditionRatioListInMonth(Long memberId, MonthParam monthParam, Integer limit, Double count);

    /**
     * 현재 로그인한 계정이 지정한 년도/달에 선택한 컨디션의 총 개수</hr>
     *
     * @param memberId      Long : 로그인한 계정 일련번호
     * @param monthParam    MonthParam : 지정한 년도/달
     * @return Optional<MonthlyConditionsDTO> : 지정한 년도/달에 선택한 컨디션의 총 개수 (null 가능)
     */
    Optional<MonthlyConditionsDTO> getAllDairyConditionCntInMonth(Long memberId, MonthParam monthParam);
}
