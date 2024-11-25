package com.soothee.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
public class UpdateMemberDTO {
    @NotEmpty(message = "회원 일련번호가 없습니다.")
    private Long id;
    @NotEmpty(message = "회원 닉네임이 없습니다.")
    private String memberName;
}
