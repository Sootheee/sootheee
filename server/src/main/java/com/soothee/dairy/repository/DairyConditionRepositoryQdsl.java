package com.soothee.dairy.repository;

import java.util.Optional;

public interface DairyConditionRepositoryQdsl {
    /**
     * 현재 로그인한 계정이 지정한 달에 가장 많이 선택한 컨디션 일련번호</hr>
     * 현재 로그인한 계정이 지정한 달에 작성한 모든 일기의 선택된 컨디션 중에서 가장 많이 선택된 컨디션 1개만 조회
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param year     Integer : 지정한 년도
     * @param month    Integer : 지정한 달
     * @return Long : 지정한 달에 선택된 모든 일기-컨디션 리스트
     */
    Optional<Long> findMostOneCondIdInMonth(Long memberId, Integer year, Integer month);
}
