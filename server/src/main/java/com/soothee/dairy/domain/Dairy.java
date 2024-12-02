package com.soothee.dairy.domain;

import com.soothee.common.domain.TimeEntity;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.member.domain.Member;
import com.soothee.reference.domain.Weather;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dairy")
public class Dairy extends TimeEntity {
    /** 일기 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dairyId;

    /** 일기 날짜 */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /** 등록 회원 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /** 오늘의 날씨 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "weather_id", nullable = false)
    private Weather weather;

    /** 오늘의 점수 */
    @Column(name = "score", nullable = false)
    private Double score;

    /** 하루 요약 */
    @Column(name = "content", length = 600)
    private String content;

    /** 바랐던 방향성 */
    @Column(name = "hope")
    private String hope;

    /** 감사한 일 */
    @Column(name = "thank")
    private String thank;

    /** 배운 점 */
    @Column(name = "learn")
    private String learn;

    /** 소프트 삭제 */
    @Column(name = "is_delete")
    private String isDelete;

    @Builder
    public Dairy(Member member, LocalDate date, Weather weather, Double score, String content, String hope, String thank, String learn) {
        this.member = member;
        this.date = date;
        this.weather = weather;
        this.score = score;
        this.content = content;
        this.hope = hope;
        this.thank = thank;
        this.learn = learn;
        this.isDelete = "N";
    }

    /**
     * 일기 수정</hr>
     *
     * @param dairy DairyDTO : 입력된 수정할 일기 정보
     * @param weather Weather : 해당 일기 날씨 정보
     */
    public void updateDairy(DairyDTO dairy, Weather weather) {
        if (!Objects.equals(dairy.getWeatherId(), weather.getWeatherId())) {
            this.weather = weather;
        }
        if (!Objects.equals(this.score, dairy.getScore())) {
            this.score = dairy.getScore();
        }
        if (!StringUtils.equals(this.content, dairy.getContent())) {
            this.content = dairy.getContent();
        }
        if (!StringUtils.equals(this.hope, dairy.getHope())) {
            this.hope = dairy.getHope();
        }
        if (!StringUtils.equals(this.thank, dairy.getThank())) {
            this.thank = dairy.getThank();
        }
        if (!StringUtils.equals(this.learn, dairy.getLearn())) {
            this.learn = dairy.getLearn();
        }
    }

    /** 일기 삭제 */
    public void deleteDairy() {
        this.isDelete = "Y";
    }

    /**
     * 다이어리 생성</hr>
     *
     * @param inputInfo DairyRegisterDTO : 입력된 등록할 일기 정보
     * @param member Member : 로그인한 계정 정보
     * @param weather Weather : 해당 일기 날씨 정보
     * @return Dairy
     */
    public static Dairy of(DairyRegisterDTO inputInfo, Member member, Weather weather) {
        return Dairy.builder()
                .date(inputInfo.getDate())
                .member(member)
                .weather(weather)
                .score(inputInfo.getScore())
                .content(inputInfo.getContent())
                .hope(inputInfo.getHope())
                .thank(inputInfo.getThank())
                .learn(inputInfo.getLearn())
                .build();
    }
}