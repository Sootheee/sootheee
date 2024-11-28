package com.soothee.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "로그인한 회원의 모든 정보 DTO")
public class AllMemberInfoDTO implements MemberDTO {
    @NotEmpty(message = "회원 일련번호가 없습니다.")
    @Schema(description = "회원 일련번호")
    private Long id;

    @NotEmpty(message = "회원 아이디가 없습니다.")
    @Schema(description = "회원 아이디")
    private String email;

    @NotEmpty(message = "회원 닉네임 정보가 없습니다.")
    @Schema(description = "회원 닉네임")
    private String name;

    @NotEmpty(message = "회원 다크모드 정보가 없습니다.")
    @Schema(description = "회원 다크모드 || Y : dark mode / N : normal mode")
    private String isDark;
}
