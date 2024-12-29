package com.soothee.member.controller.request;

import com.soothee.custom.valid.ExistReferenceId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

/**
 * 회원 탈퇴
 * 1. 회원 일련번호 2. 선택한 탈퇴 사유 일련번호 리스트
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "회원 탈퇴 요청 파라미터")
public class MemberDeleteRequest {
    @NotEmpty(message = "회원 일련번호가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "회원 일련번호")
    private Long memberId;

    @NotEmpty(message = "회원 탈퇴 사유가 없습니다.")
    @Schema(description = "선택한 회원 탈퇴 사유들")
    private List<@ExistReferenceId(min = 1, max = 5, message = "존재하는 탈퇴 사유 일련번호가 아닙니다.") String> delReasonIdList;
}
