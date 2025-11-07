package com.soothee.dairy.repository;

import com.soothee.dairy.domain.DairyCondition;
import com.soothee.stats.controller.response.ConditionRatio;

import java.util.List;
import java.util.Optional;

public interface DairyConditionRepositoryQdsl {
    /**
     * 일기 일련번호로 일기의 컨디션 리스트 조회
     * 일기 컨디션의 선택 순서대로 조회됨
     * - 삭제한 일기 제외
     * - 삭제한 일기-컨디션 제외
     *
     * @param dairyId 조회할 일기 일련번호
     * @return 조회된 일기의 일기-컨디션 리스트 선택한 순서대로 (null 가능)
     */
    Optional<List<DairyCondition>> findDairyConditionListByDairyId(Long dairyId);

    /**
     * 일기 일련번호의 일기에서 선택된 컨디션 일련번호 리스트 조회
     * 일기 컨디션의 선택 순서대로 조회됨
     * - 삭제한 일기 제외
     * - 삭제한 일기-컨디션 제외
     *
     * @param dairyId 조회할 일기 일련번호
     * @return 조회된 일기의 일기-컨디션 일련번호 리스트 선택한 순서대로 (null 가능)
     */
    Optional<List<String>> findConditionIdListByDairyId(Long dairyId);

    /**
     * 현재 로그인한 계정이 조회할 년도/달에 가장 많이 선택한 컨디션 상위 limit 개 리스트
     * [컨디션 선택 비율이 같은 경우]
     * (1) 먼저 선택한 순
     * (2) 긍정 > 보통 > 부정 카테고리 순
     * (3) 카테고리별 먼저 등록된 순
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param year 조회할 년도
     * @param month 조회할 월
     * @param limit 찾을 컨디션 갯수
     * @param count 조회할 년도/달에 선택한 컨디션의 총 갯수
     * @return 조회할 달에 가장 많이 선택된 최대 3개의 컨디션-비율 리스트 (null 가능)
     */
    Optional<List<ConditionRatio>> findConditionRatioInMonth(Long memberId, Integer year, Integer month, Integer limit, Integer count);

    /**
     * 현재 로그인한 계정이 조회할 년도/달에 선택한 컨디션의 총 갯수
     * - 삭제한 일기 제외
     *
     * @param memberId   로그인한 계정 일련번호
     * @param year 조회할 년도
     * @param month 조회할 월
     * @return 조회할 년도/달에 선택한 컨디션의 총 갯수 (null 가능)
     */
    Optional<Integer> getSelectedConditionsCountInMonth(Long memberId, Integer year, Integer month);
}
