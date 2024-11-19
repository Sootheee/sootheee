package com.soothee.dairy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Tag(name = "Dairy API", description = "다이어리 관련 처리")
@RequestMapping("/dairy")
public class DairyController {
}
