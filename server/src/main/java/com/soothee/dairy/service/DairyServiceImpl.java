package com.soothee.dairy.service;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.DomainType;
import com.soothee.custom.exception.*;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.dto.InputDairyDTO;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.domain.Member;
import com.soothee.member.service.MemberService;
import com.soothee.reference.domain.Weather;
import com.soothee.reference.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DairyServiceImpl implements DairyService {
    private final DairyRepository dairyRepository;
    private final MemberService memberService;
    private final WeatherService weatherService;
    private final DairyConditionService dairyConditionService;

    @Override
    public List<DairyScoresDTO> getAllDairyMonthly(Long memberId, MonthParam monthParam) throws IncorrectValueException, NullValueException {
        /* 현재 로그인한 계정이 지정된 년도/월에 작성한 모든 일기 조회 */
        List<DairyScoresDTO> result =  dairyRepository.findByMemberIdYearMonth(memberId, monthParam)
                /* 지정된 년도/월에 작성한 일기가 없는 경우 빈 리스트 리턴 */
                .orElse(new ArrayList<>());

        if (result.isEmpty()) {
            /* 지정된 년도/월에 작성한 일기가 없는 경우 더 이상 진행하지 않고 리턴 */
            return result;
        }

        for (DairyScoresDTO dairyScore : result) {
            /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
            dairyScore.valid();
        }
        return result;
    }

    @Override
    public DairyDTO getDairyByDate(Long memberId, LocalDate date) throws NotExistDairyException, DuplicatedResultException, NotFoundDetailInfoException, IncorrectValueException, NullValueException {
        /* 현재 로그인한 계정이 지정한 날짜에 작성한 고유한 하나의 일기 조회 */
        List<DairyDTO> dairyDTO = dairyRepository.findByDate(memberId, date)
                /* 지정한 날짜에 작성된 일기가 없는 경우 Exception 발생 */
                .orElseThrow(() -> new NotExistDairyException(memberId, date));

        /* 같은 작성 일자의 일기가 1개 초과 중복 등록된 경우 Exception 발생 */
        SootheeValidation.checkDairyDuplicate(dairyDTO.size());

        /* 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생
         * 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        return getDairyDTO(dairyDTO);
    }

    @Override
    public DairyDTO getDairyByDairyId(Long memberId, Long dairyId) throws NotExistDairyException, DuplicatedResultException, NotFoundDetailInfoException, IncorrectValueException, NullValueException {
        /* 현재 로그인한 계정이 작성한 지정한 일기 일련번호를 가진 고유한 하나의 일기 조회 */
        List<DairyDTO> dairyDTO = dairyRepository.findByMemberDiaryId(memberId, dairyId)
                /* 지정한 일기 일련번호를 가진 일기가 없는 경우 Exception 발생 */
                .orElseThrow(() -> new NotExistDairyException(memberId, dairyId));

        /* 같은 일기 일련번호의 일기가 1개 초과 중복 등록된 경우 Exception 발생 */
        SootheeValidation.checkDairyDuplicate(dairyDTO.size());

        /* 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생
         * 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        return getDairyDTO(dairyDTO);
    }

    private DairyDTO getDairyDTO(List<DairyDTO> dairyDTO) throws NotFoundDetailInfoException, IncorrectValueException, NullValueException {
        DairyDTO result = dairyDTO.get(0);
        /* 해당 일기에 선택된 컨디션이 있는지 확인 */
        if (dairyConditionService.isConditionExistInDairy(result.getDairyId())) {
            /* 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생
             * 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
            result.setCondIdList(dairyConditionService.getConditionsIdListByDairy(result.getDairyId()));
        }
        /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        result.valid();
        return result;
    }

    @Override
    public void registerDairy(Long memberId, DairyRegisterDTO inputInfo) throws DuplicatedResultException, NullValueException, IncorrectValueException, NotExistMemberException {
        /* 현재 로그인한 회원이 해당 일자에 써놓은 일기가 있는지 중복 체크
         * 입력한 새 일기의 작성 날짜에 이미 등록된 일기가 있는 경우 Exception 발생 */
        checkDuplicateDairy(memberId, inputInfo.getDate());

        /* 해당 회원 일련번호로 조회된 회원 정보가 없는 경우 Exception 발생
         * 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        Member member = memberService.getMemberById(memberId);
        /* 해당 날씨 일련번호로 조회된 날씨가 없는 경우 Exception 발생 */
        Weather weather = weatherService.getWeatherById(inputInfo.getWeatherId());

        /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        Dairy newDairy = Dairy.of(inputInfo, member, weather);

        /* 일기 등록 */
        dairyRepository.save(newDairy);
        /* 일기에 선택한 컨디션이 있는 경우 */
        if (isExistInputCondList(inputInfo)) {
            /* 해당 일기의 선택한 컨디션을 등록
             * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
            dairyConditionService.saveConditions(inputInfo.getCondIdList(), newDairy);
        }
    }

    @Override
    public void modifyDairy(Long memberId, Long dairyId, DairyDTO inputInfo) throws NotExistDairyException, NotMatchedException, NullValueException, IncorrectValueException, NotFoundDetailInfoException {
        /* 해당 일련번호로 기존 일기 조회
         * - 지정한 일기 일련번호를 가진 일기가 없는 경우 Exception 발생
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        Dairy dairy = getDairyByDairyId(inputInfo.getDairyId());

        /* 기존 일기 일련번호와 입력한 일기 일련번호가 다른 경우 Exception 발생 */
        SootheeValidation.checkMatchedId(dairyId, dairy.getDairyId(), DomainType.DAIRY);
        /* 기존 일기 작성 날짜와 입력한 일기 작성 날짜가 다른 경우 Exception 발생 */
        SootheeValidation.checkMatchedDate(dairy.getDate(), inputInfo.getDate());
        /* 기존 일기 작성 회원 일련번호와 현재 로그인한 계정의 회원 일련번호가 다른 경우 Exception 발생 */
        SootheeValidation.checkMatchedId(dairy.getMember().getMemberId(), memberId, DomainType.MEMBER);
        /* 해당 날씨 일련번호로 조회된 날씨가 없는 경우 Exception 발생 */
        Weather weather = weatherService.getWeatherById(inputInfo.getWeatherId());

        /* 기존 일기 수정 - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 */
        dairy.updateDairy(inputInfo, weather);
        /* 수정된 일기 정보에 선택한 컨디션이 있는 경우 */
        if (isExistInputCondList(inputInfo)) {
            /* 해당 일기의 기존 선택한 컨디션과 비교하여
             * 기존에 있던 것 중 상이한 것들은 소프트 삭제하고 새로 추가된 선택한 컨디션은 등록
             * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
            dairyConditionService.updateConditions(dairy, inputInfo.getCondIdList());
        }
    }

    @Override
    public void deleteDairy(Long memberId, Long dairyId) throws NotExistDairyException, NotMatchedException, NotFoundDetailInfoException, IncorrectValueException, NullValueException {
        /* 해당 일련번호로 기존 일기 조회
         * - 지정한 일기 일련번호를 가진 일기가 없는 경우 Exception 발생 */
        Dairy dairy = getDairyByDairyId(dairyId);

        /* 기존 일기 작성 회원 일련번호와 현재 로그인한 계정의 회원 일련번호가 다른 경우 Exception 발생 */
        SootheeValidation.checkMatchedId(dairy.getMember().getMemberId(), memberId, DomainType.MEMBER);

        /* 해당 일기에 선택된 컨디션이 있는지 확인 */
        if (dairyConditionService.isConditionExistInDairy(dairyId)) {
            /* 해당 일기의 선택돤 컨디션 삭제
             * - 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생 */
            dairyConditionService.deleteDairyConditionsOfDairy(dairy);
        }
        /* 일기 삭제 */
        dairy.deleteDairy();
    }

    /**
     * 현재 로그인한 회원이 해당 일자에 써놓은 일기가 있는지 중복 체크
     *
     * @param memberId 현재 로그인한 회원의 일련번호
     * @param date 조회할 날짜
     */
    private void checkDuplicateDairy(Long memberId, LocalDate date) throws DuplicatedResultException {
        if (dairyRepository.findByDate(memberId, date).isPresent()) {
            throw new DuplicatedResultException(DomainType.DAIRY);
        }
    }

    /**
     * 일기 일련변호로 일기 가져오기
     * 1. 지정한 일기 일련번호를 가진 일기가 없는 경우 Exception 발생
     * 2. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     *
     * @param dairyId 가져올 일기 일련번호
     * @return 가져온 일기
     */
    private Dairy getDairyByDairyId(Long dairyId) throws NotExistDairyException, IncorrectValueException, NullValueException {
        Dairy result = dairyRepository.findByDairyIdAndIsDelete(dairyId, BooleanYN.N.toString())
                /* 지정한 일기 일련번호를 가진 일기가 없는 경우 Exception 발생 */
                .orElseThrow(() -> new NotExistDairyException(dairyId));
        /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        result.valid();
        return result;
    }

    /**
     * 입력 값에 선택한 컨디션 일련번호가 있는지 확인
     *
     * @param inputInfo 입력 값
     * @return 있으면 true / 없으면 false
     */
    private static boolean isExistInputCondList(InputDairyDTO inputInfo) {
        return Objects.nonNull(inputInfo.getCondIdList());
    }
}
