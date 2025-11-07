package com.soothee.member.controller.response;

import com.soothee.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 회원 닉네임 조회
 * 1. 회원 일련번호 2. 회원 닉네임
 */
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "닉네임 조회 DTO")
public class MemberNameResponse implements MemberResponse {
    @Schema(description = "회원 일련번호")
    private Long memberId;

    @Schema(description = "회원 닉네임")
    private String name;

    public static MemberNameResponse fromMember(Member member) {
        return MemberNameResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .build();
    }
}
