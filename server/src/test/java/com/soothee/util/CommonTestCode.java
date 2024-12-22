package com.soothee.util;

import com.soothee.common.constants.SnsType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.domain.Member;
import com.soothee.member.domain.MemberDelReason;
import com.soothee.member.repository.MemberDelReasonRepository;
import com.soothee.member.repository.MemberRepository;
import com.soothee.member.service.MemberDelReasonService;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.domain.DelReason;
import com.soothee.reference.domain.Weather;
import com.soothee.reference.repository.ConditionRepository;
import com.soothee.reference.repository.DelReasonRepository;
import com.soothee.reference.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommonTestCode {
    @Autowired
    private final WeatherRepository weatherRepository;
    @Autowired
    private final ConditionRepository conditionRepository;
    @Autowired
    private final DelReasonRepository delReasonRepository;
    @Autowired
    private final DairyRepository dairyRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final DairyConditionRepository dairyConditionRepository;
    @Autowired
    private final MemberDelReasonRepository memberDelReasonRepository;
    @Autowired
    private final MemberDelReasonService memberDelReasonService;

    public static final Long MEMBER_ID = 11L;
    public static final String EMAIL = "abc@def.com";
    public static final String NAME = "사용자0";
    public static final String OAUTH2 = "111111";
    public static final SnsType SNS_TYPE = SnsType.KAKAOTALK;
    public static final String NEW_EMAIL = "def@abc.com";
    public static final String NEW_NAME = "사용자1";
    public static final String NEW_AUTH2 = "222222";
    public static final SnsType NEW_SNS_TYPE = SnsType.GOOGLE;
    public static final Long DAIRY_ID1 = 11L;
    public static final Long DAIRY_ID2 = 12L;
    public static final Long DAIRY_ID3 = 13L;
    public static final Long DAIRY_ID4 = 14L;
    public static final Long DAIRY_ID5 = 15L;
    public static final Long NEW_DAIRY_ID = 1L;
    public static final Double SCORE1 = 2.0;
    public static final Double SCORE2 = 5.5;
    public static final Double SCORE3 = 1.0;
    public static final Double SCORE4 = 9.5;
    public static final Double SCORE5 =4.5;
    public static final Double NEW_SCORE = 3.0;
    public static final int YEAR = 2024;
    public static final int MONTH = 10;
    public static final int WEEK = 40;
    public static final int NEW_DAY = 10;
    public static final LocalDate DATE1 = LocalDate.of(YEAR, MONTH, 1);
    public static final LocalDate DATE2 = LocalDate.of(YEAR, MONTH, 2);
    public static final LocalDate DATE3 = LocalDate.of(YEAR, MONTH, 3);
    public static final LocalDate DATE4 = LocalDate.of(YEAR, MONTH, 4);
    public static final LocalDate DATE5 = LocalDate.of(YEAR, MONTH, 5);
    public static final LocalDate NEW_DATE = LocalDate.of(YEAR, MONTH + 1, NEW_DAY);
    public static final Long WEATHER_ID = 1L;
    public static final Long COND_ID1 = 1L;
    public static final Long COND_ID2 = 7L;
    public static final Long COND_ID3 = 2L;
    public static final Long COND_ID4 = 10L;
    public static final Long NEW_COND_ID1 = 11L;
    public static final Long NEW_COND_ID2 = 12L;
    public static final Long NEW_COND_ID3 = 13L;
    public static final Long DEL_REASON_ID1 = 1L;
    public static final Long DEL_REASON_ID2 = 2L;
    public static final Long DEL_REASON_ID3 = 3L;

    public List<Condition> getConditions() {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(conditionRepository.findByCondId(COND_ID1).orElseThrow(NullPointerException::new));
        conditions.add(conditionRepository.findByCondId(COND_ID2).orElseThrow(NullPointerException::new));
        conditions.add(conditionRepository.findByCondId(COND_ID3).orElseThrow(NullPointerException::new));
        conditions.add(conditionRepository.findByCondId(COND_ID4).orElseThrow(NullPointerException::new));
        return conditions;
    }

    public Weather getWeather() {
        return weatherRepository.findByWeatherId(WEATHER_ID).orElseThrow(NullPointerException::new);
    }

    public List<Long> getDelReasonIds() {
        List<Long> delReasonIds = new ArrayList<>();
        delReasonIds.add(DEL_REASON_ID1);
        delReasonIds.add(DEL_REASON_ID2);
        delReasonIds.add(DEL_REASON_ID3);
        return delReasonIds;
    }

    public List<DelReason> getDelReasons() {
        List<DelReason> delReasons = new ArrayList<>();
        delReasons.add(delReasonRepository.findByReasonId(DEL_REASON_ID1).orElseThrow(NullPointerException::new));
        delReasons.add(delReasonRepository.findByReasonId(DEL_REASON_ID2).orElseThrow(NullPointerException::new));
        delReasons.add(delReasonRepository.findByReasonId(DEL_REASON_ID3).orElseThrow(NullPointerException::new));
        return delReasons;
    }

    public Member getSavedMember() {
        return memberRepository.findByMemberId(MEMBER_ID).orElseThrow(NullPointerException::new);
    }

    public Dairy getSavedDairy(Long dairyId) {
        return dairyRepository.findByDairyId(dairyId).orElseThrow(NullPointerException::new);
    }

    public Dairy getSavedNewDairy(Long newDairyId) {
        return dairyRepository.findByDairyId(newDairyId).orElseThrow(NullPointerException::new);
    }

    public Dairy getSavedNewDairy(LocalDate newDairyDate) {
        List<DairyDTO> list = dairyRepository.findByDate(CommonTestCode.MEMBER_ID, newDairyDate).orElseThrow(NullPointerException::new);
        DairyDTO dairyDTO = list.get(0);
        return getSavedNewDairy(dairyDTO.getDairyId());
    }

    public List<DairyCondition> getSavedDairyConditions() {
        return dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(DAIRY_ID1, "N").orElseThrow(NullPointerException::new);
    }

    public List<MemberDelReason> getSavedMemberDelReasons() {
        return memberDelReasonRepository.findByMemberMemberId(MEMBER_ID).orElseThrow(NullPointerException::new);
    }

    public List<Condition> getNewConditions() {
        List<Condition> newConditions = new ArrayList<>();
        newConditions.add(conditionRepository.findByCondId(NEW_COND_ID1).orElseThrow(NullPointerException::new));
        newConditions.add(conditionRepository.findByCondId(NEW_COND_ID2).orElseThrow(NullPointerException::new));
        newConditions.add(conditionRepository.findByCondId(NEW_COND_ID3).orElseThrow(NullPointerException::new));
        return newConditions;
    }

    public List<DairyCondition> getNewDairyConditions(Long newDairyId, String type) {
        List<DairyCondition> newDairyConditions = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(newDairyId, "N")
                                                        .orElseThrow(NullPointerException::new);
        for (int i = 0; i < newDairyConditions.size(); i++) {
            log.info("{} {} : {}, {}", type, i + 1, newDairyConditions.get(i).getDairyCondId(), newDairyConditions.get(i).getOrderNo());
        }
        return newDairyConditions;
    }

    public Dairy getNewDairy() throws IncorrectValueException, NullValueException {
        return Dairy.builder()
                .date(NEW_DATE)
                .member(getSavedMember())
                .weather(getWeather())
                .score(NEW_SCORE)
                .build();
    }

    public Member getNewMember() throws IncorrectValueException, NullValueException {
        return Member.builder()
                .email(NEW_EMAIL)
                .name(NEW_NAME)
                .snsType(NEW_SNS_TYPE)
                .oauth2ClientId(NEW_AUTH2)
                .build();
    }

    public Member deleteMember() throws IncorrectValueException, NullValueException {
        Member newMember = saveNewMember();
        newMember.deleteMember();
        memberDelReasonService.saveDeleteReasons(newMember, getDelReasonIds());
        return newMember;
    }

    public Member saveNewMember() throws IncorrectValueException, NullValueException {
        Member newMember = getNewMember();
        memberRepository.save(newMember);
        return newMember;
    }

    public Dairy saveNewDairy() throws IncorrectValueException, NullValueException {
        Dairy newDairy = getNewDairy();
        dairyRepository.save(newDairy);
        return newDairy;
    }

    public Dairy saveNewDairyCondition() throws IncorrectValueException, NullValueException {
        int index = 0;
        Dairy newDairy = saveNewDairy();
        for (Condition condition : getNewConditions()) {
            DairyCondition newDC = DairyCondition.builder()
                                                .dairy(newDairy)
                                                .condition(condition)
                                                .orderNo(index++)
                                                .build();
            dairyConditionRepository.save(newDC);
        }
        return newDairy;
    }
}
