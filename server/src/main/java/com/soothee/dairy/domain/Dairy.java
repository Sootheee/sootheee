package com.soothee.dairy.domain;

import com.soothee.common.domain.TimeEntity;
import com.soothee.member.domain.Member;
import com.soothee.reference.domain.Weather;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
     * @param dairy Dairy : 해당 다이어리
     */
    public void updateDairy(Dairy dairy) {
        this.weather = dairy.getWeather();
        this.score = dairy.getScore();
        this.content = dairy.getContent();
        this.hope = dairy.getHope();
        this.thank = dairy.getThank();
        this.learn = dairy.getLearn();
    }

    /**
     * 일기 삭제</hr>
     * */
    public void deleteDairy() {
        this.isDelete = "Y";
    }
}