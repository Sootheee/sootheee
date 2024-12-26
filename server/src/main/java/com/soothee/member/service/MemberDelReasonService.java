package com.soothee.member.service;

import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.member.domain.Member;
import com.soothee.member.domain.MemberDelReason;

import java.util.List;

public interface MemberDelReasonService {
    /**
     * 로그인한 회원의 탈퇴 사유 등록
     * 1. 탈퇴 사유 일련번호로 조회된 탈퇴 사유가 없는 경우 Exception 발생
     * 2. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     *
     * @param loginMember 현재 로그인한 계정 정보
     * @param delReasonIdList 탈퇴 사유 일련번호 리스트
     */
    void saveDeleteReasons(Member loginMember, List<String>  delReasonIdList) throws NullValueException, IncorrectValueException;

    /**
     * 회원의 일련번호로 탈퇴 사유 조회
     * 1. 회원 일련번호로 조회된 회원 탈퇴 사유가 없는 경우 Exception 발생
     *
     * @param memberId 조회할 회원의 일련번호
     * @return 탈퇴 사유 리스트
     */
    List<MemberDelReason> getMemberDelReasonByMemberId(Long memberId) throws NullValueException, IncorrectValueException;
}
