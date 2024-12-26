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
    List<DairyScoresDTO> getAllDairyMonthly(Long memberId, MonthParam monthParam) throws IncorrectValueException, NullValueException;

    /**
     * 현재 로그인한 계정이 지정한 날짜에 작성한 고유한 하나의 일기 조회
     * - 삭제된 일기 제외
     * 1. 지정한 날짜에 작성된 일기가 없는 경우 Exception 발생
     * 2. 같은 작성 일자의 일기가 1개 초과 중복 등록된 경우 Exception 발생
     * 3. 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param date 조회할 날짜
     * @return 조회한 일기 모든 정보
     */
    DairyDTO getDairyByDate(Long memberId, LocalDate date) throws NotExistDairyException, DuplicatedResultException, NotFoundDetailInfoException, IncorrectValueException, NullValueException;

    /**
     * 현재 로그인한 계정이 작성한 지정한 일기 일련번호를 가진 고유한 하나의 일기 조회
     * - 삭제된 일기 제외
     * 1. 지정한 일기 일련번호를 가진 일기가 없는 경우 Exception 발생
     * 2. 같은 일기 일련번호의 일기가 1개 초과 중복 등록된 경우 Exception 발생
     * 3. 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param dairyId 조회할 일기 일련번호
     * @return 조회한 일기 모든 정보
     */
    DairyDTO getDairyByDairyId(Long memberId, Long dairyId) throws NotExistDairyException, DuplicatedResultException, NotFoundDetailInfoException, IncorrectValueException, NullValueException;

    /**
     * 새로운 일기 등록
     * - 삭제된 일기 제외
     * 1. 입력한 새 일기의 작성 날짜에 이미 등록된 일기가 있는 경우 Exception 발생
     * 2. 해당 회원 일련번호로 조회된 회원 정보가 없는 경우 Exception 발생
     * 3. 해당 날씨 일련번호로 조회된 날씨가 없는 경우
     * 4. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param inputInfo 등록할 일기 정보
     */
    void registerDairy(Long memberId, DairyRegisterDTO inputInfo) throws DuplicatedResultException, NullValueException, IncorrectValueException, NotExistMemberException;

    /**
     * 기존 일기 수정
     * - 삭제된 일기 제외
     * 1. 지정한 일기 일련번호를 가진 일기가 없는 경우
     * 2. 기존 일기 일련번호와 입력한 일기 일련번호가 다른 경우
     * 3. 기존 일기 작성 날짜와 입력한 일기 작성 날짜가 다른 경우
     * 4. 기존 일기 작성 회원 일련번호와 현재 로그인한 계정의 회원 일련번호가 다른 경우
     * 5. 해당 날씨 일련번호로 조회된 날씨가 없는 경우
     * 6. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param dairyId 수정할 일기 일련번호
     * @param inputInfo 수정할 일기 정보
     */
    void modifyDairy(Long memberId, Long dairyId, DairyDTO inputInfo) throws NotExistDairyException, NotMatchedException, NullValueException, IncorrectValueException, NotFoundDetailInfoException;

    /**
     * 작성된 일기 삭제
     * - 삭제된 일기 제외
     * 1. 지정한 일기 일련번호를 가진 일기가 없는 경우
     * 2. 기존 일기 작성 회원 일련번호와 현재 로그인한 계정의 회원 일련번호가 다른 경우
     * 3. 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param dairyId 삭제할 일기 일련번호
     */
    void deleteDairy(Long memberId, Long dairyId) throws NotExistDairyException, NotMatchedException, NotFoundDetailInfoException, IncorrectValueException, NullValueException;
}
