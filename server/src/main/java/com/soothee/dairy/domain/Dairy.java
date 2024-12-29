package com.soothee.dairy.domain;

import com.soothee.common.constants.*;
import com.soothee.common.domain.Domain;
import com.soothee.common.domain.TimeEntity;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
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
public class Dairy extends TimeEntity implements Domain {
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
    public void updateDairy(DairyDTO dairy, Weather weather) throws IncorrectValueException, NullValueException {
        checkUpdateDairy(dairy, weather);
        if (!Objects.equals(dairy.getWeatherId(), weather.getWeatherId())) {
            this.weather = weather;
        }
        if (!Objects.equals(score, dairy.getScore())) {
            this.score = dairy.getScore();
        }
        if (!StringUtils.equals(content, dairy.getContent())) {
            this.content = dairy.getContent();
        }
        if (!StringUtils.equals(hope, dairy.getHope())) {
            this.hope = dairy.getHope();
        }
        if (!StringUtils.equals(thank, dairy.getThank())) {
            this.thank = dairy.getThank();
        }
        if (!StringUtils.equals(learn, dairy.getLearn())) {
            this.learn = dairy.getLearn();
        }
    }

    /** 일기 삭제 */
    public void deleteDairy() {
        this.isDelete = BooleanYN.Y;
    }

    @Override
    public Long getId() {
        return dairyId;
    }

    /** validation */
    private static void checkDiaryOfDiaryRegisterDTO(DairyRegisterDTO inputInfo, Member member, Weather weather) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDate(inputInfo.getDate());
        SootheeValidation.checkDomain(member, DomainType.MEMBER);
        SootheeValidation.checkReference(weather, ReferenceType.WEATHER);
        SootheeValidation.checkDouble(inputInfo.getScore(), DoubleType.SCORE);
        SootheeValidation.checkContent(inputInfo.getContent());
        SootheeValidation.checkOptionalContent(inputInfo.getHope(), ContentType.HOPE);
        SootheeValidation.checkOptionalContent(inputInfo.getThank(), ContentType.THANKS);
        SootheeValidation.checkOptionalContent(inputInfo.getLearn(), ContentType.LEARN);
    }

    /** validation */
    private void checkUpdateDairy(DairyDTO dairy, Weather weather) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkReference(weather, ReferenceType.WEATHER);
        SootheeValidation.checkDouble(score, DoubleType.SCORE);
        SootheeValidation.checkContent(dairy.getContent());
        SootheeValidation.checkOptionalContent(dairy.getHope(), ContentType.HOPE);
        SootheeValidation.checkOptionalContent(dairy.getThank(), ContentType.THANKS);
        SootheeValidation.checkOptionalContent(dairy.getLearn(), ContentType.LEARN);
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void valid() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomainId(getDairyId(), DomainType.DAIRY);
        SootheeValidation.checkDate(getDate());
        SootheeValidation.checkDomain(getMember(), DomainType.MEMBER);
        SootheeValidation.checkReference(getWeather(), ReferenceType.WEATHER);
        SootheeValidation.checkDouble(getScore(), DoubleType.SCORE);
        SootheeValidation.checkContent(getContent());
        SootheeValidation.checkOptionalContent(getHope(), ContentType.HOPE);
        SootheeValidation.checkOptionalContent(getThank(), ContentType.THANKS);
        SootheeValidation.checkOptionalContent(getLearn(), ContentType.LEARN);
        SootheeValidation.checkBoolean(getIsDelete(), BooleanType.DELETE);
    }
}