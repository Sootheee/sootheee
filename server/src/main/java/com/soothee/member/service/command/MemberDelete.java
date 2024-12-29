package com.soothee.member.service.command;

import com.soothee.member.controller.request.MemberDeleteRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberDelete {
    /** 회원 일련번호 */
    private Long memberId;
    /** 탈퇴 사유 일련번호 리스트 */
    private List<String> delReasonIdList;

    public static MemberDelete fromParam(MemberDeleteRequest param) {
        return MemberDelete.builder()
                .memberId(param.getMemberId())
                .delReasonIdList(param.getDelReasonIdList())
                .build();
    }
}
