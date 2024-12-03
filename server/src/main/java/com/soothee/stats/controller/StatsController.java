package com.soothee.stats.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Tag(name = "Stats API", description = "통계 관련 처리")
@RequestMapping("/stats")
public class StatsController {
}
