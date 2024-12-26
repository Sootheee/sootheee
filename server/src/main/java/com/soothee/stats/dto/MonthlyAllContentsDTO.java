package com.soothee.stats.dto;

import com.soothee.common.constants.ContentType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.List;

/**
 * 한 달 동안 작성한 모든 감사한/배운 일 리스트
 *  1. 한 달 동안 감사한/배운 일 작성 횟수
 *  2. 한 달 동안 작성한 모든 감사한/배운 일 정보
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "한 달 동안 작성한 모든 감사한/배운 일 리스트 조회")
public class MonthlyAllContentsDTO {
    @NotEmpty(message = "한 달 동안 감사한/배운 일 작성 횟수가 없습니다.")
    @PositiveOrZero(message = "작성 횟수는 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "한 달 동안 감사한/배운 일 작성 횟수")
    private Integer count;

    @NotEmpty(message = "작성한 감사한/배운 일이 없습니다.")
    @Schema(description = "한 달 동안 작성한 모든 감사한/배운 일 정보")
    private List<DateContents> contentList;

    public MonthlyAllContentsDTO(Integer count, ContentType type) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkInteger(count, type);
        this.count = count;
    }
}
