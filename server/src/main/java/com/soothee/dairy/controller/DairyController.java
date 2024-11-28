package com.soothee.dairy.controller;

import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.MonthlyDairyScoreDTO;
import com.soothee.dairy.service.DairyService;
import com.soothee.member.dto.AllMemberInfoDTO;
import com.soothee.member.dto.NameMemberInfoDTO;
import com.soothee.oauth2.domain.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Dairy API", description = "일기 관련 처리")
@RequestMapping("/dairy")
public class DairyController {
    private final DairyService dairyService;

    /** 달력(홈)에 표시될 해당 월의 모든 일기 점수 조회 */
    @GetMapping("/calendar")
    @Operation(summary = "달력의 출력될 오늘의 점수 리스트 조회", description = "로그인한 계정이 지정한 달에 작성한 모든 일기 일련번호와 각 오늘의 점수 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/dairy/calendar?year=2022", in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 월", example = "/dairy/calendar?year=2022&month=2", in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = MonthlyDairyScoreDTO.class))),
            @ApiResponse(responseCode = "202", description = "조회된 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendAllDairyMonthly(@RequestParam("year") Integer year,
                                                 @RequestParam("month") Integer month,
                                                 @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        List<MonthlyDairyScoreDTO> infos = dairyService.getAllDairyMonthly(loginInfo, year, month);
        return new ResponseEntity<List<MonthlyDairyScoreDTO>>(infos, HttpStatus.OK);
    }
}
