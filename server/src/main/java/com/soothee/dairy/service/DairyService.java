package com.soothee.dairy.service;

import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.oauth2.domain.AuthenticatedUser;

import java.time.LocalDate;
import java.util.List;

public interface DairyService {
    /**
     * 지정한 달에 작성한 모든 일기의 오늘의 점수 리스트 조회</hr>
     * 삭제된 일기 제외
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param year      Integer : 지정한 년도
     * @param month     Integer : 지정한 달
     * @return List<DairyScoresDTO> : 일기 일련번호와 날짜, 오늘의 점수 리스트
     */
    List<DairyScoresDTO> getAllDairyMonthly(AuthenticatedUser loginInfo, Integer year, Integer month);

    /**
     * 해당 날짜 일기 조회</hr>
     * 삭제된 일기 제외
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param date      LocalDate : 조회할 날짜
     * @return DairyDTO : 조회한 일기 모든 정보
     */
    DairyDTO getDairyByDate(AuthenticatedUser loginInfo, LocalDate date);

    /**
     * 해당 일기 일련번호 일기 조회</hr>
     * 삭제된 일기 제외
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param dairyId   Long : 조회할 일기 일련번호
     * @return DairyDTO : 조회한 일기 모든 정보
     */
    DairyDTO getDairyByDairyId(AuthenticatedUser loginInfo, Long dairyId);

    /**
     * 새로운 일기 등록</hr>
     * 해당 일자에 이미 등록된 일기가 있으면 등록 불가
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param inputInfo DairyRegisterDTO : 등록할 일기 정보
     */
    void registerDairy(AuthenticatedUser loginInfo, DairyRegisterDTO inputInfo);

    /**
     * 기존 일기 수정</hr>
     * 1. path의 일련번호와 query의 일련번호가 다르면 수정 불가
     * 2. 기존 dairy의 date와 query의 date가 다르면 수정 불가
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param dairyId Long : 수정할 일기 일련번호
     * @param inputInfo
     */
    void modifyDairy(AuthenticatedUser loginInfo, Long dairyId, DairyDTO inputInfo);
}
