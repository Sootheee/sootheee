package com.soothee.dairy.domain;

import com.querydsl.core.annotations.QueryProjection;
import com.soothee.common.constants.*;
import com.soothee.common.domain.TimeEntity;
import com.soothee.dairy.service.command.DairyModify;
import com.soothee.member.domain.Member;
import com.soothee.reference.domain.Weather;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
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

    /** 오늘의 요약 */
    @Column(name = "content", length = 600)
    private String content;

    /** 바랐던 방향성 */
    @Column(name = "hope", length = 200)
    private String hope;

    /** 감사한 일 */
    @Column(name = "thank", length = 200)
    private String thank;

    /** 배운 일 */
    @Column(name = "learn", length = 200)
    private String learn;

    /** 소프트 삭제 */
    @Enumerated(EnumType.STRING)
    @Column(name = "is_delete", nullable = false, length = 1)
    private BooleanYN isDelete;

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
        this.isDelete = BooleanYN.N;
    }

    /**
     * 일기 수정
     *
     * @param dairy 입력된 수정할 일기 정보
     * @param weather 해당 일기 날씨 정보
     */
    public void updateDairy(DairyModify dairy, Weather weather) {
        this.weather = weather;
        this.score = dairy.getScore();
        this.content = dairy.getContent();
        this.hope = dairy.getHope();
        this.thank = dairy.getThank();
        this.learn = dairy.getLearn();
    }

    /** 일기 삭제 */
    public void deleteDairy() {
        this.isDelete = BooleanYN.Y;
    }
}