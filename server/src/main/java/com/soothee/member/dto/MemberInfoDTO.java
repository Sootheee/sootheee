package com.soothee.member.dto;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "로그인한 회원의 모든 정보 DTO")
public class MemberInfoDTO implements MemberDTO {
    @NotEmpty(message = "회원 일련번호가 없습니다.")
    @Schema(description = "회원 일련번호")
    private Long memberId;

    @NotEmpty(message = "회원 아이디가 없습니다.")
    @Schema(description = "회원 아이디")
    private String email;

    @NotEmpty(message = "회원 닉네임 정보가 없습니다.")
    @Schema(description = "회원 닉네임")
    private String name;

    @NotEmpty(message = "회원 다크모드 정보가 없습니다.")
    @Schema(description = "회원 다크모드 || Y : dark mode / N : normal mode")
    private String isDark;

    public static MemberInfoDTO fromMember(Member info) {
        if (Objects.isNull(info.getMemberId())
                || Strings.isBlank(info.getEmail())
                || Strings.isBlank(info.getName())) {
            throw new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.NULL_VALUE);
        }
        return MemberInfoDTO.builder()
                .memberId(info.getMemberId())
                .email(info.getEmail())
                .isDark(info.getIsDark())
                .build();
    }
}
