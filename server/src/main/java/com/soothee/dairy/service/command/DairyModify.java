package com.soothee.dairy.service.command;

import com.soothee.dairy.controller.request.DairyModifyRequest;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DairyModify implements DairyInputInfo {
    /** 일기 일련번호 */
    private Long dairyId;

    /** 일기 작성 회원 일련번호 */
    private Long memberId;

    /** 일기 작성 날짜 */
    private LocalDate date;

    /** 오늘의 날씨 일련번호 */
    private String weatherId;

    /** 오늘의 점수 */
    private Double score;

    /** 선택한 컨디션 일련번호 리스트 */
    private List<String> condIdList;

    /** 오늘 요약 */
    private String content;

    /** 바랐던 방향성 */
    private String hope;

    /** 감사한 일 */
    private String thank;

    /** 배운 일 */
    private String learn;

    public static DairyModify fromParam(Long memberId, DairyModifyRequest param) {
        return DairyModify.builder()
                            .memberId(memberId)
                            .date(param.getDate())
                            .weatherId(param.getWeatherId())
                            .score(param.getScore())
                            .condIdList(param.getCondIdList())
                            .content(param.getContent())
                            .hope(param.getHope())
                            .thank(param.getThank())
                            .learn(param.getLearn())
                            .build();
    }
}
