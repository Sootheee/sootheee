package com.soothee.dairy.service.command;

import com.soothee.dairy.controller.request.DairyRegisterRequest;
import com.soothee.dairy.domain.Dairy;
import com.soothee.member.domain.Member;
import com.soothee.reference.domain.Weather;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DairyRegister implements DairyInputInfo {
    /** 일기 작성 회원 */
    private Long memberId;

    /** 일기 작성 날짜 */
    private LocalDate date;

    /** 오늘의 날씨 */
    private String weatherId;

    /** 오늘의 점수 */
    private Double score;

    /** 선택한 컨디션 리스트 */
    private List<String> condIdList;

    /** 오늘 요약 */
    private String content;

    /** 바랐던 방향성 */
    private String hope;

    /** 감사한 일 */
    private String thank;

    /** 배운 일 */
    private String learn;

    /**
     * 일기 생성
     *
     * @return Dairy entity
     */
    public Dairy toDairy(Member member, Weather weather) {
        return Dairy.builder()
                .date(date)
                .member(member)
                .weather(weather)
                .score(score)
                .content(content)
                .hope(hope)
                .thank(thank)
                .learn(learn)
                .build();
    }

    public static DairyRegister fromParam(Long memberId, DairyRegisterRequest param) {
        return DairyRegister.builder()
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
