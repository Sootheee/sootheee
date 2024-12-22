package com.soothee.member.dto;

import com.soothee.common.constants.BooleanType;
import com.soothee.common.constants.DomainType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * 회원
 * 1. 회원 일련번호 2. 회원 아이디 3. 회원 닉네임 4. 회원 다크모드 정보
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "로그인한 회원의 모든 정보 DTO")
public class MemberInfoDTO implements MemberDTO {
    @NotEmpty(message = "회원 일련번호가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "회원 일련번호")
    private Long memberId;

    @NotBlank(message = "회원 아이디가 없습니다.")
    @Schema(description = "회원 아이디")
    @Pattern(regexp = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "회원 아이디 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "회원 닉네임 정보가 없습니다.")
    @Size(min = 2, max = 6, message = "회원 닉네임은 최대 6자까지 입력 가능합니다.")
    @Schema(description = "회원 닉네임")
    private String name;

    @NotBlank(message = "회원 다크모드 정보가 없습니다.")
    @Size(max = 1, message = "다크모드는 1자로 입력합니다.")
    @Pattern(regexp = "^[YN]$")
    @Schema(description = "회원 다크모드 || Y : dark mode / N : normal mode")
    private String isDark;

    public static MemberInfoDTO fromMember(Member info) throws IncorrectValueException, NullValueException {
        checkFromMember(info);
        return MemberInfoDTO.builder()
                .memberId(info.getMemberId())
                .email(info.getEmail())
                .isDark(info.getIsDark())
                .build();
    }

    private static void checkFromMember(Member info) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomainId(info.getMemberId(), DomainType.MEMBER);
        SootheeValidation.checkEmail(info.getEmail());
        SootheeValidation.checkBoolean(info.getIsDark(), BooleanType.DARK_MODE);
    }
}
