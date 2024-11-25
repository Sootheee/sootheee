package com.soothee.member.dto;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
public class UpdateMemberDTO {
    private Long id;
    private String memberName;
}
