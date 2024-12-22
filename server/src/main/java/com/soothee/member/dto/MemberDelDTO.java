package com.soothee.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

/**
 * 회원 탈퇴
 * 1. 회원 일련번호 2. 선택한 탈퇴 사유 일련번호 리스트
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "회원 탈퇴 DTO")
public class MemberDelDTO {
    @NotEmpty(message = "회원 일련번호가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "회원 일련번호")
    private Long memberId;

    @NotEmpty(message = "회원 탈퇴 사유가 없습니다.")
    @Schema(description = "선택한 회원 탈퇴 사유들")
    private List<@Positive(message = "일련번호는 양수만 입력 가능합니다.")Long> delReasonList;
}
