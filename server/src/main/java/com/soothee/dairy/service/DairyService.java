package com.soothee.dairy.service;

import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.oauth2.domain.AuthenticatedUser;

import java.util.List;

public interface DairyService {
    /**
     * 지정한 달에 작성한 모든 일기의 오늘의 점수 리스트 조회</hr>
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param year      Integer : 지정한 달
     * @param month     Integer : 지정한 달
     * @return List<DairyScoresDTO> : 일기 일련번호와 각 오늘의 점수 리스트
     */
    List<DairyScoresDTO> getAllDairyMonthly(AuthenticatedUser loginInfo, Integer year, Integer month);
}
