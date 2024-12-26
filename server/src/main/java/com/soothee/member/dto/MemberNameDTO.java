package com.soothee.member.dto;

import com.soothee.common.constants.DomainType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 회원 닉네임 조회
 * 1. 회원 일련번호 2. 회원 닉네임
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "닉네임 조회 DTO")
public class MemberNameDTO implements MemberDTO {
    @Schema(description = "회원 일련번호")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @NotEmpty(message = "회원 일련번호가 없습니다.")
    private Long memberId;

    @Schema(description = "회원 닉네임")
    @Size(min = 2, max = 6, message = "회원 닉네임은 최대 6자까지 입력 가능합니다.")
    @NotBlank(message = "회원 닉네임이 없습니다.")
    private String name;

    public static MemberNameDTO fromMember(Member member) throws IncorrectValueException, NullValueException {
        checkConstructorMemberNameDTO(member);
        return MemberNameDTO.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .build();
    }

    /** validation */
    private static void checkConstructorMemberNameDTO(Member member) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomain(member, DomainType.MEMBER);
        SootheeValidation.checkName(member.getName());
    }
}
