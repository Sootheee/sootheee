package com.soothee.custom.error;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Component
public class BindingErrorUtil {

    /**
     * 입력받은 각 파라미터의 validate 중 실패한 결과 response
     *
     * @param bindingResult BingingResult validate 실패 결과
     * @return 결과 재구조화 리스트
     */
    public List<BindingErrorResult> getErrorResponse(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<BindingErrorResult> result = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            result.add(new BindingErrorResult(fieldError));
        }
        return result;
    }


}
