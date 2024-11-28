package com.soothee.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "닉네임 조회 DTO")
public class NameMemberInfoDTO implements MemberDTO {
    @Schema(description = "회원 일련번호")
    @NotEmpty(message = "회원 일련번호가 없습니다.")
    private Long memberId;

    @Schema(description = "회원 닉네임")
    @NotEmpty(message = "회원 닉네임이 없습니다.")
    private String name;
}
