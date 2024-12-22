package com.soothee.member.dto;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@AllArgsConstructor
@Builder
@Schema(description = "닉네임 조회 DTO")
public class MemberNameDTO implements MemberDTO {
    @Schema(description = "회원 일련번호")
    @NotEmpty(message = "회원 일련번호가 없습니다.")
    private Long memberId;

    @Schema(description = "회원 닉네임")
    @NotEmpty(message = "회원 닉네임이 없습니다.")
    private String name;

    public static MemberNameDTO fromMember(Member member) {
        if (Objects.isNull(member.getMemberId())
                || StringUtils.isBlank(member.getName())) {
            throw new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.NULL_VALUE);
        }
        return MemberNameDTO.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .build();
    }
}
