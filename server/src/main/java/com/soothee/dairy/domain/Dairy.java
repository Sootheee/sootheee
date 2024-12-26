package com.soothee.dairy.domain;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.DoubleType;
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
    @Column(name = "is_delete", nullable = false, length = 1)
    private String isDelete;

    @Builder
    public Dairy(Member member, LocalDate date, Weather weather, Double score, String content, String hope, String thank, String learn) throws IncorrectValueException, NullValueException {
        checkConstructDairy(member, date, weather, score, content, hope, thank, learn);
        this.member = member;
        this.date = date;
        this.weather = weather;
        this.score = score;
        this.content = content;
        this.hope = hope;
        this.thank = thank;
        this.learn = learn;
        this.isDelete = BooleanYN.N.toString();
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
        this.isDelete = BooleanYN.Y.toString();
    }

    /**
     * 일기 생성
     *
     * @param inputInfo 입력된 등록할 일기 정보
     * @param member 로그인한 계정 정보
     * @param weather 해당 일기 날씨 정보
     * @return Dairy entity
     */
    public static Dairy of(DairyRegisterDTO inputInfo, Member member, Weather weather) throws IncorrectValueException, NullValueException {
        checkDiaryOfDiaryRegisterDTO(inputInfo, member, weather);
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

    @Override
    public Long getId() {
        return dairyId;
    }

    /** validation */
    private void checkConstructDairy(Member member, LocalDate date, Weather weather, Double score, String content, String hope, String thank, String learn) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomain(member, DomainType.MEMBER);
        SootheeValidation.checkDate(date);
        SootheeValidation.checkDomain(weather, DomainType.WEATHER);
        SootheeValidation.checkDouble(score, DoubleType.SCORE);
        SootheeValidation.checkContent(content);
        SootheeValidation.checkOptionalContent(hope, ContentType.HOPE);
        SootheeValidation.checkOptionalContent(thank, ContentType.THANKS);
        SootheeValidation.checkOptionalContent(learn, ContentType.LEARN);
    }

    /** validation */
    private static void checkDiaryOfDiaryRegisterDTO(DairyRegisterDTO inputInfo, Member member, Weather weather) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDate(inputInfo.getDate());
        SootheeValidation.checkDomain(member, DomainType.MEMBER);
        SootheeValidation.checkDomain(weather, DomainType.WEATHER);
        SootheeValidation.checkDouble(inputInfo.getScore(), DoubleType.SCORE);
        SootheeValidation.checkContent(inputInfo.getContent());
        SootheeValidation.checkOptionalContent(inputInfo.getHope(), ContentType.HOPE);
        SootheeValidation.checkOptionalContent(inputInfo.getThank(), ContentType.THANKS);
        SootheeValidation.checkOptionalContent(inputInfo.getLearn(), ContentType.LEARN);
    }

    /** validation */
    private void checkUpdateDairy(DairyDTO dairy, Weather weather) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomain(weather, DomainType.WEATHER);
        SootheeValidation.checkDouble(score, DoubleType.SCORE);
        SootheeValidation.checkContent(dairy.getContent());
        SootheeValidation.checkOptionalContent(dairy.getHope(), ContentType.HOPE);
        SootheeValidation.checkOptionalContent(dairy.getThank(), ContentType.THANKS);
        SootheeValidation.checkOptionalContent(dairy.getLearn(), ContentType.LEARN);
    }
}