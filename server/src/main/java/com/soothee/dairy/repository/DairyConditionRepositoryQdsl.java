package com.soothee.dairy.repository;

import com.soothee.common.requestParam.MonthParam;
import com.soothee.stats.dto.ConditionRatio;

import java.util.List;
import java.util.Optional;

public interface DairyConditionRepositoryQdsl {
    /**
     * 현재 로그인한 계정이 지정한 년도/달에 가장 많이 선택한 컨디션 상위 limit 개 리스트
     * [컨디션 선택 비율이 같은 경우]
     * (1) 먼저 선택한 순
     * (2) 긍정 > 보통 > 부정 카테고리 순
     * (3) 카테고리별 먼저 등록된 순
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param monthParam 지정한 년도/달
     * @param limit 찾을 컨디션 갯수
     * @param count 지정한 년도/달에 선택한 컨디션의 총 갯수
     * @return 지정한 달에 가장 많이 선택된 최대 3개의 컨디션-비율 리스트 (null 가능)
     */
    Optional<List<ConditionRatio>> findConditionRatioListInMonth(Long memberId, MonthParam monthParam, Integer limit, Integer count)  ;

    /**
     * 현재 로그인한 계정이 지정한 년도/달에 선택한 컨디션의 총 갯수
     * - 삭제한 일기 제외
     *
     * @param memberId   로그인한 계정 일련번호
     * @param monthParam 지정한 년도/달
     * @return 지정한 년도/달에 선택한 컨디션의 총 갯수 (null 가능)
     */
    Optional<Integer> getAllDairyConditionCntInMonth(Long memberId, MonthParam monthParam)  ;
}
