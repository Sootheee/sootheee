package com.soothee.dairy.service;

import com.soothee.dairy.domain.Dairy;

import java.util.List;

public interface DairyConditionService {
    /**
     * 해당 일기에서 선택한 컨디션들 저장</hr>
     *
     * @param condIdList List<Long> : 선택한 컨디션들의 일련번호 리스트
     * @param newDairy   Dairy : 해당 일기
     */
    void saveConditions(List<Long> condIdList, Dairy newDairy);

    /**
     * 해당 일기의 일기-컨디션 리스트 조회</hr>
     * 삭제한 일기-컨디션 제외
     *
     * @param dairyId Long : 조회할 일기 일련번호
     * @return List<Long> : 해당 일기의 다수의 컨디션 일련번호 리스트
     */
    List<Long> getConditionsIdListByDairy(Long dairyId);

    /**
     * 해당 일기의 일기-컨디션 리스트 업데이트</hr>
     *
     * @param curDairy     Dairy : 조회할 일기 일련번호
     * @param inputCondIds List<Long> : 업데이트될 컨디션 리스트
     */
    void updateConditions(Dairy curDairy, List<Long> inputCondIds);

    /**
     * 해당 일기의 일기-컨디션 리스트 모두 소프트삭제</hr>
     *
     * @param dairy Dairy : 삭제할 일기 일련번호
     */
    void deleteDairyConditionsOfDairy(Dairy dairy);

    /**
     * 로그인한 계정이 한 달간 작성한 일기에 가장 많이 선택한 컨디션 조회</hr>
     * [컨디션 선택 횟수가 같은 경우]
     * (1) 먼저 선택한 순
     * (2) 긍정 > 보통 > 부정 카테고리 순
     * (3) 카테고리별 먼저 등록된 순
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param year     Integer : 지정한 년도
     * @param month    Integer : 지정한 달
     * @return Long : 가장 많이 선택한 컨디션 일련번호
     */
    Long getMostOneCondIdInMonth(Long memberId, Integer year, Integer month);
}
