package com.soothee.member.controller.response;

import com.soothee.common.constants.BooleanYN;
import com.soothee.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 회원
 * 1. 회원 일련번호 2. 회원 아이디 3. 회원 닉네임 4. 회원 다크모드 정보
 */
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "로그인한 회원의 모든 정보 DTO")
public class MemberAllInfoResponse implements MemberResponse {
    @Schema(description = "회원 일련번호")
    private Long memberId;

    @Schema(description = "회원 아이디")
    private String email;

    @Schema(description = "회원 닉네임")
    private String name;

    @Schema(description = "회원 다크모드 || Y : dark mode / N : normal mode")
    private BooleanYN isDark;

    public static MemberAllInfoResponse fromMember(Member info) {
        return MemberAllInfoResponse.builder()
                .memberId(info.getMemberId())
                .email(info.getEmail())
                .name(info.getName())
                .isDark(info.getIsDark())
                .build();
    }
}
