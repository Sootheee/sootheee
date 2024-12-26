package com.soothee.dairy.service;

import com.soothee.common.requestParam.MonthParam;
import com.soothee.custom.exception.*;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.dairy.dto.DairyScoresDTO;

import java.time.LocalDate;
import java.util.List;

public interface DairyService {
    /**
     * 현재 로그인한 계정이 지정된 년도/월에 작성한 모든 일기의 작성 날짜와 오늘의 점수 리스트 조회
     * - 삭제된 일기 제외
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param monthParam 지정한 년도/달
     * @return 일기 일련번호와 날짜, 오늘의 점수 리스트 || 지정된 년도/월에 작성한 일기가 없는 경우 빈 리스트 리턴
     */
    List<DairyScoresDTO> getAllDairyMonthly(Long memberId, MonthParam monthParam) throws NotExistDairyException;

    /**
     * 현재 로그인한 계정이 지정한 날짜에 작성한 고유한 하나의 일기 조회
     * - 삭제된 일기 제외
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param date 조회할 날짜
     * @return 조회한 일기 모든 정보
     */
    DairyDTO getDairyByDate(Long memberId, LocalDate date) throws NotExistDairyException, DuplicatedResultException, NoDairyConditionException;

    /**
     * 현재 로그인한 계정이 작성한 지정한 일기 일련번호를 가진 고유한 하나의 일기 조회
     * - 삭제된 일기 제외
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param dairyId 조회할 일기 일련번호
     * @return 조회한 일기 모든 정보
     */
    DairyDTO getDairyByDairyId(Long memberId, Long dairyId) throws NotExistDairyException, DuplicatedResultException, NoDairyConditionException;

    /**
     * 새로운 일기 등록
     * 해당 일자에 이미 등록된 일기가 있으면 등록 불가
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param inputInfo 등록할 일기 정보
     */
    void registerDairy(Long memberId, DairyRegisterDTO inputInfo) throws NotExistMemberException, NullValueException, IncorrectValueException, DuplicatedResultException;

    /**
     * 기존 일기 수정
     * - 삭제된 일기 제외
     * 2. 기존 dairy의 date와 query의 date가 다르면 수정 불가
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param dairyId 수정할 일기 일련번호
     * @param inputInfo 수정할 일기 정보
     */
    void modifyDairy(Long memberId, Long dairyId, DairyDTO inputInfo) throws NotExistDairyException, NullValueException, IncorrectValueException, NotMatchedException;

    /**
     * 작성된 일기 삭제
     * - 삭제된 일기 제외
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param dairyId 삭제할 일기 일련번호
     */
    void deleteDairy(Long memberId, Long dairyId) throws NotExistDairyException, NotMatchedException, NoDairyConditionException;
}
