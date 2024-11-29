package com.soothee.dairy.controller;

import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.service.DairyService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Dairy API", description = "일기 관련 처리")
@RequestMapping("/dairy")
public class DairyController {
    private final DairyService dairyService;

    /** 달력(홈)에 표시될 해당 월의 모든 일기 점수 조회 */
    @GetMapping("/calendar")
    @Operation(summary = "달력의 출력될 일기들의 오늘의 점수 리스트 조회", description = "로그인한 계정이 지정한 달에 작성한 모든 일기 일련번호와 각 오늘의 점수 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/dairy/calendar?year=2022", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 월", example = "/dairy/calendar?year=2022&month=2", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = DairyScoresDTO.class))),
            @ApiResponse(responseCode = "202", description = "조회된 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendAllDairyMonthly(@RequestParam("year") Integer year,
                                                 @RequestParam("month") Integer month,
                                                 @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        List<DairyScoresDTO> infos = dairyService.getAllDairyMonthly(loginInfo, year, month);
        return new ResponseEntity<List<DairyScoresDTO>>(infos, HttpStatus.OK);
    }

    @PostMapping("/dairy")
    @Operation(summary = "새 일기 등록", description = "로그인한 계정이 작성한 새 특정일자 일기 등록", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "date", description = "해당 날짜", example = "date=2024-10-11", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "weatherId", description = "날씨 일련번호", example = "weatherId=11", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "score", description = "오늘의 점수", example = "score=3.2", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "condId", description = "다중 선택한 컨디션 일련번호 리스트", example = "condId=[1,2,4,5,3]", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "content", description = "오늘의 요약", example = "content=개발을했다", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "hope", description = "바랐던 방향성", example = "hope=놀고싶다", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "thank", description = "감사한 일", example = "thank=점심을먹었다", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "learn", description = "배운 일", example = "learn=사회는액팅이다", required = false, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> registerDairy(@ModelAttribute DairyRegisterDTO inputInfo,
                                           @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        dairyService.registerDairy(loginInfo, inputInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
