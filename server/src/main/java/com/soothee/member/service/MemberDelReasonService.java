package com.soothee.member.service;

import com.soothee.member.domain.Member;

import java.util.List;

public interface MemberDelReasonService {
    /**
     * 로그인한 회원의 탈퇴 사유 저장</hr>
     *
     * @param loginMember 현재 로그인한 계정 정보
     * @param delReasonList 탈퇴 사유 일련번호 리스트
     */
    void saveDeleteReasons(Member loginMember, List<Long> delReasonList);
}
